package com.github.peterrk.protocache;

import java.util.function.Supplier;

public abstract class Int64Dict extends DictType {
    private byte[] tmp = null;

    protected void init(byte[] data, int offset, int word) {
        init(data, offset, 2, word);
    }

    public long key(int idx) {
        return Data.getLong(index.data, keyFieldOffset(idx));
    }

    public int find(long key) {
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

    public static class BoolValue extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public boolean value(int idx) {
            return index.data[valueFieldOffset(idx)] != 0;
        }
    }

    public static class Int32Value extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public int value(int idx) {
            return Data.getInt(index.data, valueFieldOffset(idx));
        }
    }

    public static class Int64Value extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 2);
        }
        public long value(int idx) {
            return Data.getLong(index.data, valueFieldOffset(idx));
        }
    }

    public static class Float2Value extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public float value(int idx) {
            return Data.getFloat(index.data, valueFieldOffset(idx));
        }
    }

    public static class float64Value extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 2);
        }
        public double value(int idx) {
            return Data.getDouble(index.data, valueFieldOffset(idx));
        }
    }

    public static class StringValue extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 0);
        }

        public String value(int idx) {
            return Bytes.extractString(index.data, Unit.jump(index.data, valueFieldOffset(idx)));
        }
    }

    public static class BytesValue extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 0);
        }

        public byte[] value(int idx) {
            return Bytes.extractBytes(index.data, Unit.jump(index.data, valueFieldOffset(idx)));
        }
    }

    public static class ObjectValue<V extends Unit> extends Int64Dict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 0);
        }

        public V value(int idx, Supplier<V> supplier) {
            return Unit.NewByField(index.data, valueFieldOffset(idx), supplier);
        }

        public V value(int idx, V unit) {
            unit.initByField(index.data, valueFieldOffset(idx));
            return unit;
        }
    }
}
