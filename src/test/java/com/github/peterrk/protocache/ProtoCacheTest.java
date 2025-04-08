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
        Assertions.assertEquals(i32v.size(), 2);
        Assertions.assertEquals(i32v.get(0), 1);
        Assertions.assertEquals(i32v.get(1), 2);

        Int64Array u64v = root.getU64V();
        Assertions.assertEquals(u64v.size(), 1);
        Assertions.assertEquals(u64v.get(0), 12345678987654321L);

        String[] expectedStrv = new String[]{
                "abc", "apple", "banana", "orange", "pear", "grape",
                "strawberry", "cherry", "mango", "watermelon"};
        StrArray strv = root.getStrv();
        Assertions.assertEquals(strv.size(), expectedStrv.length);
        for (int i = 0; i < expectedStrv.length; i++) {
            Assertions.assertEquals(strv.get(i), expectedStrv[i]);
        }

        Float32Array f32v = root.getF32V();
        Assertions.assertEquals(f32v.size(), 2);
        Assertions.assertEquals(f32v.get(0), 1.1f);
        Assertions.assertEquals(f32v.get(1), 2.2f);

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
        Assertions.assertEquals(objv.size(), 3);
        Assertions.assertEquals(objv.fastGet(0, leaf).getI32(), 1);
        Assertions.assertTrue(objv.fastGet(1, leaf).getFlag());
        Assertions.assertEquals(objv.fastGet(2, leaf).getStr(), "good luck!");

        Assertions.assertFalse(root.hasField(Main.FIELD_t_i32));

        Int32 i32 = new Int32();
        StrDict<Int32> map1 = root.getIndex();
        Assertions.assertEquals(map1.size(), 6);
        Int32 val1 = map1.find("abc-1", Int32::new);
        Assertions.assertNotNull(val1);
        Assertions.assertEquals(val1.get(), 1);
        val1 = map1.fastFind("abc-2", i32);
        Assertions.assertNotNull(val1);
        Assertions.assertEquals(val1.get(), 2);
        Assertions.assertNull(map1.fastFind("abc-3", i32));
        Assertions.assertNull(map1.find("abc-4", Int32::new));

        Int32Dict<Small> map2 = root.getObjects();
        for (int i = 0; i < map2.size(); i++) {
            int key = map2.key(i);
            Assertions.assertNotEquals(key, 0);
            Assertions.assertEquals(key, map2.fastGetValue(i, leaf).getI32());
        }

        Vec2D matrix = root.getMatrix();
        Assertions.assertEquals(matrix.size(), 3);
        Vec2D.Vec1D line = matrix.get(2, Vec2D.Vec1D::new);
        Assertions.assertEquals(line.size(), 3);
        Assertions.assertEquals(line.get(2), 9);

        Array<ArrMap> vector = root.getVector();
        Assertions.assertEquals(vector.size(), 2);
        ArrMap map3 = vector.get(0, ArrMap::new);
        ArrMap.Array val3 = map3.find("lv2", ArrMap.Array::new);
        Assertions.assertNotNull(val3);
        Assertions.assertEquals(val3.size(), 2);
        Assertions.assertEquals(val3.get(0), 21);
        Assertions.assertEquals(val3.get(1), 22);

        ArrMap map4 = root.getArrays();
        ArrMap.Array val4 = map4.find("lv5", ArrMap.Array::new);
        Assertions.assertEquals(val4.size(), 2);
        Assertions.assertEquals(val4.get(0), 51);
        Assertions.assertEquals(val4.get(1), 52);
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
