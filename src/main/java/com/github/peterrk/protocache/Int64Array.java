// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int64Array extends IUnit.Complex {
    private int size;
    private DataView view;

    @Override
    public void init(DataView data) {
        if (data == null) {
            size = 0;
            return;
        }
        int mark = data.getInt();
        if ((mark & 3) != 2) {
            throw new IllegalArgumentException("illegal long array");
        }
        size = mark >>> 2;
        view = data;
    }

    public int size() {
        return size;
    }

    public long get(int idx) {
        return view.getLong(4 + idx * 8);
    }
}