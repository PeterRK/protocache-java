// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int64 extends IUnit.Simple implements IKey {
    private byte[] raw = null;
    private long value;

    public Int64() {
    }

    public Int64(long value) {
        this.value = value;
    }

    public long get() {
        return value;
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = 0;
        } else {
            value = Data.getLong(data, offset);
        }
        if (raw != null) {
            Data.putLong(raw, 0, value);
        }
    }

    @Override
    public byte[] bytes() {
        if (raw == null) {
            raw = new byte[8];
            Data.putLong(raw, 0, value);
        }
        return raw;
    }

    @Override
    public boolean equalToField(byte[] data, int offset) {
        return value == Data.getLong(data, offset);
    }
}
