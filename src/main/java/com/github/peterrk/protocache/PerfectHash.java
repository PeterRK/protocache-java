// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;

public class PerfectHash {

    private final DataView data;
    private final int size;
    private final int section;

    private final int byteSize;

    public PerfectHash(DataView data) {
        if (data.size() < 4) {
            throw new IllegalArgumentException("too short data");
        }
        this.data = data;
        this.size = this.data.getInt() & 0xfffffff;
        if (size < 2) {
            section = 0;
            byteSize = 4;
            return;
        }
        section = calcSectionSize(size);
        int n = calcBitmapSize(section);
        if (size > 0xffff) {
            n += n / 2;
        } else if (size > 0xff) {
            n += n / 4;
        } else if (size > 24) {
            n += n / 8;
        }
        byteSize = n + 8;
        if (data.size() < byteSize) {
            throw new IllegalArgumentException("too short data");
        }
    }

    public PerfectHash(byte[] data) {
        this(new DataView(data));
    }

    private PerfectHash(byte[] data, int size, int section) {
        this.data = new DataView(data);
        this.size = size;
        this.section = section;
        this.byteSize = data.length;
    }

    private static int calcSectionSize(int size) {
        return Math.max(10, (int) ((size * 105L + 255) / 256));
    }

    private static int calcBitmapSize(int section) {
        return ((section * 3 + 31) & ~31) / 4;
    }

    private static int countValidSlot(long v) {
        v &= (v >>> 1);
        v = (v & 0x1111111111111111L) + ((v >>> 2) & 0x1111111111111111L);
        v = v + (v >>> 4);
        v = v + (v >>> 8);
        v = (v & 0xf0f0f0f0f0f0f0fL) + ((v >>> 16) & 0xf0f0f0f0f0f0f0fL);
        v = v + (v >>> 32);
        return 32 - ((int) v & 0xff);
    }

    private static int[] hash(int seed, int section, byte[] key) {
        int[] out = new int[3];
        Hash.V128 h = Hash.hash128(key, (long) seed & 0xffffffffL);
        out[0] = Integer.remainderUnsigned((int) h.low, section);
        out[1] = Integer.remainderUnsigned((int) (h.low >>> 32), section) + section;
        out[2] = Integer.remainderUnsigned((int) h.high, section) + section * 2;
        return out;
    }

    private static int bit2(DataView bitmap, int pos) {
        int blk = pos >>> 2;
        int sft = (pos & 3) << 1;
        return (bitmap.data[bitmap.offset + blk] >>> sft) & 3;
    }

    private static void setBit2on11(DataView bitmap, int pos, int val) {
        int blk = pos >>> 2;
        int sft = (pos & 3) << 1;
        bitmap.data[bitmap.offset + blk] ^= (byte) ((~val & 3) << sft);
    }

    private static byte[] build(KeySource src, int width) {
        int size = src.total();
        int section = calcSectionSize(size);
        int bmsz = calcBitmapSize(section);
        int bytes = 8 + bmsz;
        if (bmsz > 8) {
            bytes += (bmsz / 8) * width;
        }
        byte[] data = new byte[bytes];

        Graph graph = new Graph(size);
        int[] free = new int[size];
        BitSet book = new BitSet(size);

        DataView header = new DataView(data);
        DataView bitmap = new DataView(data, 8);
        DataView table = new DataView(data, 8 + bmsz);
        header.putInt(size);

        Random rand = new Random();
        for (int chance = (width == 1) ? 40 : 16; chance >= 0; chance--) {
            int seed = rand.nextInt();
            header.putInt(seed, 4);
            if (!graph.init(seed, src) || !graph.tear(free, book)) {
                continue;
            }
            graph.mapping(free, book, bitmap);
            if (bmsz > 8) {
                int cnt = 0;
                switch (width) {
                    case 4:
                        for (int i = 0; i < bmsz / 8; i++) {
                            table.putInt(cnt, i * 4);
                            cnt += countValidSlot(bitmap.getLong(i * 8));
                        }
                        break;
                    case 2:
                        for (int i = 0; i < bmsz / 8; i++) {
                            table.putShort((short) cnt, i * 2);
                            cnt += countValidSlot(bitmap.getLong(i * 8));
                        }
                        break;
                    default:
                        for (int i = 0; i < bmsz / 8; i++) {
                            table.data[table.offset + i] = (byte) cnt;
                            cnt += countValidSlot(bitmap.getLong(i * 8));
                        }
                        break;
                }
                if (cnt != size) {
                    throw new RuntimeException("item lost");
                }
            }
            return data;
        }

        throw new RuntimeException("fail to build perfect-hash");
    }

    public static PerfectHash build(KeySource src) {
        int size = src.total();
        if (size < 0 || size > 0xfffffff) {
            throw new IllegalArgumentException("illegal size");
        }
        byte[] data = null;
        if (size > 0xffff) {
            data = build(src, 4);
        } else if (size > 0xff) {
            data = build(src, 2);
        } else if (size > 1) {
            data = build(src, 1);
        } else {
            PerfectHash out = new PerfectHash(new byte[4], size, 0);
            out.data.putInt(size);
            return out;
        }
        return new PerfectHash(data, size, calcSectionSize(size));
    }

