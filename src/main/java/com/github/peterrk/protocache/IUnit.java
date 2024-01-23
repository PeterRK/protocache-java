// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

interface IUnit {
    static <T extends IUnit> T NewByField(DataView field, Supplier<T> supplier) {
        T unit = supplier.get();
        unit.initByField(field);
        return unit;
    }

    void init(DataView data);

    void initByField(DataView data);

    abstract class Simple implements IUnit {
        public void initByField(DataView field) {
            init(field);
        }
    }

    abstract class Complex implements IUnit {
        public void initByField(DataView field) {
            if (field == null) {
                init(null);
                return;
            }
            int mark = field.getInt();
            if ((mark & 3) == 3) {
                field = new DataView(field.data, field.offset + (mark & 0xfffffffc));
            }
            init(field);
        }
    }
}
