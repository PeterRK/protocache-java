// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Dictionary<K extends IKey, V extends IUnit> extends IUnit.Complex {
    private final static byte[] empty = new byte[4];
    static {
        Data.putInt(empty, 0, 0);
    }

    private PerfectHash index;
    private int bodyOffset;
    private int keyWidth;
    private int valueWidth;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            index = new PerfectHash(empty);
            bodyOffset = 4;
            keyWidth = 0;
            valueWidth = 0;
            return;
        }
        index = new PerfectHash(data, offset);
        bodyOffset = offset + ((index.byteSize + 3) & 0xfffffffc);
        int mark = Data.getInt(data, offset);
        keyWidth = ((mark >>> 30) & 3) * 4;
        valueWidth = ((mark >>> 28) & 3) * 4;
        if (keyWidth == 0 || valueWidth == 0) {
            throw new IllegalArgumentException("illegal map");
        }
    }

    public int size() {
        return index.getSize();
    }


    private int keyFieldOffset(int idx) {
        return bodyOffset+ idx * (keyWidth + valueWidth);
    }

    private int valueFieldOffset(int idx) {
        return bodyOffset  + idx * (keyWidth + valueWidth) + keyWidth;
    }

    public K key(int idx, Supplier<K> supplier) {
        return IUnit.NewByField(index.data, keyFieldOffset(idx), supplier);
    }

    public V value(int idx, Supplier<V> supplier) {
        return IUnit.NewByField(index.data, valueFieldOffset(idx), supplier);
    }

    public V find(K key, Supplier<V> supplier) {
        int idx = index.locate(key.bytes());
        if (idx >= index.getSize() || !key.equalToField(index.data, keyFieldOffset(idx))) {
            return null;
        }
        return IUnit.NewByField(index.data, valueFieldOffset(idx), supplier);
    }

    public K fastGetKey(int idx, K unit) {
        unit.initByField(index.data, keyFieldOffset(idx));
        return unit;
    }

    public V fastGetValue(int idx, V unit) {
        unit.initByField(index.data, valueFieldOffset(idx));
        return unit;
    }

    public V fastFind(K key, V unit) {
        int idx = index.locate(key.bytes());
        if (idx >= index.getSize() || !key.equalToField(index.data, keyFieldOffset(idx))) {
            return null;
        }
        unit.initByField(index.data, valueFieldOffset(idx));
        return unit;
    }
}
