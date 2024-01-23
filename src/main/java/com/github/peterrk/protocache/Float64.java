// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Float64 extends IUnit.Simple {
    private double value;

    public double get() {
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = 0;
            return;
        }
        value = data.getDouble();
    }
}
