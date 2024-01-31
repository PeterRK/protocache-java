// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BoolArray extends IUnit.Complex {
    private DataView view = null;
    private int cnt = 0;

    @Override
    public void init(DataView data) {
        if (data == null) {
            cnt = 0;
            view = null;
            return;
        }
        view = Bytes.extract(data);
        cnt = view.size();
    }

    public int size() {
        return cnt;
    }

    public boolean get(int idx) {
        return view.getByte(idx) != 0;
    }
}
