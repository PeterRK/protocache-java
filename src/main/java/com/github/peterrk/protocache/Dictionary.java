// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Dictionary<K extends IMapKey, V extends IUnit> extends IUnit.Complex {
    private final static DataView empty = new DataView(new byte[4]);

    static {
        empty.putInt(0);
    }

    private PerfectHash index;
    private DataView body;
    private int keyWidth;
    private int valueWidth;

    @Override
    public void init(DataView data) {
        if (data == null) {
            index = new PerfectHash(empty);
            body = null;
            keyWidth = 0;
            valueWidth = 0;
            return;
        }
        index = new PerfectHash(data);
        body = new DataView(data.data, data.offset + ((index.getByteSize() + 3) & 0xfffffffc));
        int mark = data.getInt();
        keyWidth = ((mark >>> 30) & 3) * 4;
        valueWidth = ((mark >>> 28) & 3) * 4;
        if (keyWidth == 0 || valueWidth == 0) {
            throw new IllegalArgumentException("illegal map");
        }
    }

    public int size() {
        return index.getSize();
    }


    private DataView keyField(int idx) {
        return new DataView(body.data, body.offset + idx * (keyWidth + valueWidth));
    }

    private DataView valueField(int idx) {
        return new DataView(body.data, body.offset + idx * (keyWidth + valueWidth) + keyWidth);
    }

    public K key(int idx, Supplier<K> supplier) {
        return IUnit.NewByField(keyField(idx), supplier);
    }

    public V value(int idx, Supplier<V> supplier) {
        return IUnit.NewByField(valueField(idx), supplier);
    }

    public V find(K key, Supplier<V> supplier) {
        int idx = index.locate(key.toBytes());
        if (idx >= index.getSize() || !key.equalToField(keyField(idx))) {
            return null;
        }
        return IUnit.NewByField(valueField(idx), supplier);
    }
}
