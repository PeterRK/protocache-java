// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Str extends IUnit.Complex implements IKey {
    private final static String empty = "";
    private DataView view = null;
    private String value = null;

    public Str() {}

    public Str(String value) {
        this.value = value;
        this.view = null;
    }

    public String get() {
        if (value == null) {
            value = new String(view.data, view.offset, view.size(), StandardCharsets.UTF_8);
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
        view = Bytes.extract(data);
    }

    @Override
    public byte[] toBytes() {
        if (view != null) {
            return Arrays.copyOfRange(view.data, view.offset, view.limit);
        }
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean equalToField(DataView field) {
        int mark = field.getInt();
        if ((mark & 3) == 3) {
            field = new DataView(field.data, field.offset + (mark & 0xfffffffc));
        }
        DataView unit = Bytes.extract(field);
        return get().equals(new String(unit.data, unit.offset, unit.size(), StandardCharsets.UTF_8));
    }
}
