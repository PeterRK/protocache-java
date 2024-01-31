// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.Arrays;

public final class Bytes extends IUnit.Complex {
    private final static byte[] empty = new byte[0];
    private DataView view = null;
    private byte[] value = null;

    static DataView extract(DataView data) {
        int off = data.offset;
        int mark = 0;
        for (int sft = 0; sft < 32; sft += 7) {
            byte b = data.data[off++];
            mark |= ((int) b & 0x7f) << sft;
            if ((b & 0x80) == 0) {
                if ((mark & 3) != 0) {
                    break;
                }
                int size = mark >>> 2;
                return new DataView(data.data, off, off + size);
            }
        }
        throw new IllegalArgumentException("illegal bytes unit");
    }

    public byte[] get() {
        if (value == null) {
            value = Arrays.copyOfRange(view.data, view.offset, view.limit);
        }
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = empty;
            view = null;
            return;
        }
        value = null;
        view = extract(data);
    }
}