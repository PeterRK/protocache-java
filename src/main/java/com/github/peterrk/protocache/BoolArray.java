// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BoolArray extends IUnit.Complex {
    private final static boolean[] empty = new boolean[0];
    private boolean[] value;

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = empty;
            return;
        }
        byte[] raw = Bytes.extractBytes(data);
        value = new boolean[raw.length];
        for (int i = 0; i < value.length; i++) {
            value[i] = raw[i] != 0;
        }
    }

    public int size() {
        return value.length;
    }

    public boolean get(int idx) {
        return value[idx];
    }
}
