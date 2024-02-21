// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;

public final class Str extends IUnit.Complex {
    final static String empty = "";
    private String value = null;

    static String extract(byte[] data, int offset) {
        int mark = 0;
        for (int sft = 0; sft < 32; sft += 7) {
            byte b = data[offset++];
            mark |= ((int) b & 0x7f) << sft;
            if ((b & 0x80) == 0) {
                if ((mark & 3) != 0) {
                    break;
                }
                int size = mark >>> 2;
                return new String(data, offset, size, StandardCharsets.UTF_8);
            }
        }
        throw new IllegalArgumentException("illegal string");
    }

    public String get() {
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
