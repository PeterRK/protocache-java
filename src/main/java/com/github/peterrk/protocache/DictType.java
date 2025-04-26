package com.github.peterrk.protocache;

abstract class DictType implements IUnit {
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

    protected void init(byte[] data, int offset, int keyWord, int valueWord) {
        if (offset < 0) {
            index = emptyIndex;
            bodyOffset = 4;
            keyWidth = keyWord * 4;
            valueWidth = valueWord * 4;
            return;
        }
        index = new PerfectHash(data, offset);
        bodyOffset = offset + ((index.byteSize + 3) & 0xfffffffc);
        int mark = Data.getInt(data, offset);
        keyWidth = ((mark >>> 30) & 3) * 4;
        valueWidth = ((mark >>> 28) & 3) * 4;
        if ((keyWord != 0 && keyWidth != keyWord*4) || keyWidth == 0
                || (valueWord != 0 && valueWidth != valueWord*4) || valueWidth == 0) {
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
        return keyFieldOffset(idx) + keyWidth;
    }
}
