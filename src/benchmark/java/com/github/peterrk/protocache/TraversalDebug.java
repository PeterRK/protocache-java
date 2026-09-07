// Copyright (c) 2026, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/** Diagnostic totals from the same traversals used by JMH, outside measurement. */
public final class TraversalDebug {
    private TraversalDebug() {}

    static final class Junk implements AccessBenchmark.Sink {
        int i32;
        long i64;
        float f32;
        double f64;

        public void consume(int value) { i32 += value; }
        public void consume(long value) { i64 += value; }
        public void consume(float value) { f32 += value; }
        public void consume(double value) { f64 += value; }
        public void consume(boolean value) { if (value) i32++; }
        public void consume(String value) { i32 += value.hashCode(); }
        public void consume(byte[] value) { i32 += Arrays.hashCode(value); }

        boolean matches(Junk other) {
            // Map iteration order can change floating-point rounding.
            return i32 == other.i32 && i64 == other.i64
                    && Math.abs(f32 - other.f32) <= 8 * Math.ulp(Math.max(Math.abs(f32), Math.abs(other.f32)))
                    && Math.abs(f64 - other.f64) <= 8 * Math.ulp(Math.max(Math.abs(f64), Math.abs(other.f64)));
        }

        @Override
        public String toString() {
            return String.format(Locale.ROOT, "i32=%08x i64=%016x f32=%s f64=%s", i32, i64, f32, f64);
        }
    }

    static Map<String, Junk> collect() throws IOException {
        Map<String, Junk> results = new LinkedHashMap<>();

        AccessBenchmark.ProtobufState protobuf = new AccessBenchmark.ProtobufState();
        protobuf.setup();
        Junk junk = new Junk();
        protobuf.traverse(com.github.peterrk.protocache.pb.Main.parseFrom(protobuf.raw), junk);
        results.put("Protobuf", junk);

        AccessBenchmark.ProtoCacheState protocache = new AccessBenchmark.ProtoCacheState();
        protocache.setup();
        junk = new Junk();
        protocache.traverse(new com.github.peterrk.protocache.pc.Main(protocache.raw), junk);
        results.put("ProtoCache", junk);

        AccessBenchmark.DummyState dummy = new AccessBenchmark.DummyState();
        dummy.setup();
        junk = new Junk();
        dummy.traverse(dummy.root, junk);
        results.put("Dummy", junk);

        AccessBenchmark.ForyState fory = new AccessBenchmark.ForyState();
        fory.setup();
        junk = new Junk();
        fory.traverse(fory.fory.deserialize(fory.raw, com.github.peterrk.protocache.fr.Main.class), junk);
        results.put("Fory", junk);

        AccessBenchmark.JavaForyState foryJava = new AccessBenchmark.JavaForyState();
        foryJava.setup();
        junk = new Junk();
        foryJava.traverse(foryJava.fory.deserialize(foryJava.raw, com.github.peterrk.protocache.fr.Main.class), junk);
        results.put("Fory-Java", junk);

        if (FlatbuffersFixture.exists()) {
            AccessBenchmark.FlatbuffersState flatbuffers = new AccessBenchmark.FlatbuffersState();
            flatbuffers.setup();
            junk = new Junk();
            flatbuffers.traverse(com.github.peterrk.protocache.fb.Main.getRootAsMain(
                    ByteBuffer.wrap(flatbuffers.raw)), junk);
            results.put("FlatBuffers", junk);
        }
        return results;
    }

    static void verify(Map<String, Junk> results) {
        Junk expected = results.get("Protobuf");
        for (Map.Entry<String, Junk> entry : results.entrySet()) {
            if (!expected.matches(entry.getValue())) {
                throw new IllegalStateException(entry.getKey() + " traversal differs from Protobuf: "
                        + entry.getValue() + "; expected " + expected);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Junk> results = collect();
        results.forEach((name, junk) -> System.out.println(name + ": " + junk));
        if (!results.containsKey("FlatBuffers")) {
            System.out.println("SKIP FlatBuffers: " + FlatbuffersFixture.missingMessage());
        }
        verify(results);
        System.out.println("Traversal totals agree for " + results.size() + " inputs.");
    }
}
