// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Int32 extends IUnit.Simple implements IMapKey {
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
    public void init(DataView data) {
        if (data == null) {
            value = 0;
            return;
        }
        value = data.getInt();
    }

    @Override
    public byte[] toBytes() {
        byte[] out = new byte[4];
        ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN).putInt(value);
        return out;
    }

    @Override
    public boolean equalToField(DataView field) {
        return value == field.getInt();
    }
}