// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BytesArray extends Array<Bytes> {
    public byte[] get(int idx) {
        return get(idx, Bytes::new).get();
    }
}
