package com.github.peterrk.protocache;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public abstract class StringDict extends DictType {
    protected void init(byte[] data, int offset, int word) {
        init(data, offset, 0, word);
    }

    public String key(int idx) {
        return Bytes.extractString(index.data, Unit.jump(index.data, keyFieldOffset(idx)));
    }

    public int find(String key) {
        int idx = index.locate(key.getBytes(StandardCharsets.UTF_8));
        if (idx >= index.getSize() || !key.equals(this.key(idx))) {
            return -1;
        }
        return valueFieldOffset(idx);
    }

    public static class BoolValue extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public boolean value(int idx) {
            return index.data[valueFieldOffset(idx)] != 0;
        }
    }

    public static class Int32Value extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public int value(int idx) {
            return Data.getInt(index.data, valueFieldOffset(idx));
        }
    }

    public static class Int64Value extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 2);
        }
        public long value(int idx) {
            return Data.getLong(index.data, valueFieldOffset(idx));
        }
    }

    public static class Float2Value extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 1);
        }
        public float value(int idx) {
            return Data.getFloat(index.data, valueFieldOffset(idx));
        }
    }

    public static class float64Value extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 2);
        }
        public double value(int idx) {
            return Data.getDouble(index.data, valueFieldOffset(idx));
        }
    }

    public static class StringValue extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 0);
        }

        public String value(int idx) {
            return Bytes.extractString(index.data, Unit.jump(index.data, valueFieldOffset(idx)));
        }
    }

    public static class BytesValue extends StringDict {
        @Override
        public void init(byte[] data, int offset) {
            init(data, offset, 0);
        }

        public byte[] value(int idx) {
            return Bytes.extractBytes(index.data, Unit.jump(index.data, valueFieldOffset(idx)));
        }
    }

    public static class ObjectValue<V extends Unit> extends StringDict {
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
