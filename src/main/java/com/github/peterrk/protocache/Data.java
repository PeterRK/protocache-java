// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class Data {

    private static final sun.misc.Unsafe unsafe;
    private static final long bytesBaseOffset;

    static {
        sun.misc.Unsafe handle = null;
        int offset = 0;
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            handle = (sun.misc.Unsafe) field.get(null);
            offset = handle.arrayBaseOffset(byte[].class);
            byte[] tmp = new byte[]{(byte) 0xff, 0};
            if (handle.getShort(tmp, offset) != 0xff) {
                handle = null;
            }
        } catch (Exception ignored) {
        }
        unsafe = handle;
        bytesBaseOffset = offset;
    }

    public static short getShort(byte[] data, int offset) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 2) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getShort(data, bytesBaseOffset + offset);
        }
        return ByteBuffer.wrap(data, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public static int getInt(byte[] data, int offset) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 4) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getInt(data, bytesBaseOffset + offset);
        }
        return ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static long getLong(byte[] data, int offset) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 8) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getLong(data, bytesBaseOffset + offset);
        }
        return ByteBuffer.wrap(data, offset, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public static float getFloat(byte[] data, int offset) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 4) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getFloat(data, bytesBaseOffset + offset);
        }
        return ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public static double getDouble(byte[] data, int offset) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 8) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getDouble(data, bytesBaseOffset + offset);
        }
        return ByteBuffer.wrap(data, offset, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public static void putShort(byte[] data, int offset, short value) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 2) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putShort(data, bytesBaseOffset + offset, value);
            return;
        }
        ByteBuffer.wrap(data, offset, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(value);
    }

    public static void putInt(byte[] data, int offset, int value) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 4) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putInt(data, bytesBaseOffset + offset, value);
            return;
        }
        ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(value);
    }

    public static void putLong(byte[] data, int offset, long value) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 8) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putLong(data, bytesBaseOffset + offset, value);
            return;
        }
        ByteBuffer.wrap(data, offset, 8).order(ByteOrder.LITTLE_ENDIAN).putLong(value);
    }

    public static void putFloat(byte[] data, int offset, float value) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 4) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putFloat(data, bytesBaseOffset + offset, value);
            return;
        }
        ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value);
    }

    public static void putDouble(byte[] data, int offset, double value) {
        if (unsafe != null) {
            if (offset < 0 || offset > data.length - 8) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putDouble(data, bytesBaseOffset + offset, value);
            return;
        }
        ByteBuffer.wrap(data, offset, 8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value);
    }

    public static final class View {
        public final byte[] data;
        public final int offset;
        public final int limit;

        public View(byte[] data, int offset, int limit) {
            this.data = data;
            this.offset = offset;
            this.limit = limit;
        }

        public View(byte[] data, int offset) {
            this(data, offset, data.length);
        }

        public View(byte[] data) {
            this(data, 0);
        }

        public int size() {
            return limit - offset;
        }
    }
}
