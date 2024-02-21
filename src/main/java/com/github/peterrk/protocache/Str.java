// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;

public final class Str extends IUnit.Complex implements IKey {
    final static String empty = "";
    private byte[] raw = null;
    private String value = null;

    public Str() {
    }

    public Str(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            value = empty;
            raw = Bytes.empty;
            return;
        }
        Data.View view = Bytes.extract(data, offset);
        value = new String(view.data, view.offset, view.size(), StandardCharsets.UTF_8);
        raw = null;
    }

    @Override
    public byte[] bytes() {
        if (raw == null) {
            raw = value.getBytes(StandardCharsets.UTF_8);
        }
        return raw;
    }

    @Override
    public boolean equalToField(byte[] data, int offset) {
        int mark = Data.getInt(data, offset);
        if ((mark & 3) == 3) {
            offset += (mark & 0xfffffffc);
        }
        Data.View unit = Bytes.extract(data, offset);
        return get().equals(new String(unit.data, unit.offset, unit.size(), StandardCharsets.UTF_8));
    }
}
