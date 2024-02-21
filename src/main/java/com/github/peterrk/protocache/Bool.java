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
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = false;
            return;
        }
        value = data[offset] != 0;
    }
}
