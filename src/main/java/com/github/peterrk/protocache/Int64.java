// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Int64 extends IUnit.Simple implements IKey {
    private DataView view = null;
    private long value;

    public Int64() {
    }

    public Int64(long value) {
        this.value = value;
        this.view = null;
    }

    public long get() {
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = 0;
            view = null;
            return;
        }
        value = data.getLong();
        view = data;
    }

    @Override
    public DataView view() {
        if (view == null) {
            view = new DataView(new byte[8]);
            view.putLong(value);
        }
        return view;
    }

    @Override
    public boolean equalToField(DataView field) {
        return value == field.getLong();
    }
}
