// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

abstract class Unit {
    static <T extends Unit> T NewByField(byte[] data, int offset, Supplier<T> supplier) {
        T unit = supplier.get();
        unit.initByField(data, offset);
        return unit;
    }

    static int jump(byte[] data, int offset) {
        int mark = Data.getInt(data, offset);
        if ((mark & 3) == 3) {
            offset += (mark & 0xfffffffc);
        }
        return offset;
    }

    abstract void init(byte[] data, int offset);

    void initByField(byte[] data, int offset) {
        if (offset < 0) {
            init(null, offset);
            return;
        }
        init(data, jump(data, offset));
    }
}
