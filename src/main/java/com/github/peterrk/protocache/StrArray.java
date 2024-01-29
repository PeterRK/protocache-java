// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public final class StrArray extends Array<Str> {
    private Str tmp = null;

    public String get(int idx) {
        if (tmp == null) {
            tmp = new Str();
        }
        return fastGet(idx, tmp).get();
    }
}
