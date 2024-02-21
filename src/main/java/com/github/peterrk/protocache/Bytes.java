// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.Arrays;

public final class Bytes extends IUnit.Complex {
    final static byte[] empty = new byte[0];
    private byte[] value = null;

    static byte[] extract(byte[] data, int offset) {
        int mark = 0;
        for (int sft = 0; sft < 32; sft += 7) {
            byte b = data[offset++];
            mark |= ((int) b & 0x7f) << sft;
            if ((b & 0x80) == 0) {
                if ((mark & 3) != 0) {
                    break;
                }
                int size = mark >>> 2;
                return Arrays.copyOfRange(data, offset, offset + size);
            }
        }
        throw new IllegalArgumentException("illegal bytes");
    }

    public byte[] get() {
        return value;
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = empty;
            return;
        }
        value = extract(data, offset);
    }
}