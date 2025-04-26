// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class Int64Array extends ArrayType {
    @Override
    public void init(byte[] data, int offset) {
        init(data, offset, 2);
    }

    public long get(int idx) {
        return Data.getLong(data, fieldOffset(idx));
    }
}