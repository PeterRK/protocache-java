// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class Int32Array extends ArrayType {
    @Override
    public void init(byte[] data, int offset) {
        init(data, offset, 1);
    }

    public int get(int idx) {
        return Data.getInt(data, fieldOffset(idx));
    }
}