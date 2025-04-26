// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Message extends Unit {
    private final static byte[] empty = new byte[4];

    static {
        Data.putInt(empty, 0, 0);
    }

    private byte[] data;
    private int offset = -1;

    public Message() {
    }

    public Message(byte[] data, int offset) {
        init(data, offset);
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            this.data = empty;
            this.offset = 0;
            return;
        }
        this.data = data;
        this.offset = offset;
    }

    public boolean hasField(int id) {
        int section = (int) data[offset] & 0xff;
        if (id < 12) {
            int v = Data.getInt(data, offset) >>> 8;
            int width = (v >>> (id << 1)) & 3;
            return width != 0;
        } else {
            int a = (id - 12) / 25;
            int b = (id - 12) % 25;
            if (a >= section) {
                return false;
            }
            long v = Data.getLong(data, offset + 4 + a * 8);
            int width = (int) (v >>> (b << 1)) & 3;
            return width != 0;
        }
    }

    private int getFieldOffset(int id) {
        int section = (int) data[offset] & 0xff;
        int off = 1 + section * 2;
        if (id < 12) {
            int v = Data.getInt(data, offset) >>> 8;
            int width = (v >>> (id << 1)) & 3;
            if (width == 0) {
                return -1;
            }
            v &= ~(0xffffffff << (id << 1));
            v = (v & 0x33333333) + ((v >>> 2) & 0x33333333);
            v = v + (v >>> 4);
            v = (v & 0xf0f0f0f) + ((v >>> 8) & 0xf0f0f0f);
            v = v + (v >>> 16);
            off += (v & 0xff);
        } else {
            int a = (id - 12) / 25;
            int b = (id - 12) % 25;
            if (a >= section) {
                return -1;
            }
            long v = Data.getLong(data, offset + 4 + a * 8);
            int width = (int) (v >>> (b << 1)) & 3;
            if (width == 0) {
                return -1;
            }
            off += (int) (v >>> 50);
            v &= ~(0xffffffffffffffffL << (b << 1));
            v = (v & 0x3333333333333333L) + ((v >>> 2) & 0x3333333333333333L);
            v = v + (v >>> 4);
            v = (v & 0xf0f0f0f0f0f0f0fL) + ((v >>> 8) & 0xf0f0f0f0f0f0f0fL);
            v = v + (v >>> 16);
            v = v + (v >>> 32);
            off += ((int) v & 0xff);
        }
        return offset + off * 4;
    }

    public boolean getBool(int id) {
        int fieldOffset = getFieldOffset(id);
        if (fieldOffset < 0) {
            return false;
        }
        return data[fieldOffset] != 0;
    }

    public int getInt32(int id) {
        int fieldOffset = getFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getInt(data, fieldOffset);
    }

    public long getInt64(int id) {
        int fieldOffset = getFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getLong(data, fieldOffset);
    }

    public float getFloat32(int id) {
        int fieldOffset = getFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getFloat(data, fieldOffset);
    }

    public double getFloat64(int id) {
        int fieldOffset = getFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getDouble(data, fieldOffset);
    }

    public byte[] getBytes(int id) {
        return Bytes.extractBytes(data, Unit.jump(data, getFieldOffset(id)));
    }

    public String getStr(int id) {
        return Bytes.extractString(data, Unit.jump(data, getFieldOffset(id)));
    }

    public <T extends Unit> T getField(int id, Supplier<T> supplier) {
        return Unit.NewByField(data, getFieldOffset(id), supplier);
    }

    public <T extends Unit> T getField(int id, T unit) {
        unit.initByField(data, getFieldOffset(id));
        return unit;
    }

    public BoolArray getBoolArray(int id) {
        return Unit.NewByField(data, getFieldOffset(id), BoolArray::new);
    }

    public Int32Array getInt32Array(int id) {
        return Unit.NewByField(data, getFieldOffset(id), Int32Array::new);
    }

    public Int64Array getInt64Array(int id) {
        return Unit.NewByField(data, getFieldOffset(id), Int64Array::new);
    }

    public Float32Array getFloat32Array(int id) {
        return Unit.NewByField(data, getFieldOffset(id), Float32Array::new);
    }

    public Float64Array getFloat64Array(int id) {
        return Unit.NewByField(data, getFieldOffset(id), Float64Array::new);
    }

    public StringArray getStrArray(int id) {
        return Unit.NewByField(data, getFieldOffset(id), StringArray::new);
    }

    public BytesArray getBytesArray(int id) {
        return Unit.NewByField(data, getFieldOffset(id), BytesArray::new);
    }
}