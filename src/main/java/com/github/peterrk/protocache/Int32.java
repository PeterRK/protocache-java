// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int32 extends IUnit.Simple implements IKey {
    private DataView view = null;
    private int value;

    public Int32() {
    }

    public Int32(int value) {
        this.value = value;
        this.view = null;
    }

    public int get() {
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = 0;
            view = null;
            return;
        }
        value = data.getInt();
        view = data;
    }

    @Override
    public DataView view() {
        if (view == null) {
            view = new DataView(new byte[4]);
            view.putInt(value);
        }
        return view;
    }

    @Override
    public boolean equalToField(DataView field) {
        return value == field.getInt();
    }
}