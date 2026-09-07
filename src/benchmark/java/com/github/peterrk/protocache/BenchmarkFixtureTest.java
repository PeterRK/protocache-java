// Copyright (c) 2026, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class BenchmarkFixtureTest {
    @Test
    public void traversalTotalsAgreeAcrossFormats() throws IOException {
        TraversalDebug.verify(TraversalDebug.collect());
    }

    @Test
    public void traversalDebugRejectsChangedInput() throws IOException {
        AccessBenchmark.ProtobufState state = new AccessBenchmark.ProtobufState();
        com.github.peterrk.protocache.pb.Main root = TestData.protobufMain();
        TraversalDebug.Junk original = new TraversalDebug.Junk();
        state.traverse(root, original);
        TraversalDebug.Junk changed = new TraversalDebug.Junk();
        state.traverse(root.toBuilder().setI32(root.getI32() + 1).build(), changed);
        assertThrows(IllegalStateException.class,
                () -> TraversalDebug.verify(Map.of("Protobuf", original, "Changed", changed)));
    }

    @Test
    public void flatbuffersFixtureIsValidWhenGenerated() throws IOException {
        Assumptions.assumeTrue(FlatbuffersFixture.exists(), FlatbuffersFixture.missingMessage());

        AccessBenchmark.FlatbuffersState flatbuffers = new AccessBenchmark.FlatbuffersState();
        flatbuffers.setup();
        com.github.peterrk.protocache.fb.Main root = com.github.peterrk.protocache.fb.Main.getRootAsMain(
                ByteBuffer.wrap(flatbuffers.raw));
        assertEquals(TestData.protobufMain().getI32(), root.i32());
    }
}
