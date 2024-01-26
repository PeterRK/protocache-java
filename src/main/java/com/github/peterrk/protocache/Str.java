// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Str extends IUnit.Complex implements IKey {
    private String value;

    public Str() {
    }

    public Str(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public void init(DataView data) {
        if (data == null) {
            value = "";
            return;
        }
        value = new String(Bytes.extractBytes(data), StandardCharsets.UTF_8);
    }

    @Override
    public byte[] toBytes() {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean equalToField(DataView field) {
        int mark = field.getInt();
        if ((mark & 3) == 3) {
            field = new DataView(field.data, field.offset + (mark & 0xfffffffc));
        }
        return Arrays.equals(toBytes(), Bytes.extractBytes(field));
    }
}
