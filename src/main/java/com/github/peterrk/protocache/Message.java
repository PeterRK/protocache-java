// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class Message implements IUnit {
    private final static byte[] empty = new byte[4];
    private final static byte[] emptyBytes = new byte[0];

    static {
        Data.putInt(empty, 0, 0);
    }

    private byte[] data;
    private int offset = -1;

    public Message() {}
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

    private int calcFieldOffset(int id) {
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

    public boolean fetchBool(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return false;
        }
        return data[fieldOffset] != 0;
    }

    public int fetchInt32(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getInt(data, fieldOffset);
    }

    public long fetchInt64(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getLong(data, fieldOffset);
    }

    public float fetchFloat32(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getFloat(data, fieldOffset);
    }

    public double fetchFloat64(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return 0;
        }
        return Data.getDouble(data, fieldOffset);
    }

    public byte[] fetchBytes(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return emptyBytes;
        }
        return Bytes.extractBytes(data, IUnit.jump(data, fieldOffset));
    }

    public String fetchString(int id) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            return "";
        }
        return Bytes.extractString(data, IUnit.jump(data, fieldOffset));
    }

    public <T extends IUnit> T fetchObject(int id, T unit) {
        int fieldOffset = calcFieldOffset(id);
        if (fieldOffset < 0) {
            unit.init(null, -1);
            return unit;
        }
        return IUnit.initByField(data, calcFieldOffset(id), unit);
    }

    public BoolArray fetchBoolArray(int id) {
        return fetchObject(id, new BoolArray());
    }

    public Int32Array fetchInt32Array(int id) {
        return fetchObject(id, new Int32Array());
    }

    public Int64Array fetchInt64Array(int id) {
        return fetchObject(id, new Int64Array());
    }

    public Float32Array fetchFloat32Array(int id) {
        return fetchObject(id, new Float32Array());
    }

    public Float64Array fetchFloat64Array(int id) {
        return fetchObject(id, new Float64Array());
    }

    public StringArray fetchStringArray(int id) {
        return fetchObject(id, new StringArray());
    }

    public BytesArray fetchBytesArray(int id) {
        return fetchObject(id, new BytesArray());
    }
}