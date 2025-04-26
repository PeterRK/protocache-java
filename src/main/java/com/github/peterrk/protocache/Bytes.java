// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Bytes {
    static byte[] extractBytes(byte[] data, int offset) {
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

    static String extractString(byte[] data, int offset) {
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
}