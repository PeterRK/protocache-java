// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Int64 extends IUnit.Simple implements IKey {
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
    public void init(DataView data) {
        if (data == null) {
            value = 0;
            return;
        }
        value = data.getLong();
    }

    @Override
    public byte[] toBytes() {
        byte[] out = new byte[8];
        ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN).putLong(value);
        return out;
    }

    @Override
    public boolean equalToField(DataView field) {
        return value == field.getLong();
    }
}
