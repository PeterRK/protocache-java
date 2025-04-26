package com.github.peterrk.protocache;

public class ObjectArray<T extends IUnit> extends ArrayType {
    @Override
    public void init(byte[] data, int offset) {
        init(data, offset, 0);
    }

    public T get(int idx, T unit) {
        return IUnit.initByField(data, fieldOffset(idx), unit);
    }
}
