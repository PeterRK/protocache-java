// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

public class BoolArray extends Unit {
    private byte[] data;
    private int bodyOffset = -1;
    private int cnt = 0;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            this.data = null;
            this.bodyOffset = -1;
            cnt = 0;
            return;
        }
        this.data = data;
        int mark = 0;
        for (int sft = 0; sft < 32; sft += 7) {
            byte b = data[offset++];
            mark |= ((int) b & 0x7f) << sft;
            if ((b & 0x80) == 0) {
                if ((mark & 3) != 0) {
                    break;
                }
                bodyOffset = offset;
                cnt = mark >>> 2;
                return;
            }
        }
        throw new IllegalArgumentException("illegal bool array");
    }

    public int size() {
        return cnt;
    }

    public boolean get(int idx) {
        return data[bodyOffset + idx] != 0;
    }
}
