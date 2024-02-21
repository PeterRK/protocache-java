package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class StrDict<V extends IUnit> extends Dict<V> {

    public String key(int idx) {
        return Str.extract(index.data, Complex.jump(index.data, keyFieldOffset(idx)));
    }

    private int find(String key) {
        int idx = index.locate(key.getBytes(StandardCharsets.UTF_8));
        if (idx >= index.getSize() || !key.equals(this.key(idx))) {
            return -1;
        }
        return valueFieldOffset(idx);
    }

    public V find(String key, Supplier<V> supplier) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        return IUnit.NewByField(index.data, valueOffset, supplier);
    }

    public V fastFind(String key, V unit) {
        int valueOffset = find(key);
        if (valueOffset < 0) {
            return null;
        }
        unit.initByField(index.data, valueOffset);
        return unit;
    }
}
