// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class PerfectHashTest {
    private void test(int size) {
        byte[][] keys = new byte[size][];
        for (int i = 0; i < size; i++) {
            keys[i] = String.valueOf(i).getBytes(StandardCharsets.UTF_8);
        }
        PerfectHash ph = PerfectHash.build(new Reader(keys));
        BitSet mark = new BitSet(size);
        for (int i = 0; i < size; i++) {
            int pos = ph.locate(keys[i]);
            Assertions.assertFalse(mark.get(pos));
            mark.set(pos);
        }
    }

    @Test
    public void tinyTest() {
        test(0);
        test(1);
        test(2);
        test(24);
    }

    @Test
    public void smallTest() {
        test(200);
        test(1000);
    }

    @Test
    public void bitTest() {
        test(100000);
    }

    private static final class Reader implements PerfectHash.KeySource {
        private final byte[][] keys;
        int current = 0;

        public Reader(byte[][] keys) {
            this.keys = keys;
        }


        @Override
        public void reset() {
            current = 0;
        }

        @Override
        public int total() {
            return keys.length;
        }

        @Override
        public byte[] next() {
            return keys[current++];
        }
    }
}
