package com.github.peterrk.protocache;

import java.io.ByteArrayOutputStream;

public class Utils {
    private static final class Int {
        int v = 0;
    }

    private static int pick(byte[] src, Int k) {
        int cnt = 1;
        byte ch = src[k.v++];
        if (ch == 0) {
            while (k.v < src.length && cnt < 4 && src[k.v] == 0) {
                k.v++;
                cnt++;
            }
            return 0x8 | (cnt - 1);
        } else if (ch == (byte)0xff) {
            while (k.v < src.length && cnt < 4 && src[k.v] == (byte)0xff) {
                k.v++;
                cnt++;
            }
            return 0xC | (cnt - 1);
        } else {
            while (k.v < src.length && cnt < 7 && src[k.v] != 0 && src[k.v] != (byte)0xff) {
                k.v++;
                cnt++;
            }
            return cnt;
        }
    }

    public static byte[] compress(byte[] src) {
        if (src.length == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int n = src.length;
        while ((n & ~0x7f) != 0) {
            buf.write(0x80 | (n & 0x7f));
            n >>>= 7;
        }
        buf.write(n);

        Int k = new Int();
        while (k.v < src.length) {
            int x = k.v;
            int a = pick(src, k);
            if (k.v == src.length) {
                buf.write(a);
                if ((a & 0x8) == 0) {
                    buf.write(src, x, a);
                }
                break;
            }
            int y = k.v;
            int b = pick(src, k);
            buf.write(a|(b<<4));
            if ((a & 0x8) == 0) {
                buf.write(src, x, a);
            }
            if ((b & 0x8) == 0) {
                buf.write(src, y, b);
            }
        }
        return buf.toByteArray();
    }

    private static boolean unpack(byte[] src, Int k, ByteArrayOutputStream buf, int mark) {
        if ((mark & 8) != 0) {
            int cnt = (mark & 3) + 1;
            int ch = 0;
            if ((mark & 4) != 0) {
                ch = 0xff;
            }
            for (; cnt != 0; cnt--) {
                buf.write(ch);
            }
        } else {
            int l = mark & 7;
            if (k.v+l > src.length) {
                return false;
            }
            buf.write(src, k.v, l);
            k.v += l;
        }
        return true;
    }

    public static byte[] decompress(byte[] src) {
        if (src.length == 0) {
            return new byte[0];
        }
        Int k = new Int();
        int size = 0;
        for (int sft = 0; sft < 32; sft += 7) {
            byte b = src[k.v++];
            size |= ((int) b & 0x7f) << sft;
            if ((b & 0x80) == 0) {
                break;
            }
        }

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        while (k.v < src.length) {
            int mark = src[k.v++] & 0xff;
            if (!unpack(src, k, buf, mark&0xf) || !unpack(src, k, buf, mark>>4)) {
                throw new IllegalArgumentException("broken data");
            }
        }
        byte[] out = buf.toByteArray();
        if (out.length != size) {
            throw new IllegalArgumentException("size mismatch");
        }
        return out;
    }
}
