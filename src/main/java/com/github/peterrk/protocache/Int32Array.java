// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int32Array extends IUnit.Complex {
    private int size;
    private byte[] data;
    private int bodyOffset = -1;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            size = 0;
            this.data = null;
            this.bodyOffset = -1;
            return;
        }
        int mark = Data.getInt(data, offset);
        if ((mark & 3) != 1) {
            throw new IllegalArgumentException("illegal int array");
        }
        size = mark >>> 2;
        this.data = data;
        this.bodyOffset = offset + 4;
    }

    public int size() {
        return size;
    }

    public int get(int idx) {
        int fieldOffset = bodyOffset + idx * 4;
        return Data.getInt(data, fieldOffset);
    }
}