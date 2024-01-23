// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Message extends IUnit.Complex {
    private final static DataView empty = new DataView(new byte[4]);

    static {
        empty.putInt(0);
    }

    private DataView view;

    public Message() {
    }

    public Message(DataView data) {
        init(data);
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            view = empty;
            return;
        }
        view = data;
    }

    public boolean hasField(int id) {
        int section = (int) view.data[view.offset] & 0xff;
        if (id < 12) {
            int v = view.getInt() >>> 8;
            int width = (v >>> (id << 1)) & 3;
            return width != 0;
        } else {
            int a = (id - 12) / 25;
            int b = (id - 12) % 25;
            if (a >= section) {
                return false;
            }
            long v = view.getLong(4 + a * 8);
            int width = (int) (v >>> (b << 1)) & 3;
            return width != 0;
        }
    }

    private DataView getField(int id) {
        int section = (int) view.data[view.offset] & 0xff;
        int off = 1 + section * 2;
        if (id < 12) {
            int v = view.getInt() >>> 8;
            int width = (v >>> (id << 1)) & 3;
            if (width == 0) {
                return null;
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
                return null;
            }
            long v = view.getLong(4 + a * 8);
            int width = (int) (v >>> (b << 1)) & 3;
            if (width == 0) {
                return null;
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
        return new DataView(view.data, view.offset + off * 4);
    }

    public boolean getBool(int id) {
        DataView field = getField(id);
        if (field == null) {
            return false;
        }
        return field.data[field.offset] != 0;
    }

    public int getInt32(int id) {
        DataView field = getField(id);
        if (field == null) {
            return 0;
        }
        return field.getInt();
    }

    public long getInt64(int id) {
        DataView field = getField(id);
        if (field == null) {
            return 0;
        }
        return field.getLong();
    }

    public float getFloat32(int id) {
        DataView field = getField(id);
        if (field == null) {
            return 0;
        }
        return field.getFloat();
    }

    public double getFloat64(int id) {
        DataView field = getField(id);
        if (field == null) {
            return 0;
        }
        return field.getDouble();
    }

    public byte[] getBytes(int id) {
        return IUnit.NewByField(getField(id), Bytes::new).get();
    }

    public String getStr(int id) {
        return IUnit.NewByField(getField(id), Str::new).get();
    }

    public <T extends IUnit.Complex> T getField(int id, Supplier<T> supplier) {
        return IUnit.NewByField(getField(id), supplier);
    }

    public BoolArray getBoolArray(int id) {
        return IUnit.NewByField(getField(id), BoolArray::new);
    }

    public Int32Array getInt32Array(int id) {
        return IUnit.NewByField(getField(id), Int32Array::new);
    }

    public Int64Array getInt64Array(int id) {
        return IUnit.NewByField(getField(id), Int64Array::new);
    }

    public Float32Array getFloat32Array(int id) {
        return IUnit.NewByField(getField(id), Float32Array::new);
    }

    public Float64Array getFloat64Array(int id) {
        return IUnit.NewByField(getField(id), Float64Array::new);
    }

    public StrArray getStrArray(int id) {
        return IUnit.NewByField(getField(id), StrArray::new);
    }

    public BytesArray getBytesArray(int id) {
        return IUnit.NewByField(getField(id), BytesArray::new);
    }
}