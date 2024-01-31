// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class DataView {
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
        return ByteBuffer.wrap(this.data, this.offset + offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public short getShort() {
        return getShort(0);
    }

    public int getInt(int offset) {
        return ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public int getInt() {
        return getInt(0);
    }

    public long getLong(int offset) {
        return ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    public long getLong() {
        return getLong(0);
    }

    public float getFloat(int offset) {
        return ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public float getFloat() {
        return getFloat(0);
    }

    public double getDouble(int offset) {
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
        ByteBuffer.wrap(this.data, this.offset + offset, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(value);
    }

    public void putShort(short value) {
        putInt(value, 0);
    }

    public void putInt(int value, int offset) {
        ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(value);
    }

    public void putInt(int value) {
        putInt(value, 0);
    }

    public void putLong(long value, int offset) {
        ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).putLong(value);
    }

    public void putLong(long value) {
        putLong(value, 0);
    }

    public void putFloat(float value, int offset) {
        ByteBuffer.wrap(this.data, this.offset + offset, 4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value);
    }

    public void putFloat(float value) {
        putFloat(value, 0);
    }

    public void putDouble(double value, int offset) {
        ByteBuffer.wrap(this.data, this.offset + offset, 8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value);
    }

    public void putDouble(double value) {
        putDouble(value, 0);
    }
}
