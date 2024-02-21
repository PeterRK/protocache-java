// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProtoCache {
    public static byte[] serialize(Message message) {
        Descriptors.Descriptor descriptor = message.getDescriptorForType();

        ArrayList<Descriptors.FieldDescriptor> temp = new ArrayList<>(descriptor.getFields());
        if (temp.isEmpty()) {
            throw new IllegalArgumentException(String.format("no fields in %s", descriptor.getFullName()));
        }

        temp.sort(Comparator.comparingInt(Descriptors.FieldDescriptor::getNumber));
        int maxId = temp.get(temp.size() - 1).getNumber();
        if (maxId > (12 + 25 * 255)) {
            throw new IllegalArgumentException(String.format("too many fields in %s", descriptor.getFullName()));
        }
        if ((maxId - temp.size()) > 6 && maxId > temp.size() * 2) {
            throw new IllegalArgumentException(String.format("message %s is too sparse", descriptor.getFullName()));
        }
        Descriptors.FieldDescriptor[] fields = new Descriptors.FieldDescriptor[maxId];
        for (int i = 0; i < temp.size(); i++) {
            Descriptors.FieldDescriptor one = temp.get(i);
            fields[one.getNumber() - 1] = one;
        }

        ArrayList<byte[]> parts = new ArrayList<>(fields.length);
        for (Descriptors.FieldDescriptor field : fields) {
            if (field == null) {
                parts.add(null);
                continue;
            }
            String name = field.getFullName();
            if (field.isRepeated()) {
                if (message.getRepeatedFieldCount(field) == 0) {
                    parts.add(null);
                    continue;
                }
                if (field.isMapField()) {
                    parts.add(serializeMap(message, field));
                } else {
                    parts.add(serializeList(message, field));
                }
            } else {
                if (!message.hasField(field)) {
                    parts.add(null);
                    continue;
                }
                parts.add(serializeField(field, message.getField(field)));
            }
        }

        while (!parts.isEmpty() && parts.get(parts.size() - 1) == null) {
            parts.remove(parts.size() - 1);
        }
        if (parts.isEmpty()) {
            byte[] out = new byte[4];
            ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN).putInt(0);
            return out;
        }
        if (fields.length == 1 && fields[0].getName().equals("_")) {
            return parts.get(0);    // trim message wrapper
        }

        int section = (parts.size() + 12) / 25;
        if (section > 0xff) {
            throw new RuntimeException("too many fields");
        }

        int size = 1 + section * 2;
        int cnt = 0;
        int head = section;
        for (int i = 0; i < Math.min(12, parts.size()); i++) {
            byte[] one = parts.get(i);
            if (one == null) {
                continue;
            }
            if (one.length / 4 < 4) {
                head |= (one.length / 4) << (8 + i * 2);
                size += one.length / 4;
                cnt += one.length / 4;
            } else {
                head |= 1 << (8 + i * 2);
                size += 1 + one.length / 4;
                cnt += 1;
            }
        }
        for (int i = 12; i < parts.size(); i++) {
            byte[] one = parts.get(i);
            if (one == null) {
                continue;
            }
            if (one.length / 4 < 4) {
                size += one.length / 4;
            } else {
                size += 1 + one.length / 4;
            }
            if (size >= (1 << 30)) {
                throw new IllegalArgumentException("message size overflow");
            }
        }
        byte[] out = new byte[size * 4];
        Data.putInt(out, 0, head);

        int off = 4;
        for (int i = 12; i < parts.size(); ) {
            int next = Math.min(i + 25, parts.size());
            if (cnt >= (1 << 14)) {
                throw new IllegalArgumentException("message parts overflow");
            }
            long mark = ((long) cnt) << 50;
            for (int j = 0; i < next; j += 2) {
                byte[] one = parts.get(i++);
                if (one == null) {
                    continue;
                }
                if (one.length / 4 < 4) {
                    mark |= ((long) one.length / 4) << j;
                    cnt += one.length / 4;
                } else {
                    mark |= 1L << j;
                    cnt += 1;
                }
            }
            Data.putLong(out, off, mark);
            off += 8;
        }

        for (byte[] one : parts) {
            if (one == null) {
                continue;
            }
            if (one.length / 4 < 4) {
                System.arraycopy(one, 0, out, off, one.length);
                off += one.length;
            } else {
                off += 4;
            }
        }
        int tail = off;
        off = 4 + section * 8;
        for (byte[] one : parts) {
            if (one == null) {
                continue;
            }
            if (one.length / 4 < 4) {
                off += one.length;
            } else {
                Data.putInt(out, off, (tail - off) | 3);
                System.arraycopy(one, 0, out, tail, one.length);
                tail += one.length;
                off += 4;
            }
        }
        if (tail != out.length) {
            throw new RuntimeException("size mismatch");
        }
        return out;
    }

    private static <T> byte[] serializeScalar(T value, int width, ByteBufferFiller<T> filler) {
        byte[] out = new byte[width * 4];
        ByteBuffer buffer = ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN);
        filler.fill(buffer, value);
        return out;
    }

    private static byte[] serializeField(Descriptors.FieldDescriptor field, Object value) {
        switch (field.getType()) {
            case MESSAGE:
                return serialize((Message) value);
            case BYTES:
                return serialize((ByteString) value);
            case STRING:
                return serialize((String) value);
            case DOUBLE:
                return serializeScalar(value, 2, (buffer, v) -> {
                    buffer.putDouble((Double) v);
                });
            case FLOAT:
                return serializeScalar(value, 1, (buffer, v) -> {
                    buffer.putFloat((float) v);
                });
            case FIXED64:
            case UINT64:
            case SFIXED64:
            case SINT64:
            case INT64:
                return serializeScalar(value, 2, (buffer, v) -> {
                    buffer.putLong((Long) v);
                });
            case FIXED32:
            case UINT32:
            case SFIXED32:
            case SINT32:
            case INT32:
                return serializeScalar(value, 1, (buffer, v) -> {
                    buffer.putInt((Integer) v);
                });
            case BOOL:
                return serializeScalar(value, 1, (buffer, v) -> {
                    if ((Boolean) v) {
                        buffer.putInt(1);
                    } else {
                        buffer.putInt(0);
                    }
                });
            case ENUM:
                return serializeScalar(value, 1, (buffer, v) -> {
                    buffer.putInt(((Descriptors.EnumValueDescriptor) v).getNumber());
                });
        }
        throw new IllegalArgumentException(String.format("unsupported field: %s", field.getFullName()));
    }

    private static byte[] serialize(String value) {
        return serialize(value.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] serialize(ByteString value) {
        return serialize(value.toByteArray());
    }

    private static byte[] serialize(byte[] value) {
        if (value.length >= (1 << 28)) {
            throw new IllegalArgumentException("too long string");
        }

        byte[] tmp = new byte[5];
        int mark = value.length << 2;
        int w = 0;
        while ((mark & ~0x7f) != 0) {
            tmp[w++] = (byte) (0x80 | (mark & 0x7f));
            mark >>>= 7;
        }
        tmp[w++] = (byte) mark;

        byte[] out = new byte[((w + value.length) + 3) & 0xfffffffc];
        System.arraycopy(tmp, 0, out, 0, w);
        System.arraycopy(value, 0, out, w, value.length);
        return out;
    }

    private static byte[] serializeList(Message message, Descriptors.FieldDescriptor field) {
        switch (field.getType()) {
            case MESSAGE:
                return serializeObjectList(message, field, (object) -> serialize((Message) object));
            case BYTES:
                return serializeObjectList(message, field, (object) -> serialize((ByteString) object));
            case STRING:
                return serializeObjectList(message, field, (object) -> serialize((String) object));
            case DOUBLE:
                return serializeScalarList(message, field, 2, (buffer, value) -> {
                    buffer.putDouble((Double) value);
                });
            case FLOAT:
                return serializeScalarList(message, field, 1, (buffer, value) -> {
                    buffer.putFloat((Float) value);
                });
            case FIXED64:
            case UINT64:
            case SFIXED64:
            case SINT64:
            case INT64:
                return serializeScalarList(message, field, 2, (buffer, value) -> {
                    buffer.putLong((Long) value);
                });
            case FIXED32:
            case UINT32:
            case SFIXED32:
            case SINT32:
            case INT32:
                return serializeScalarList(message, field, 1, (buffer, value) -> {
                    buffer.putInt((Integer) value);
                });
            case BOOL:
                int cnt = message.getRepeatedFieldCount(field);
                byte[] tmp = new byte[cnt];
                for (int i = 0; i < cnt; i++) {
                    if ((Boolean) message.getRepeatedField(field, i)) {
                        tmp[i] = 1;
                    } else {
                        tmp[i] = 0;
                    }
                }
                return serialize(tmp);
            case ENUM:
                return serializeScalarList(message, field, 1, (buffer, value) -> {
                    buffer.putInt(((Descriptors.EnumValueDescriptor) value).getNumber());
                });
            default:
                throw new IllegalArgumentException(String.format("unsupported field: %s", field.getFullName()));
        }
    }

    private static BestArray detectBestArray(byte[][] parts) {
        long[] sizes = new long[]{0, 0, 0};
        for (byte[] one : parts) {
            sizes[0] += 1;
            sizes[1] += 2;
            sizes[2] += 3;
            if (one.length / 4 <= 1) {
                continue;
            }
            sizes[0] += one.length / 4;
            if (one.length / 4 <= 2) {
                continue;
            }
            sizes[1] += one.length / 4;
            if (one.length / 4 <= 3) {
                continue;
            }
            sizes[2] += one.length / 4;
        }
        int mode = 0;
        for (int i = 1; i < 3; i++) {
            if (sizes[i] < sizes[mode]) {
                mode = i;
            }
        }
        return new BestArray(sizes[mode], mode + 1);
    }

    private static byte[] serializeObjectList(Message message, Descriptors.FieldDescriptor field, Serializer<Object> serializer) {
        int cnt = message.getRepeatedFieldCount(field);
        byte[][] parts = new byte[cnt][];
        for (int i = 0; i < cnt; i++) {
            parts[i] = serializer.serialize(message.getRepeatedField(field, i));
        }
        BestArray ret = detectBestArray(parts);
        ret.size += 1;
        if (ret.size >= (1 << 30)) {
            throw new IllegalArgumentException("array size overflow");
        }
        byte[] out = new byte[(int) ret.size * 4];
        Data.putInt(out, 0, (parts.length << 2) | ret.width);
        ret.width *= 4;

        int off = 4;
        for (byte[] one : parts) {
            if (one.length <= ret.width) {
                System.arraycopy(one, 0, out, off, one.length);
            }
            off += ret.width;
        }
        int tail = off;
        off = 4;
        for (byte[] one : parts) {
            if (one.length > ret.width) {
                Data.putInt(out, off, (tail - off) | 3);
                System.arraycopy(one, 0, out, tail, one.length);
                tail += one.length;
            }
            off += ret.width;
        }
        if (tail != out.length) {
            throw new RuntimeException("size mismatch");
        }
        return out;
    }

    private static byte[] serializeScalarList(Message message, Descriptors.FieldDescriptor field,
                                              int width, ByteBufferFiller<Object> filler) {
        int size = message.getRepeatedFieldCount(field);
        if (size >= (1 << 28)) {
            throw new IllegalArgumentException("array size overflow");
        }
        int mark = (size << 2) | width;
        byte[] out = new byte[4 + size * width * 4];
        ByteBuffer buffer = ByteBuffer.wrap(out).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(mark);
        for (int i = 0; i < size; i++) {
            filler.fill(buffer, message.getRepeatedField(field, i));
        }
        return out;
    }

    private static byte[] serializeMap(Message message, Descriptors.FieldDescriptor field) {
        List<Descriptors.FieldDescriptor> subfields = field.getMessageType().getFields();
        Descriptors.FieldDescriptor keyField = subfields.get(0);
        Descriptors.FieldDescriptor valueField = subfields.get(1);

        int total = message.getRepeatedFieldCount(field);
        ArrayList<byte[]> keys = new ArrayList<>(total);
        ArrayList<byte[]> values = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Message m = (Message) message.getRepeatedField(field, i);
            keys.add(serializeField(keyField, m.getField(keyField)));
            values.add(serializeField(valueField, m.getField(valueField)));
        }

        PerfectHash.KeySource reader;
        switch (keyField.getType()) {
            case STRING:
                reader = new StrReader(keys);
                break;
            case FIXED64:
            case UINT64:
            case SFIXED64:
            case SINT64:
            case INT64:
            case FIXED32:
            case UINT32:
            case SFIXED32:
            case SINT32:
            case INT32:
            case BOOL:
                reader = new SimpleReader(keys);
                break;
            default:
                throw new IllegalArgumentException(String.format("unsupported map key type: %s", keyField.getType().toString()));
        }

        PerfectHash index = PerfectHash.build(reader);
        byte[][] outKeys = new byte[total][];
        byte[][] outValues = new byte[total][];
        reader.reset();
        for (int i = 0; i < total; i++) {
            int pos = index.locate(reader.next());
            outKeys[pos] = keys.get(i);
            outValues[pos] = values.get(i);
        }

        int indexSize = (index.byteSize + 3) / 4;
        BestArray k = detectBestArray(outKeys);
        BestArray v = detectBestArray(outValues);

        String name = field.getFullName();
        long size = indexSize;
        size += k.size + v.size;
        if (size >= (1 << 30)) {
            throw new IllegalArgumentException("map size overflow");
        }
        byte[] out = new byte[(int) size * 4];
        System.arraycopy(index.data, index.offset, out, 0, index.byteSize);
        int mark = Data.getInt(out, 0);
        mark |= (k.width << 30) | (v.width << 28);
        Data.putInt(out, 0, mark);

        k.width *= 4;
        v.width *= 4;

        int off = indexSize * 4;
        for (int i = 0; i < total; i++) {
            byte[] key = outKeys[i];
            byte[] value = outValues[i];
            if (key.length <= k.width) {
                System.arraycopy(key, 0, out, off, key.length);
            }
            off += k.width;
            if (value.length <= v.width) {
                System.arraycopy(value, 0, out, off, value.length);
            }
            off += v.width;
        }
        int tail = off;
        off = indexSize * 4;
        for (int i = 0; i < total; i++) {
            byte[] key = outKeys[i];
            byte[] value = outValues[i];
            if (key.length > k.width) {
                Data.putInt(out, off,(tail - off) | 3);
                System.arraycopy(key, 0, out, tail, key.length);
                tail += key.length;
            }
            off += k.width;
            if (value.length > v.width) {
                Data.putInt(out, off,(tail - off) | 3);
                System.arraycopy(value, 0, out, tail, value.length);
                tail += value.length;
            }
            off += v.width;
        }
        if (tail != out.length) {
            throw new RuntimeException("size mismatch");
        }
        return out;
    }

    private interface ByteBufferFiller<T> {
        void fill(ByteBuffer buffer, T object);
    }

    private interface Serializer<T> {
        byte[] serialize(T object);
    }


    private static final class BestArray {
        public long size;
        public int width;

        public BestArray(long size, int width) {
            this.size = size;
            this.width = width;
        }
    }

    private static class SimpleReader implements PerfectHash.KeySource {
        protected final List<byte[]> data;
        protected Iterator<byte[]> iterator;

        public SimpleReader(List<byte[]> data) {
            this.data = data;
        }

        @Override
        public void reset() {
            iterator = data.iterator();
        }

        @Override
        public int total() {
            return data.size();
        }

        @Override
        public byte[] next() {
            return iterator.next();
        }
    }

    private static class StrReader extends SimpleReader {
        public StrReader(List<byte[]> data) {
            super(data);
        }

        @Override
        public byte[] next() {
            Data.View view = Bytes.extract(iterator.next(), 0);
            return Arrays.copyOfRange(view.data, view.offset, view.limit);
        }
    }
}
