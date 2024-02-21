// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BoolArray extends IUnit.Complex {
    private Data.View view = null;
    private int cnt = 0;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            cnt = 0;
            view = null;
            return;
        }
        view = Bytes.extract(data, offset);
        cnt = view.size();
    }

    public int size() {
        return cnt;
    }

    public boolean get(int idx) {
        return view.data[view.offset+idx] != 0;
    }
}
