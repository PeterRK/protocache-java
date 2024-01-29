// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BytesArray extends Array<Bytes> {
    private Bytes tmp = null;

    public byte[] get(int idx) {
        if (tmp == null) {
            tmp = new Bytes();
        }
        return fastGet(idx, tmp).get();
    }
}
