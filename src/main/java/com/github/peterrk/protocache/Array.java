// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Array<T extends IUnit.Complex> extends IUnit.Complex {
    private int size;
    private int width;
    protected byte[] data;
    private int bodyOffset = -1;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            size = 0;
            width = 0;
            this.data = null;
            this.bodyOffset = -1;
            return;
        }
        int mark = Data.getInt(data, offset);
        if ((mark & 3) == 0) {
            throw new IllegalArgumentException("illegal array");
        }
        size = mark >>> 2;
        width = (mark & 3) * 4;
        this.data = data;
        this.bodyOffset = offset + 4;
    }

    public int size() {
        return size;
    }

    protected int fieldOffset(int idx) {
        return bodyOffset + idx * width;
    }

    public T get(int idx, Supplier<T> supplier) {
        return IUnit.NewByField(data, fieldOffset(idx), supplier);
    }

    public T fastGet(int idx, T unit) {
        unit.initByField(data, fieldOffset(idx));
        return unit;
    }
}
