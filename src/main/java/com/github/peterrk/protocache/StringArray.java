// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class StringArray extends ArrayType {
    @Override
    public void init(byte[] data, int offset) {
        init(data, offset, 0);
    }

    public String get(int idx) {
        return Bytes.extractString(data, IUnit.jump(data, fieldOffset(idx)));
    }
}
