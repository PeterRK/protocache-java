// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import com.github.peterrk.protocache.pc.*;
import com.google.protobuf.util.JsonFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProtoCacheTest {

    @Test
    public void binaryTest() {
        byte[] raw = null;
        try {
            //raw = Files.readAllBytes(Paths.get("test.pc"));
            raw = Files.readAllBytes(Paths.get("test.json"));
            com.github.peterrk.protocache.pb.Main.Builder builder = com.github.peterrk.protocache.pb.Main.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(new String(raw, StandardCharsets.UTF_8), builder);
            raw = ProtoCache.serialize(builder.build());
        } catch (IOException e) {
            Assertions.fail();
        }

        Main root = new Main(raw);

        Assertions.assertEquals(-999, root.getI32());
        Assertions.assertEquals(1234, root.getU32());
        Assertions.assertEquals(-9876543210L, root.getI64());
        Assertions.assertEquals(98765432123456789L, root.getU64());
        Assertions.assertTrue(root.getFlag());
        Assertions.assertEquals(Mode.MODE_C, root.getMode());
        Assertions.assertEquals("Hello World!", root.getStr());
        Assertions.assertArrayEquals(root.getData(), "abc123!?$*&()'-=@~".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(-2.1f, root.getF32());
        Assertions.assertEquals(1.0, root.getF64());

        Small leaf = root.getObject();
        Assertions.assertEquals(88, leaf.getI32());
        Assertions.assertFalse(leaf.getFlag());
        Assertions.assertEquals("tmp", leaf.getStr());

        Int32Array i32v = root.getI32V();
        Assertions.assertEquals(2, i32v.size());
        Assertions.assertEquals(1, i32v.get(0));
        Assertions.assertEquals(2, i32v.get(1));

        Int64Array u64v = root.getU64V();
        Assertions.assertEquals(1, u64v.size());
        Assertions.assertEquals(12345678987654321L, u64v.get(0));

        String[] expectedStrv = new String[]{
                "abc", "apple", "banana", "orange", "pear", "grape",
                "strawberry", "cherry", "mango", "watermelon"};
        StrArray strv = root.getStrv();
        Assertions.assertEquals(expectedStrv.length, strv.size());
        for (int i = 0; i < expectedStrv.length; i++) {
            Assertions.assertEquals(expectedStrv[i], strv.get(i));
        }

        Float32Array f32v = root.getF32V();
        Assertions.assertEquals(2, f32v.size());
        Assertions.assertEquals(1.1f, f32v.get(0));
        Assertions.assertEquals(2.2f, f32v.get(1));

        double[] expectedF64v = new double[]{9.9, 8.8, 7.7, 6.6, 5.5};
        Float64Array f64v = root.getF64V();
        Assertions.assertEquals(f64v.size(), expectedF64v.length);
        for (int i = 0; i < expectedF64v.length; i++) {
            Assertions.assertEquals(f64v.get(i), expectedF64v[i]);
        }

        boolean[] expectedFlags = new boolean[]{true, true, false, true, false, false, false};
        BoolArray flags = root.getFlags();
        Assertions.assertEquals(flags.size(), expectedFlags.length);
        for (int i = 0; i < expectedFlags.length; i++) {
            Assertions.assertEquals(flags.get(i), expectedFlags[i]);
        }

        Array<Small> objv = root.getObjectv();
        Assertions.assertEquals(3, objv.size());
        Assertions.assertEquals(1, objv.fastGet(0, leaf).getI32());
        Assertions.assertTrue(objv.fastGet(1, leaf).getFlag());
        Assertions.assertEquals("good luck!", objv.fastGet(2, leaf).getStr());

        Assertions.assertFalse(root.hasField(Main.FIELD_t_i32));

        Int32 i32 = new Int32();
        StrDict<Int32> map1 = root.getIndex();
        Assertions.assertEquals(6, map1.size());
        Int32 val1 = map1.find("abc-1", Int32::new);
        Assertions.assertNotNull(val1);
        Assertions.assertEquals(1, val1.get());
        val1 = map1.fastFind("abc-2", i32);
        Assertions.assertNotNull(val1);
        Assertions.assertEquals(2, val1.get());
        Assertions.assertNull(map1.fastFind("abc-3", i32));
        Assertions.assertNull(map1.find("abc-4", Int32::new));

        Int32Dict<Small> map2 = root.getObjects();
        for (int i = 0; i < map2.size(); i++) {
            int key = map2.key(i);
            Assertions.assertNotEquals(0, key);
            Assertions.assertEquals(key, map2.fastGetValue(i, leaf).getI32());
        }

        Vec2D matrix = root.getMatrix();
        Assertions.assertEquals(3, matrix.size());
        Vec2D.Vec1D line = matrix.get(2, Vec2D.Vec1D::new);
        Assertions.assertEquals(3, line.size());
        Assertions.assertEquals(9, line.get(2));

        Array<ArrMap> vector = root.getVector();
        Assertions.assertEquals(2, vector.size());
        ArrMap map3 = vector.get(0, ArrMap::new);
        ArrMap.Array val3 = map3.find("lv2", ArrMap.Array::new);
        Assertions.assertNotNull(val3);
        Assertions.assertEquals(2, val3.size());
        Assertions.assertEquals(21, val3.get(0));
        Assertions.assertEquals(22, val3.get(1));

        ArrMap map4 = root.getArrays();
        ArrMap.Array val4 = map4.find("lv5", ArrMap.Array::new);
        Assertions.assertEquals(2, val4.size());
        Assertions.assertEquals(51, val4.get(0));
        Assertions.assertEquals(52, val4.get(1));
    }

    @Test
    public void aliasTest() {
        byte[] raw = null;
        try {
            raw = Files.readAllBytes(Paths.get("test-alias.json"));
            com.github.peterrk.protocache.pb.Main.Builder builder = com.github.peterrk.protocache.pb.Main.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(new String(raw, StandardCharsets.UTF_8), builder);
            raw = ProtoCache.serialize(builder.build());
        } catch (IOException e) {
            Assertions.fail();
        }
        Assertions.assertEquals(68, raw.length);
        Assertions.assertEquals(0xd, Data.getInt(raw, 20));
        Assertions.assertEquals(1, Data.getInt(raw, 24));
        Assertions.assertEquals(1, Data.getInt(raw, 28));
    }

    @Test
    public void compressTest() {
        byte[] raw = null;
        try {
            //raw = Files.readAllBytes(Paths.get("test.pc"));
            raw = Files.readAllBytes(Paths.get("test.json"));
            com.github.peterrk.protocache.pb.Main.Builder builder = com.github.peterrk.protocache.pb.Main.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(new String(raw, StandardCharsets.UTF_8), builder);
            raw = ProtoCache.serialize(builder.build());
        } catch (IOException e) {
            Assertions.fail();
        }

        byte[] compressed = Utils.compress(raw);
        Assertions.assertTrue(compressed.length != 0 && compressed.length < raw.length);
        byte[] back = Utils.decompress(compressed);
        Assertions.assertArrayEquals(raw, back);
    }

    @Test
    public void reflectTest() {
        //TODO
    }
}
