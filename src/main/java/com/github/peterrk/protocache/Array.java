// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Array<T extends IUnit.Complex> extends IUnit.Complex {
    private int size;
    private int width;
    private DataView view;

    @Override
    public void init(DataView data) {
        if (data == null) {
            size = 0;
            width = 0;
            return;
        }
        int mark = data.getInt();
        if ((mark & 3) == 0) {
            throw new IllegalArgumentException("illegal array");
        }
        size = mark >>> 2;
        width = (mark & 3) * 4;
        view = data;
    }

    public int size() {
        return size;
    }

    public T get(int idx, Supplier<T> supplier) {
        DataView field = new DataView(view.data, view.offset + 4 + idx * width);
        return IUnit.NewByField(field, supplier);
    }
}
