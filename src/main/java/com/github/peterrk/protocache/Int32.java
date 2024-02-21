// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int32 extends IUnit.Simple implements IKey {
    private byte[] raw = null;
    private int value;

    public Int32() {
    }

    public Int32(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = 0;
        } else {
            value = Data.getInt(data, offset);
        }
        if (raw != null) {
            Data.putInt(raw, 0, value);
        }
    }

    @Override
    public byte[] bytes() {
        if (raw == null) {
            raw = new byte[4];
            Data.putInt(raw, 0, value);
        }
        return raw;
    }

    @Override
    public boolean equalToField(byte[] data, int offset) {
        return value == Data.getInt(data, offset);
    }
}