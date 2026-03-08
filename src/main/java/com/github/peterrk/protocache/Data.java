// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteOrder;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

final class Data {

    private static final VarHandle SHORT_VIEW =
            MethodHandles.byteArrayViewVarHandle(short[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle INT_VIEW =
            MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle LONG_VIEW =
            MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle FLOAT_VIEW =
            MethodHandles.byteArrayViewVarHandle(float[].class, ByteOrder.LITTLE_ENDIAN);
    private static final VarHandle DOUBLE_VIEW =
            MethodHandles.byteArrayViewVarHandle(double[].class, ByteOrder.LITTLE_ENDIAN);

    public static short getShort(byte[] data, int offset) {
        return (short) SHORT_VIEW.get(data, offset);
    }

    public static int getInt(byte[] data, int offset) {
        return (int) INT_VIEW.get(data, offset);
    }

    public static long getLong(byte[] data, int offset) {
        return (long) LONG_VIEW.get(data, offset);
    }

    public static float getFloat(byte[] data, int offset) {
        return (float) FLOAT_VIEW.get(data, offset);
    }

    public static double getDouble(byte[] data, int offset) {
        return (double) DOUBLE_VIEW.get(data, offset);
    }

    public static void putShort(byte[] data, int offset, short value) {
        SHORT_VIEW.set(data, offset, value);
    }

    public static void putInt(byte[] data, int offset, int value) {
        INT_VIEW.set(data, offset, value);
    }

    public static void putLong(byte[] data, int offset, long value) {
        LONG_VIEW.set(data, offset, value);
    }

    public static void putFloat(byte[] data, int offset, float value) {
        FLOAT_VIEW.set(data, offset, value);
    }

    public static void putDouble(byte[] data, int offset, double value) {
        DOUBLE_VIEW.set(data, offset, value);
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
