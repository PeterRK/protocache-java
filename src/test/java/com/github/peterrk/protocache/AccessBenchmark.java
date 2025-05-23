package com.github.peterrk.protocache;

import com.github.peterrk.protocache.pc.ArrMap;
import com.github.peterrk.protocache.pc.Small;
import com.google.gson.Gson;
import org.apache.fury.Fury;
import org.apache.fury.config.Language;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
import org.openjdk.jmh.profile.AsyncProfiler;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.Runner;
*/

@Fork(value = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class AccessBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
/*
        Options opt = new OptionsBuilder()
                .include(AccessBenchmark.class.getSimpleName())
                .addProfiler(AsyncProfiler.class)
                .build();

        new Runner(opt).run();
 */
    }

    @Benchmark
    public void traverseDummy(DummyState ctx) {
        ctx.traverse(ctx.root);
    }

    @Benchmark
    public void traverseFury(FuryState ctx) {
        com.github.peterrk.protocache.fr.Main root = (com.github.peterrk.protocache.fr.Main) ctx.fury.deserialize(ctx.raw);
        ctx.traverse(root);
    }

    @Benchmark
    public void traverseFuryJava(JavaFuryState ctx) {
        com.github.peterrk.protocache.fr.Main root = (com.github.peterrk.protocache.fr.Main) ctx.fury.deserialize(ctx.raw);
        ctx.traverse(root);
    }

    @Benchmark
    public void traverseProtoCache(ProtoCacheState ctx) {
        com.github.peterrk.protocache.pc.Main root = new com.github.peterrk.protocache.pc.Main(ctx.raw);
        ctx.traverse(root);
    }

    @Benchmark
    public void traverseProtobuf(ProtobufState ctx) throws IOException {
        com.github.peterrk.protocache.pb.Main root = com.github.peterrk.protocache.pb.Main.parseFrom(ctx.raw);
        ctx.traverse(root);
    }

    @Benchmark
    public void traverseFlatbuffers(FlatbuffersState ctx) {
        com.github.peterrk.protocache.fb.Main root = com.github.peterrk.protocache.fb.Main.getRootAsMain(ByteBuffer.wrap(ctx.raw));
        ctx.traverse(root);
    }

    @Benchmark
    public void compress(CompressState ctx) {
        Utils.compress(ctx.raw);
    }

    @Benchmark
    public void decompress(CompressState ctx) {
        Utils.decompress(ctx.compressed);
    }

    @State(Scope.Thread)
    public static class CompressState {
        byte[] raw;
        byte[] compressed;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = Files.readAllBytes(Paths.get("test.pc"));
            compressed = Utils.compress(raw);
        }
    }

    @State(Scope.Thread)
    public static class FuryState extends FuryStateBase {
        Fury fury;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            byte[] tmp = Files.readAllBytes(Paths.get("test-fury.json"));
            Gson gson = new Gson();
            com.github.peterrk.protocache.fr.Main root = gson.fromJson(new String(tmp, StandardCharsets.UTF_8), com.github.peterrk.protocache.fr.Main.class);
            wash(root);

            fury = Fury.builder().withLanguage(Language.XLANG).build();
            fury.register(com.github.peterrk.protocache.fr.Small.class, "test.Small");
            fury.register(com.github.peterrk.protocache.fr.Main.class, "test.Main");
            fury.register(java.util.Map.class, "java.util.Map");

            raw = fury.serialize(root);
            //fury.deserialize(raw);

            junk = new Junk();
        }
    }

    @State(Scope.Thread)
    public static class JavaFuryState extends FuryStateBase {
        Fury fury;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            byte[] tmp = Files.readAllBytes(Paths.get("test-fury.json"));
            Gson gson = new Gson();
            com.github.peterrk.protocache.fr.Main root = gson.fromJson(new String(tmp, StandardCharsets.UTF_8), com.github.peterrk.protocache.fr.Main.class);
            wash(root);

            fury = Fury.builder().withLanguage(Language.JAVA).build();
            fury.register(com.github.peterrk.protocache.fr.Small.class);
            fury.register(com.github.peterrk.protocache.fr.Main.class);
            fury.register(java.util.Map.class);

            raw = fury.serialize(root);
            //fury.deserialize(raw);

            junk = new Junk();
        }
    }

    @State(Scope.Thread)
    public static class DummyState extends FuryStateBase {
        com.github.peterrk.protocache.fr.Main root;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            byte[] tmp = Files.readAllBytes(Paths.get("test-fury.json"));
            Gson gson = new Gson();
            root = gson.fromJson(new String(tmp, StandardCharsets.UTF_8), com.github.peterrk.protocache.fr.Main.class);
            junk = new Junk();
        }
    }

    @State(Scope.Thread)
    public static class FuryStateBase {
        Junk junk;
        byte[] raw;

        @TearDown(value = Level.Invocation)
        public void reset() {
            //junk.print();
            junk.reset();
        }

        public static void wash(com.github.peterrk.protocache.fr.Main root) {
            root.index = new HashMap<>(root.index);
            root.objects = new HashMap<>(root.objects);
            for (int i = 0; i < root.vector.length; i++) {
                root.vector[i] = new HashMap<>(root.vector[i]);
            }
            root.arrays = new HashMap<>(root.arrays);
        }

        void traverse(com.github.peterrk.protocache.fr.Main root) {
            junk.consume(root.i32);
            junk.consume(root.u32);
            junk.consume(root.i64);
            junk.consume(root.u64);
            junk.consume(root.flag);
            junk.consume(root.mode);
            if (root.str != null) {
                junk.consume(root.str);
            }
            if (root.data != null) {
                junk.consume(root.data);
            }
            junk.consume(root.f32);
            junk.consume(root.f64);
            if (root.object != null) {
                traverse(root.object);
            }
            if (root.i32v != null) {
                for (int v : root.i32v) {
                    junk.consume(v);
                }
            }
            if (root.u64v != null) {
                for (long v : root.u64v) {
                    junk.consume(v);
                }
            }
            if (root.strv != null) {
                for (String v : root.strv) {
                    junk.consume(v);
                }
            }
            if (root.datav != null) {
                for (byte[] v : root.datav) {
                    junk.consume(v);
                }
            }
            if (root.f32v != null) {
                for (float v : root.f32v) {
                    junk.consume(v);
                }
            }
            if (root.f64v != null) {
                for (double v : root.f64v) {
                    junk.consume(v);
                }
            }
            if (root.flags != null) {
                for (boolean v : root.flags) {
                    junk.consume(v);
                }
            }
            if (root.objectv != null) {
                for (com.github.peterrk.protocache.fr.Small v : root.objectv) {
                    traverse(v);
                }
            }
            junk.consume(root.t_u32);
            junk.consume(root.t_i32);
            junk.consume(root.t_s32);
            junk.consume(root.t_u64);
            junk.consume(root.t_i64);
            junk.consume(root.t_s64);

            if (root.index != null) {
                for (Map.Entry<String, Integer> entry : root.index.entrySet()) {
                    junk.consume(entry.getKey());
                    junk.consume(entry.getValue());
                }
            }

            if (root.objects != null) {
                for (Map.Entry<Integer, com.github.peterrk.protocache.fr.Small> entry : root.objects.entrySet()) {
                    junk.consume(entry.getKey());
                    traverse(entry.getValue());
                }
            }

            if (root.matrix != null) {
                for (float[] u : root.matrix) {
                    for (float v : u) {
                        junk.consume(v);
                    }
                }
            }

            if (root.vector != null) {
                for (Map<String, float[]> u : root.vector) {
                    traverse(u);
                }
            }
            if (root.arrays != null) {
                traverse(root.arrays);
            }
        }

        void traverse(com.github.peterrk.protocache.fr.Small root) {
            junk.consume(root.i32);
            junk.consume(root.flag);
            if (root.str != null) {
                junk.consume(root.str);
            }
        }

        void traverse(Map<String, float[]> map) {
            for (Map.Entry<String, float[]> entry : map.entrySet()) {
                junk.consume(entry.getKey());
                for (float v : entry.getValue()) {
                    junk.consume(v);
                }
            }
        }
    }

    private static final class Junk {
        private int i32 = 0;
        private float f32 = 0;
        private long i64 = 0;
        private double f64 = 0;

        public void print() {
            System.out.printf("%x %x, %f, %f\n", i32, i64, f32, f64);
        }

        public void reset() {
            i32 = 0;
            f32 = 0;
            i64 = 0;
            f64 = 0;
        }

        public void consume(int v) {
            i32 += v;
        }

        public void consume(long v) {
            i64 += v;
        }

        public void consume(float v) {
            f32 += v;
        }

        public void consume(double v) {
            f64 += v;
        }

        public void consume(boolean v) {
            if (v) {
                i32 += 1;
            }
        }

        public void consume(String v) {
            i32 += v.hashCode();
        }

        public void consume(byte[] v) {
            i32 += Arrays.hashCode(v);
        }
    }

    @State(Scope.Thread)
    public static class ProtoCacheState {
        private final com.github.peterrk.protocache.pc.Small tmpSmall = new com.github.peterrk.protocache.pc.Small();
        private final com.github.peterrk.protocache.pc.ArrMap tmpArrMap = new com.github.peterrk.protocache.pc.ArrMap();
        private final com.github.peterrk.protocache.pc.ArrMap.Array tmpArrMapArray = new com.github.peterrk.protocache.pc.ArrMap.Array();
        private final com.github.peterrk.protocache.pc.Vec2D.Vec1D tmpVec2DVec1D = new com.github.peterrk.protocache.pc.Vec2D.Vec1D();
        Junk junk = new Junk();
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = Files.readAllBytes(Paths.get("test.pc"));
        }

        @TearDown(value = Level.Invocation)
        public void reset() {
            //junk.print();
            junk.reset();
        }

        void traverse(com.github.peterrk.protocache.pc.Main root) {
            junk.consume(root.getI32());
            junk.consume(root.getU32());
            junk.consume(root.getI64());
            junk.consume(root.getU64());
            junk.consume(root.getFlag());
            junk.consume(root.getMode());
            junk.consume(root.getStr());
            junk.consume(root.getData());
            junk.consume(root.getF32());
            junk.consume(root.getF64());
            traverse(root.getObject());
            Int32Array i32v = root.getI32V();
            for (int i = 0; i < i32v.size(); i++) {
                junk.consume(i32v.get(i));
            }
            Int64Array u64v = root.getU64V();
            for (int i = 0; i < u64v.size(); i++) {
                junk.consume(u64v.get(i));
            }
            StringArray strv = root.getStrv();
            for (int i = 0; i < strv.size(); i++) {
                junk.consume(strv.get(i));
            }
            BytesArray datv = root.getDatav();
            for (int i = 0; i < datv.size(); i++) {
                junk.consume(datv.get(i));
            }
            Float32Array f32v = root.getF32V();
            for (int i = 0; i < f32v.size(); i++) {
                junk.consume(f32v.get(i));
            }
            Float64Array f64v = root.getF64V();
            for (int i = 0; i < f64v.size(); i++) {
                junk.consume(f64v.get(i));
            }
            BoolArray flags = root.getFlags();
            for (int i = 0; i < flags.size(); i++) {
                junk.consume(flags.get(i));
            }
            ObjectArray<Small> objv = root.getObjectv();
            for (int i = 0; i < objv.size(); i++) {
                traverse(objv.get(i, tmpSmall));
            }

            junk.consume(root.getTU32());
            junk.consume(root.getTI32());
            junk.consume(root.getTS32());
            junk.consume(root.getTU64());
            junk.consume(root.getTI64());
            junk.consume(root.getTS64());

            StringDict.Int32Value map1 = root.getIndex();
            for (int i = 0; i < map1.size(); i++) {
                junk.consume(map1.getKey(i));
                junk.consume(map1.getValue(i));
            }

            Int32Dict.ObjectValue<com.github.peterrk.protocache.pc.Small> map2 = root.getObjects();
            for (int i = 0; i < map2.size(); i++) {
                junk.consume(map2.getKey(i));
                traverse(map2.getValue(i, tmpSmall));
            }

            traverse(root.getMatrix());

            ObjectArray<com.github.peterrk.protocache.pc.ArrMap> vec = root.getVector();
            for (int i = 0; i < vec.size(); i++) {
                traverse(vec.get(i, tmpArrMap));
            }
            traverse(root.getArrays());
        }

        void traverse(com.github.peterrk.protocache.pc.Small root) {
            junk.consume(root.getI32());
            junk.consume(root.getFlag());
            junk.consume(root.getStr());
        }

        void traverse(com.github.peterrk.protocache.pc.ArrMap map) {
            for (int i = 0; i < map.size(); i++) {
                junk.consume(map.getKey(i));
                map.getValue(i, tmpArrMapArray);
                for (int j = 0; j < tmpArrMapArray.size(); j++) {
                    junk.consume(tmpArrMapArray.get(j));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.pc.Vec2D vec) {
            for (int i = 0; i < vec.size(); i++) {
                vec.get(i, tmpVec2DVec1D);
                for (int j = 0; j < tmpVec2DVec1D.size(); j++) {
                    junk.consume(tmpVec2DVec1D.get(j));
                }
            }
        }
    }

    @State(Scope.Thread)
    public static class ProtobufState {
        Junk junk;
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = Files.readAllBytes(Paths.get("test.pb"));
            junk = new Junk();
        }

        @TearDown(value = Level.Invocation)
        public void reset() {
            //junk.print();
            junk.reset();
        }

        void traverse(com.github.peterrk.protocache.pb.Main root) {
            junk.consume(root.getI32());
            junk.consume(root.getU32());
            junk.consume(root.getI64());
            junk.consume(root.getU64());
            junk.consume(root.getFlag());
            junk.consume(root.getMode().getNumber());
            junk.consume(root.getStr());
            junk.consume(root.getData().toByteArray());
            junk.consume(root.getF32());
            junk.consume(root.getF64());
            traverse(root.getObject());
            for (int i = 0; i < root.getI32VCount(); i++) {
                junk.consume(root.getI32V(i));
            }
            for (int i = 0; i < root.getU64VCount(); i++) {
                junk.consume(root.getU64V(i));
            }
            for (int i = 0; i < root.getStrvCount(); i++) {
                junk.consume(root.getStrv(i));
            }
            for (int i = 0; i < root.getDatavCount(); i++) {
                junk.consume(root.getDatav(i).toByteArray());
            }
            for (int i = 0; i < root.getF32VCount(); i++) {
                junk.consume(root.getF32V(i));
            }
            for (int i = 0; i < root.getF64VCount(); i++) {
                junk.consume(root.getF64V(i));
            }
            for (int i = 0; i < root.getFlagsCount(); i++) {
                junk.consume(root.getFlags(i));
            }
            for (int i = 0; i < root.getObjectvCount(); i++) {
                traverse(root.getObjectv(i));
            }
            junk.consume(root.getTU32());
            junk.consume(root.getTI32());
            junk.consume(root.getTS32());
            junk.consume(root.getTU64());
            junk.consume(root.getTI64());
            junk.consume(root.getTS64());

            for (Map.Entry<String, Integer> entry : root.getIndexMap().entrySet()) {
                junk.consume(entry.getKey());
                junk.consume(entry.getValue());
            }

            for (Map.Entry<Integer, com.github.peterrk.protocache.pb.Small> entry : root.getObjectsMap().entrySet()) {
                junk.consume(entry.getKey());
                traverse(entry.getValue());
            }

            traverse(root.getMatrix());

            for (int i = 0; i < root.getVectorCount(); i++) {
                traverse(root.getVector(i));
            }
            traverse(root.getArrays());
        }

        void traverse(com.github.peterrk.protocache.pb.Small root) {
            junk.consume(root.getI32());
            junk.consume(root.getFlag());
            junk.consume(root.getStr());
        }

        void traverse(com.github.peterrk.protocache.pb.ArrMap root) {
            for (Map.Entry<String, com.github.peterrk.protocache.pb.ArrMap.Array> entry : root.getXMap().entrySet()) {
                junk.consume(entry.getKey());
                com.github.peterrk.protocache.pb.ArrMap.Array vec = entry.getValue();
                for (int i = 0; i < vec.getXCount(); i++) {
                    junk.consume(vec.getX(i));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.pb.Vec2D root) {
            for (int i = 0; i < root.getXCount(); i++) {
                com.github.peterrk.protocache.pb.Vec2D.Vec1D vec = root.getX(i);
                for (int j = 0; j < vec.getXCount(); j++) {
                    junk.consume(vec.getX(j));
                }
            }
        }
    }

    @State(Scope.Thread)
    public static class FlatbuffersState {
        private final com.google.flatbuffers.ByteVector tmpByteVector = new com.google.flatbuffers.ByteVector();
        private final com.github.peterrk.protocache.fb.Bytes tmpBytes = new com.github.peterrk.protocache.fb.Bytes();
        private final com.github.peterrk.protocache.fb.Small tmpSmall = new com.github.peterrk.protocache.fb.Small();
        private final com.github.peterrk.protocache.fb.Map1Entry tmpMap1Entry = new com.github.peterrk.protocache.fb.Map1Entry();
        private final com.github.peterrk.protocache.fb.Map2Entry tmpMap2Entry = new com.github.peterrk.protocache.fb.Map2Entry();
        private final com.github.peterrk.protocache.fb.ArrMapEntry tmpArrMapEntry = new com.github.peterrk.protocache.fb.ArrMapEntry();
        private final com.github.peterrk.protocache.fb.Array tmpArray = new com.github.peterrk.protocache.fb.Array();
        private final com.github.peterrk.protocache.fb.Vec1D tmpVec1D = new com.github.peterrk.protocache.fb.Vec1D();
        private final com.github.peterrk.protocache.fb.Vec2D tmpVec2D = new com.github.peterrk.protocache.fb.Vec2D();
        private final com.github.peterrk.protocache.fb.ArrMap tmpArrMap = new com.github.peterrk.protocache.fb.ArrMap();
        Junk junk;
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = Files.readAllBytes(Paths.get("test.fb"));
            junk = new Junk();
        }

        @TearDown(value = Level.Invocation)
        public void reset() {
            //junk.print();
            junk.reset();
        }

        void consume(com.google.flatbuffers.ByteVector bytes) {
            byte[] tmp = new byte[bytes.length()];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = bytes.get(i);
            }
            junk.consume(tmp);
        }

        void traverse(com.github.peterrk.protocache.fb.Main root) {
            junk.consume(root.i32());
            junk.consume((int) root.u32());
            junk.consume(root.i64());
            junk.consume(root.u64());
            junk.consume(root.flag());
            junk.consume((int) root.mode());
            String str = root.str();
            if (str != null) {
                junk.consume(str);
            }
            com.google.flatbuffers.ByteVector dat = root.dataVector(tmpByteVector);
            if (dat != null) {
                consume(dat);
            }
            junk.consume(root.f32());
            junk.consume(root.f64());
            com.github.peterrk.protocache.fb.Small obj1 = root.object(tmpSmall);
            traverse(obj1);

            for (int i = 0; i < root.i32vLength(); i++) {
                junk.consume(root.i32v(i));
            }
            for (int i = 0; i < root.u64vLength(); i++) {
                junk.consume(root.u64v(i));
            }
            for (int i = 0; i < root.strvLength(); i++) {
                junk.consume(root.strv(i));
            }
            for (int i = 0; i < root.datavLength(); i++) {
                consume(root.datav(tmpBytes, i).aliasVector(tmpByteVector));
            }
            for (int i = 0; i < root.f32vLength(); i++) {
                junk.consume(root.f32v(i));
            }
            for (int i = 0; i < root.f64vLength(); i++) {
                junk.consume(root.f64v(i));
            }
            for (int i = 0; i < root.flagsLength(); i++) {
                junk.consume(root.flags(i));
            }
            for (int i = 0; i < root.objectvLength(); i++) {
                traverse(root.objectv(tmpSmall, i));
            }

            junk.consume((int) root.tU32());
            junk.consume(root.tI32());
            junk.consume(root.tS32());
            junk.consume(root.tU64());
            junk.consume(root.tI64());
            junk.consume(root.tS64());

            for (int i = 0; i < root.indexLength(); i++) {
                root.index(tmpMap1Entry, i);
                junk.consume(tmpMap1Entry.key());
                junk.consume(tmpMap1Entry.value());
            }

            for (int i = 0; i < root.objectsLength(); i++) {
                root.objects(tmpMap2Entry, i);
                junk.consume(tmpMap2Entry.key());
                traverse(tmpMap2Entry.value(tmpSmall));
            }

            com.github.peterrk.protocache.fb.Vec2D obj2 = root.matrix(tmpVec2D);
            if (obj2 != null) {
                traverse(obj2);
            }
            for (int i = 0; i < root.vectorLength(); i++) {
                traverse(root.vector(tmpArrMap, i));
            }
            com.github.peterrk.protocache.fb.ArrMap obj3 = root.arrays(tmpArrMap);
            if (obj3 != null) {
                traverse(obj3);
            }
        }

        void traverse(com.github.peterrk.protocache.fb.Small root) {
            junk.consume(root.i32());
            junk.consume(root.flag());
            String str = root.str();
            if (str != null) {
                junk.consume(str);
            }
        }

        void traverse(com.github.peterrk.protocache.fb.ArrMap root) {
            for (int i = 0; i < root.aliasLength(); i++) {
                root.alias(tmpArrMapEntry, i);
                junk.consume(tmpArrMapEntry.key());
                tmpArrMapEntry.value(tmpArray);
                for (int j = 0; j < tmpArray.aliasLength(); j++) {
                    junk.consume(tmpArray.alias(j));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.fb.Vec2D root) {
            for (int i = 0; i < root.aliasLength(); i++) {
                root.alias(tmpVec1D, i);
                for (int j = 0; j < tmpVec1D.aliasLength(); j++) {
                    junk.consume(tmpVec1D.alias(j));
                }
            }
        }
    }
}
