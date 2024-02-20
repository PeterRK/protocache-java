// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class DataView {
    private static final sun.misc.Unsafe unsafe;
    private static final long bytesBaseOffset;
    static {
        sun.misc.Unsafe handle = null;
        int offset = 0;
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            handle = (sun.misc.Unsafe)field.get(null);
            offset = handle.arrayBaseOffset(byte[].class);
            byte[] tmp = new byte[]{(byte)0xff, 0};
            if (handle.getShort(tmp, offset) != 0xff) {
                handle = null;
            }
        } catch (Exception ignored) {}
        unsafe = handle;
        bytesBaseOffset = offset;
    }

    public final byte[] data;
    public final int offset;
    public final int limit;

    public DataView(byte[] data, int offset, int limit) {
        this.data = data;
        this.offset = offset;
        this.limit = limit;
    }

    public DataView(byte[] data, int offset) {
        this(data, offset, data.length);
    }

    public DataView(byte[] data) {
        this(data, 0);
    }

    public int size() {
        return limit - offset;
    }

    public byte getByte(int offset) {
        return this.data[this.offset + offset];
    }

    public byte getByte() {
        return getByte(0);
    }

    public short getShort(int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-2) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getShort(this.data, bytesBaseOffset+offset);
        }
        return ByteBuffer.wrap(this.data, this.offset + offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public short getShort() {
        return getShort(0);
    }

    public int getInt(int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-4) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getInt(this.data, bytesBaseOffset+offset);
        }
        return ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public int getInt() {
        return getInt(0);
    }

    public long getLong(int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-8) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getLong(this.data, bytesBaseOffset+offset);
        }
        return ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public long getLong() {
        return getLong(0);
    }

    public float getFloat(int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-4) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getFloat(this.data, bytesBaseOffset+offset);
        }
        return ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public float getFloat() {
        return getFloat(0);
    }

    public double getDouble(int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-8) {
                throw new IndexOutOfBoundsException();
            }
            return unsafe.getDouble(this.data, bytesBaseOffset+offset);
        }
        return ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public double getDouble() {
        return getDouble(0);
    }

    public void putByte(byte value, int offset) {
        this.data[this.offset + offset] = value;
    }

    public void putByte(byte value) {
        putByte(value, 0);
    }

    public void putShort(short value, int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-2) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putShort(this.data, bytesBaseOffset+offset, value);
            return;
        }
        ByteBuffer.wrap(this.data, this.offset + offset, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(value);
    }

    public void putShort(short value) {
        putInt(value, 0);
    }

    public void putInt(int value, int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-4) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putInt(this.data, bytesBaseOffset+offset, value);
            return;
        }
        ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(value);
    }

    public void putInt(int value) {
        putInt(value, 0);
    }

    public void putLong(long value, int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-8) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putLong(this.data, bytesBaseOffset+offset, value);
            return;
        }
        ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).putLong(value);
    }

    public void putLong(long value) {
        putLong(value, 0);
    }

    public void putFloat(float value, int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-4) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putFloat(this.data, bytesBaseOffset+offset, value);
            return;
        }
        ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value);
    }

    public void putFloat(float value) {
        putFloat(value, 0);
    }

    public void putDouble(double value, int offset) {
        if (unsafe != null) {
            offset += this.offset;
            if (offset < 0 || offset > this.limit-8) {
                throw new IndexOutOfBoundsException();
            }
            unsafe.putDouble(this.data, bytesBaseOffset+offset, value);
            return;
        }
        ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value);
    }

    public void putDouble(double value) {
        putDouble(value, 0);
    }
}
