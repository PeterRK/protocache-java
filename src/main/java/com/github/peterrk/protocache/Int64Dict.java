package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class Int64Dict<V extends IUnit> extends Dict<V> {
    private byte[] tmp = null;

    @Override
    public void init(byte[] data, int offset) {
        super.init(data, offset);
        if (keyWidth != 8) {
            throw new IllegalArgumentException("illegal map");
        }
    }

    public long key(int idx) {
        return Data.getLong(index.data, keyFieldOffset(idx));
    }

    private int find(long key) {
        if (tmp == null) {
            tmp = new byte[8];
        }
        Data.putLong(tmp, 0, key);
        int idx = index.locate(tmp);
        if (idx >= index.getSize() || key != this.key(idx)) {
            return -1;
        }
        return valueFieldOffset(idx);
    }

    public V find(long key, Supplier<V> supplier) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        return IUnit.NewByField(index.data, valueOffset, supplier);
    }

    public V fastFind(long key, V unit) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        unit.initByField(index.data, valueOffset);
        return unit;
    }
}
