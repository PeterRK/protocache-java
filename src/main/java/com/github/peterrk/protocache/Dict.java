package com.github.peterrk.protocache;

import java.util.function.Supplier;

abstract class Dict<V extends IUnit> extends IUnit.Complex {
    private final static PerfectHash emptyIndex;

    static {
        byte[] empty = new byte[4];
        Data.putInt(empty, 0, 0);
        emptyIndex = new PerfectHash(empty);
    }

    protected PerfectHash index;
    protected int bodyOffset;
    protected int keyWidth;
    protected int valueWidth;

    @Override
    public void init(byte[] data, int offset) {
        if (offset < 0) {
            index = emptyIndex;
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

    protected int keyFieldOffset(int idx) {
        return bodyOffset + idx * (keyWidth + valueWidth);
    }

    protected int valueFieldOffset(int idx) {
        return bodyOffset + idx * (keyWidth + valueWidth) + 4;
    }

    public V value(int idx, Supplier<V> supplier) {
        return IUnit.NewByField(index.data, valueFieldOffset(idx), supplier);
    }

    public V fastGetValue(int idx, V unit) {
        unit.initByField(index.data, valueFieldOffset(idx));
        return unit;
    }
}
