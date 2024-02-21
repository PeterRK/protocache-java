// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Float32 extends IUnit.Simple {
    private float value;

    public float get() {
        return value;
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = 0;
            return;
        }
        value = Data.getFloat(data, offset);
    }
}
