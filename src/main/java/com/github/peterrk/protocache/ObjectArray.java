package com.github.peterrk.protocache;

import java.util.function.Supplier;

public class ObjectArray<T extends Unit> extends ArrayType {
    @Override
    public void init(byte[] data, int offset) {
        init(data, offset, 0);
    }

    public T get(int idx, Supplier<T> supplier) {
        return Unit.NewByField(data, fieldOffset(idx), supplier);
    }

    public T get(int idx, T unit) {
        unit.initByField(data, fieldOffset(idx));
        return unit;
    }
}