    public DataView getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public int getByteSize() {
        return byteSize;
    }

    public int locate(byte[] key) {
        if (size < 2) {
            return 0;
        }
        int[] slots = hash(data.getInt(4), section, key);

        DataView bitmap = new DataView(data.data, data.offset + 8);
        int m = bit2(bitmap, slots[0]) + bit2(bitmap, slots[1]) + bit2(bitmap, slots[2]);
        int slot = slots[m % 3];

        int a = slot >>> 5;
        int b = slot & 31;
        DataView table = new DataView(data.data, data.offset + 8 + calcBitmapSize(section));

        int off = 0;
        if (size > 0xffff) {
            off = table.getInt(a * 4);
        } else if (size > 0xff) {
            off = (int) table.getShort(a * 2) & 0xffff;
        } else if (size > 24) {
            off = (int) table.data[table.offset + a] & 0xff;
        }

        long block = bitmap.getLong(a * 8);
        block |= 0xffffffffffffffffL << (b << 1);
        return off + countValidSlot(block);
    }

    public interface KeySource {
        void reset();

        int total();

        byte[] next();
    }

    private static class Vertex {
        public int slot;
        public int next;
    }

    private static class Node {
        public int head;
        public int size;
    }

    private static final class Graph {
        public final Vertex[][] edges;
        public final Node[] nodes;

        public Graph(int size) {
            edges = new Vertex[size][];
            for (int i = 0; i < size; i++) {
                edges[i] = new Vertex[3];
                for (int j = 0; j < 3; j++) {
                    edges[i][j] = new Vertex();
                }
            }
            int section = calcSectionSize(size);
            nodes = new Node[section * 3];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new Node();
            }
        }

        private static boolean testAndSet(BitSet book, int pos) {
            if (book.get(pos)) {
                return false;
            }
            book.set(pos);
            return true;
        }

        public boolean init(int seed, KeySource src) {
            for (Node value : nodes) {
                value.head = -1;
                value.size = 0;
            }
            int section = nodes.length / 3;
            int total = src.total();
            src.reset();
            for (int i = 0; i < total; i++) {
                byte[] key = src.next();
                int[] slots = hash(seed, section, key);
                Vertex[] edge = edges[i];
                for (int j = 0; j < 3; j++) {
                    Vertex v = edge[j];
                    v.slot = slots[j];
                    Node node = nodes[v.slot];
                    v.next = node.head;
                    node.head = i;
                    if (++node.size > 50) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean tear(int[] free, BitSet book) {
            book.clear();

            int tail = 0;
            for (int i = edges.length - 1; i >= 0; i--) {
                Vertex[] edge = edges[i];
                for (int j = 0; j < 3; j++) {
                    Vertex v = edge[j];
                    if (nodes[v.slot].size == 1 && testAndSet(book, i)) {
                        free[tail++] = i;
                    }
                }
            }

            for (int head = 0; head < tail; head++) {
                int curr = free[head];
                Vertex[] edge = edges[curr];
                for (int j = 0; j < 3; j++) {
                    Vertex v = edge[j];
                    Node node = nodes[v.slot];
                    if (node.head == curr) {
                        node.head = v.next;
                    } else {
                        Vertex u = edges[node.head][j];
                        while (u.next != curr) {
                            u = edges[u.next][j];
                        }
                        u.next = v.next;
                    }
                    v.next = -1;
                    if (--node.size == 1 && testAndSet(book, node.head)) {
                        free[tail++] = node.head;
                    }
                }
            }

            return tail == free.length;
        }


        public void mapping(int[] free, BitSet book, DataView bitmap) {
            book.clear();
            Arrays.fill(bitmap.data, bitmap.offset, bitmap.offset + calcBitmapSize(nodes.length / 3), (byte) -1);

            for (int i = free.length - 1; i >= 0; i--) {
                Vertex[] edge = edges[free[i]];
                int a = edge[0].slot;
                int b = edge[1].slot;
                int c = edge[2].slot;
                if (testAndSet(book, a)) {
                    book.set(b);
                    book.set(c);
                    int sum = bit2(bitmap, b) + bit2(bitmap, c);
                    setBit2on11(bitmap, a, (6 - sum) % 3);
                } else if (testAndSet(book, b)) {
                    book.set(c);
                    int sum = bit2(bitmap, a) + bit2(bitmap, c);
                    setBit2on11(bitmap, b, (7 - sum) % 3);
                } else if (testAndSet(book, c)) {
                    int sum = bit2(bitmap, a) + bit2(bitmap, b);
                    setBit2on11(bitmap, c, (8 - sum) % 3);
                } else {
                    throw new RuntimeException("broken graph");
                }
            }
        }
    }
}
