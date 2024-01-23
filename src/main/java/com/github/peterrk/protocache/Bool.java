// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class Bool extends IUnit.Simple {
    private boolean value;

    public boolean get() {
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = false;
            return;
        }
        value = data.data[data.offset] != 0;
    }
}
