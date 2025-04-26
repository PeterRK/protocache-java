// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

interface IUnit {
    void init(byte[] data, int offset);

    static int jump(byte[] data, int offset) {
        int mark = Data.getInt(data, offset);
        if ((mark & 3) == 3) {
            offset += (mark & 0xfffffffc);
        }
        return offset;
    }

    static <T extends IUnit> T initByField(byte[] data, int offset, T unit) {
        unit.init(data, jump(data, offset));
        return unit;
    }
}
