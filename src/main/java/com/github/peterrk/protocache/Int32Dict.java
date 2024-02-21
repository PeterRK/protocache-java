package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Int32Dict<V extends IUnit> extends Dict<V> {
    private byte[] tmp = null;

    @Override
    public void init(byte[] data, int offset) {
        super.init(data, offset);
        if (keyWidth != 4) {
            throw new IllegalArgumentException("illegal map");
        }
    }

    public int key(int idx) {
        return Data.getInt(index.data, keyFieldOffset(idx));
    }

    private int find(int key) {
        if (tmp == null) {
            tmp = new byte[4];
        }
        Data.putInt(tmp, 0, key);
        int idx = index.locate(tmp);
        if (idx >= index.getSize() || key != this.key(idx)) {
            return -1;
        }
        return valueFieldOffset(idx);
    }

    public V find(int key, Supplier<V> supplier) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        return IUnit.NewByField(index.data, valueOffset, supplier);
    }

    public V fastFind(int key, V unit) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        unit.initByField(index.data, valueOffset);
        return unit;
    }
}
