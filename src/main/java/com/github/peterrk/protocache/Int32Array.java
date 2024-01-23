// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int32Array extends IUnit.Complex {
    private int size;
    private DataView view;

    @Override
    public void init(DataView data) {
        if (data == null) {
            size = 0;
            return;
        }
        int mark = data.getInt();
        if ((mark & 3) != 1) {
            throw new IllegalArgumentException("illegal int array");
        }
        size = mark >>> 2;
        view = data;
    }

    public int size() {
        return size;
    }

    public int get(int idx) {
        return view.getInt(4 + idx * 4);
    }
}