package com.github.peterrk.protocache;

import com.github.peterrk.protocache.pc.ArrMap;
import com.github.peterrk.protocache.pc.Small;
import org.apache.fory.Fory;
import org.apache.fory.collection.BoolList;
import org.apache.fory.collection.Float32List;
import org.apache.fory.collection.Float64List;
import org.apache.fory.collection.Int32List;
import org.apache.fory.collection.UInt64List;
import org.apache.fory.config.Language;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
public class AccessBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    interface Sink {
        void consume(int value);
        void consume(long value);
        void consume(float value);
        void consume(double value);
        void consume(boolean value);
        void consume(String value);
        void consume(byte[] value);
    }

    @State(Scope.Thread)
    public static class BlackholeSink implements Sink {
        private Blackhole blackhole;

        @Setup(Level.Trial)
        public void setup(Blackhole blackhole) {
            this.blackhole = blackhole;
        }

        public void consume(int value) { blackhole.consume(value); }
        public void consume(long value) { blackhole.consume(value); }
        public void consume(float value) { blackhole.consume(value); }
        public void consume(double value) { blackhole.consume(value); }
        public void consume(boolean value) { blackhole.consume(value); }
        public void consume(String value) { blackhole.consume(value); }
        public void consume(byte[] value) { blackhole.consume(value); }
    }

    @Benchmark
    public void traverseDummy(DummyState ctx, BlackholeSink sink) {
        ctx.traverse(ctx.root, sink);
    }

    @Benchmark
    public void traverseFory(ForyState ctx, BlackholeSink sink) {
        com.github.peterrk.protocache.fr.Main root = ctx.fory.deserialize(ctx.raw, com.github.peterrk.protocache.fr.Main.class);
        ctx.traverse(root, sink);
    }

    @Benchmark
    public void traverseForyJava(JavaForyState ctx, BlackholeSink sink) {
        com.github.peterrk.protocache.fr.Main root = ctx.fory.deserialize(ctx.raw, com.github.peterrk.protocache.fr.Main.class);
        ctx.traverse(root, sink);
    }

    @Benchmark
    public void traverseProtoCache(ProtoCacheState ctx, BlackholeSink sink) {
        com.github.peterrk.protocache.pc.Main root = new com.github.peterrk.protocache.pc.Main(ctx.raw);
        ctx.traverse(root, sink);
    }

    @Benchmark
    public void traverseProtobuf(ProtobufState ctx, BlackholeSink sink) throws IOException {
        com.github.peterrk.protocache.pb.Main root = com.github.peterrk.protocache.pb.Main.parseFrom(ctx.raw);
        ctx.traverse(root, sink);
    }

    @Benchmark
    public void traverseFlatbuffers(FlatbuffersState ctx, BlackholeSink sink) {
        com.github.peterrk.protocache.fb.Main root = com.github.peterrk.protocache.fb.Main.getRootAsMain(ByteBuffer.wrap(ctx.raw));
        ctx.traverse(root, sink);
    }

    @Benchmark
    public byte[] compress(CompressState ctx) {
        return Utils.compress(ctx.raw);
    }

    @Benchmark
    public byte[] decompress(CompressState ctx) {
        return Utils.decompress(ctx.compressed);
    }

    @State(Scope.Thread)
    public static class CompressState {
        byte[] raw;
        byte[] compressed;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = ProtoCache.serialize(TestData.protobufMain());
            compressed = Utils.compress(raw);
        }
    }

    @State(Scope.Thread)
    public static class ForyState extends ForyStateBase {
        Fory fory;

        @Setup(value = Level.Trial)
        public void setup() {
            com.github.peterrk.protocache.fr.Main root = createRoot();

            fory = Fory.builder()
                    .withXlang(true)
                    .withCompatible(true)
                    .withRefTracking(true)
                    .withCodegen(false)
                    .withModule(com.github.peterrk.protocache.fr.FrForyModule.INSTANCE)
                    .build();

            raw = fory.serialize(root);

        }
    }

    @State(Scope.Thread)
    public static class JavaForyState extends ForyStateBase {
        Fory fory;

        @Setup(value = Level.Trial)
        public void setup() {
            com.github.peterrk.protocache.fr.Main root = createRoot();

            fory = Fory.builder()
                    .withLanguage(Language.JAVA)
                    .withCompatible(false)
                    .build();
            fory.register(com.github.peterrk.protocache.fr.Mode.class);
            fory.register(com.github.peterrk.protocache.fr.Small.class);
            fory.register(com.github.peterrk.protocache.fr.Vec2D.Vec1D.class);
            fory.register(com.github.peterrk.protocache.fr.Vec2D.class);
            fory.register(com.github.peterrk.protocache.fr.ArrMap.Array.class);
            fory.register(com.github.peterrk.protocache.fr.ArrMap.class);
            fory.register(com.github.peterrk.protocache.fr.Main.class);

            raw = fory.serialize(root);

        }
    }

    @State(Scope.Thread)
    public static class DummyState extends ForyStateBase {
        com.github.peterrk.protocache.fr.Main root;

        @Setup(value = Level.Trial)
        public void setup() {
            root = createRoot();
        }
    }

    @State(Scope.Thread)
    public static class ForyStateBase {
        byte[] raw;

        void traverse(com.github.peterrk.protocache.fr.Main root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume((int) root.getU32());
            sink.consume(root.getI64());
            sink.consume(root.getU64());
            sink.consume(root.getFlag());
            if (root.getMode() != null) {
                sink.consume(root.getMode().getId());
            }
            if (root.getStr() != null) {
                sink.consume(root.getStr());
            }
            if (root.getData() != null) {
                sink.consume(root.getData());
            }
            sink.consume(root.getF32());
            sink.consume(root.getF64());
            if (root.getObject() != null) {
                traverse(root.getObject(), sink);
            }
            if (root.getI32v() != null) {
                Int32List i32v = root.getI32v();
                for (int i = 0; i < i32v.size(); i++) {
                    sink.consume(i32v.getInt(i));
                }
            }
            if (root.getU64v() != null) {
                UInt64List u64v = root.getU64v();
                for (int i = 0; i < u64v.size(); i++) {
                    sink.consume(u64v.getLong(i));
                }
            }
            if (root.getStrv() != null) {
                for (String v : root.getStrv()) {
                    sink.consume(v);
                }
            }
            if (root.getDatav() != null) {
                for (byte[] v : root.getDatav()) {
                    sink.consume(v);
                }
            }
            if (root.getF32v() != null) {
                Float32List f32v = root.getF32v();
                for (int i = 0; i < f32v.size(); i++) {
                    sink.consume(f32v.getFloat(i));
                }
            }
            if (root.getF64v() != null) {
                Float64List f64v = root.getF64v();
                for (int i = 0; i < f64v.size(); i++) {
                    sink.consume(f64v.getDouble(i));
                }
            }
            if (root.getFlags() != null) {
                BoolList flags = root.getFlags();
                for (int i = 0; i < flags.size(); i++) {
                    sink.consume(flags.getBoolean(i));
                }
            }
            if (root.getObjectv() != null) {
                for (com.github.peterrk.protocache.fr.Small v : root.getObjectv()) {
                    traverse(v, sink);
                }
            }
            sink.consume((int) root.getTU32());
            sink.consume(root.getTI32());
            sink.consume(root.getTS32());
            sink.consume(root.getTU64());
            sink.consume(root.getTI64());
            sink.consume(root.getTS64());

            if (root.getIndex() != null) {
                for (Map.Entry<String, Integer> entry : root.getIndex().entrySet()) {
                    sink.consume(entry.getKey());
                    sink.consume(entry.getValue());
                }
            }

            if (root.getObjects() != null) {
                for (Map.Entry<Integer, com.github.peterrk.protocache.fr.Small> entry : root.getObjects().entrySet()) {
                    sink.consume(entry.getKey());
                    traverse(entry.getValue(), sink);
                }
            }

            if (root.getMatrix() != null) {
                traverse(root.getMatrix(), sink);
            }

            if (root.getVector() != null) {
                for (com.github.peterrk.protocache.fr.ArrMap u : root.getVector()) {
                    traverse(u, sink);
                }
            }
            if (root.getArrays() != null) {
                traverse(root.getArrays(), sink);
            }
        }

        void traverse(com.github.peterrk.protocache.fr.Small root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume(root.getFlag());
            if (root.getStr() != null) {
                sink.consume(root.getStr());
            }
        }

        void traverse(com.github.peterrk.protocache.fr.Vec2D root, Sink sink) {
            for (com.github.peterrk.protocache.fr.Vec2D.Vec1D u : root.getX()) {
                Float32List x = u.getX();
                for (int i = 0; i < x.size(); i++) {
                    sink.consume(x.getFloat(i));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.fr.ArrMap root, Sink sink) {
            for (Map.Entry<String, com.github.peterrk.protocache.fr.ArrMap.Array> entry : root.getX().entrySet()) {
                sink.consume(entry.getKey());
                Float32List x = entry.getValue().getX();
                for (int i = 0; i < x.size(); i++) {
                    sink.consume(x.getFloat(i));
                }
            }
        }

        static com.github.peterrk.protocache.fr.Main createRoot() {
            com.github.peterrk.protocache.fr.Main root = new com.github.peterrk.protocache.fr.Main();
            root.setI32(-999);
            root.setU32(1234);
            root.setI64(-9876543210L);
            root.setU64(98765432123456789L);
            root.setFlag(true);
            root.setMode(com.github.peterrk.protocache.fr.Mode.C);
            root.setStr("Hello World!");
            root.setData("abc123!?$*&()'-=@~".getBytes(StandardCharsets.UTF_8));
            root.setF32(-2.1f);
            root.setF64(1.0);
            root.setObject(createSmall(88, false, "tmp"));
            root.setI32v(new Int32List(new int[]{1, 2}));
            root.setU64v(new UInt64List(new long[]{12345678987654321L}));
            root.setStrv(listOf(
                    "abc", "apple", "banana", "orange", "pear",
                    "grape", "strawberry", "cherry", "mango", "watermelon"));
            root.setDatav(new ArrayList<>());
            root.setF32v(new Float32List(new float[]{1.1f, 2.2f}));
            root.setF64v(new Float64List(new double[]{9.9, 8.8, 7.7, 6.6, 5.5}));
            root.setFlags(new BoolList(new boolean[]{true, true, false, true, false, false, false}));
            root.setObjectv(listOf(
                    createSmall(1, false, ""),
                    createSmall(0, true, ""),
                    createSmall(0, false, "good luck!")));
            root.setTU32(0);
            root.setTI32(0);
            root.setTS32(0);
            root.setTU64(0);
            root.setTI64(0);
            root.setTS64(0);

            Map<String, Integer> index = new LinkedHashMap<>();
            index.put("abc-1", 1);
            index.put("abc-2", 2);
            index.put("x-1", 1);
            index.put("x-2", 2);
            index.put("x-3", 3);
            index.put("x-4", 4);
            root.setIndex(index);

            Map<Integer, com.github.peterrk.protocache.fr.Small> objects = new LinkedHashMap<>();
            objects.put(1, createSmall(1, false, "aaaaaaaaaaa"));
            objects.put(2, createSmall(2, false, "b"));
            objects.put(3, createSmall(3, false, "ccccccccccccccc"));
            objects.put(4, createSmall(4, false, "ddddd"));
            root.setObjects(objects);

            com.github.peterrk.protocache.fr.Vec2D matrix = new com.github.peterrk.protocache.fr.Vec2D();
            matrix.setX(listOf(
                    createVec1D(1, 2, 3),
                    createVec1D(4, 5, 6),
                    createVec1D(7, 8, 9)));
            root.setMatrix(matrix);
            root.setVector(listOf(
                    createArrMap(
                            arrayEntry("lv1", 11, 12),
                            arrayEntry("lv2", 21, 22)),
                    createArrMap(arrayEntry("lv3", 31, 32))));
            root.setArrays(createArrMap(
                    arrayEntry("lv4", 41, 42),
                    arrayEntry("lv5", 51, 52)));
            return root;
        }

        private static com.github.peterrk.protocache.fr.Small createSmall(int i32, boolean flag, String str) {
            com.github.peterrk.protocache.fr.Small small = new com.github.peterrk.protocache.fr.Small();
            small.setI32(i32);
            small.setFlag(flag);
            small.setStr(str);
            return small;
        }

        private static com.github.peterrk.protocache.fr.Vec2D.Vec1D createVec1D(float... values) {
            com.github.peterrk.protocache.fr.Vec2D.Vec1D vec = new com.github.peterrk.protocache.fr.Vec2D.Vec1D();
            vec.setX(new Float32List(values));
            return vec;
        }

        @SafeVarargs
        private static com.github.peterrk.protocache.fr.ArrMap createArrMap(
                Map.Entry<String, com.github.peterrk.protocache.fr.ArrMap.Array>... entries) {
            Map<String, com.github.peterrk.protocache.fr.ArrMap.Array> map = new LinkedHashMap<>();
            for (Map.Entry<String, com.github.peterrk.protocache.fr.ArrMap.Array> entry : entries) {
                map.put(entry.getKey(), entry.getValue());
            }
            com.github.peterrk.protocache.fr.ArrMap arrMap = new com.github.peterrk.protocache.fr.ArrMap();
            arrMap.setX(map);
            return arrMap;
        }

        private static Map.Entry<String, com.github.peterrk.protocache.fr.ArrMap.Array> arrayEntry(String key, float... values) {
            com.github.peterrk.protocache.fr.ArrMap.Array array = new com.github.peterrk.protocache.fr.ArrMap.Array();
            array.setX(new Float32List(values));
            return new AbstractMap.SimpleImmutableEntry<>(key, array);
        }

        @SafeVarargs
        private static <T> List<T> listOf(T... values) {
            ArrayList<T> result = new ArrayList<>(values.length);
            for (T value : values) {
                result.add(value);
            }
            return result;
        }
    }

    @State(Scope.Thread)
    public static class ProtoCacheState {
        private final com.github.peterrk.protocache.pc.Small tmpSmall = new com.github.peterrk.protocache.pc.Small();
        private final com.github.peterrk.protocache.pc.ArrMap tmpArrMap = new com.github.peterrk.protocache.pc.ArrMap();
        private final com.github.peterrk.protocache.pc.ArrMap.Array tmpArrMapArray = new com.github.peterrk.protocache.pc.ArrMap.Array();
        private final com.github.peterrk.protocache.pc.Vec2D.Vec1D tmpVec2DVec1D = new com.github.peterrk.protocache.pc.Vec2D.Vec1D();
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = ProtoCache.serialize(TestData.protobufMain());
        }

        void traverse(com.github.peterrk.protocache.pc.Main root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume(root.getU32());
            sink.consume(root.getI64());
            sink.consume(root.getU64());
            sink.consume(root.getFlag());
            sink.consume(root.getMode());
            sink.consume(root.getStr());
            sink.consume(root.getData());
            sink.consume(root.getF32());
            sink.consume(root.getF64());
            traverse(root.getObject(), sink);
            Int32Array i32v = root.getI32V();
            for (int i = 0; i < i32v.size(); i++) {
                sink.consume(i32v.get(i));
            }
            Int64Array u64v = root.getU64V();
            for (int i = 0; i < u64v.size(); i++) {
                sink.consume(u64v.get(i));
            }
            StringArray strv = root.getStrv();
            for (int i = 0; i < strv.size(); i++) {
                sink.consume(strv.get(i));
            }
            BytesArray datv = root.getDatav();
            for (int i = 0; i < datv.size(); i++) {
                sink.consume(datv.get(i));
            }
            Float32Array f32v = root.getF32V();
            for (int i = 0; i < f32v.size(); i++) {
                sink.consume(f32v.get(i));
            }
            Float64Array f64v = root.getF64V();
            for (int i = 0; i < f64v.size(); i++) {
                sink.consume(f64v.get(i));
            }
            BoolArray flags = root.getFlags();
            for (int i = 0; i < flags.size(); i++) {
                sink.consume(flags.get(i));
            }
            ObjectArray<Small> objv = root.getObjectv();
            for (int i = 0; i < objv.size(); i++) {
                traverse(objv.get(i, tmpSmall), sink);
            }

            sink.consume(root.getTU32());
            sink.consume(root.getTI32());
            sink.consume(root.getTS32());
            sink.consume(root.getTU64());
            sink.consume(root.getTI64());
            sink.consume(root.getTS64());

            StringDict.Int32Value map1 = root.getIndex();
            for (int i = 0; i < map1.size(); i++) {
                sink.consume(map1.getKey(i));
                sink.consume(map1.getValue(i));
            }

            Int32Dict.ObjectValue<com.github.peterrk.protocache.pc.Small> map2 = root.getObjects();
            for (int i = 0; i < map2.size(); i++) {
                sink.consume(map2.getKey(i));
                traverse(map2.getValue(i, tmpSmall), sink);
            }

            traverse(root.getMatrix(), sink);

            ObjectArray<com.github.peterrk.protocache.pc.ArrMap> vec = root.getVector();
            for (int i = 0; i < vec.size(); i++) {
                traverse(vec.get(i, tmpArrMap), sink);
            }
            traverse(root.getArrays(), sink);
        }

        void traverse(com.github.peterrk.protocache.pc.Small root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume(root.getFlag());
            sink.consume(root.getStr());
        }

        void traverse(com.github.peterrk.protocache.pc.ArrMap map, Sink sink) {
            for (int i = 0; i < map.size(); i++) {
                sink.consume(map.getKey(i));
                map.getValue(i, tmpArrMapArray);
                for (int j = 0; j < tmpArrMapArray.size(); j++) {
                    sink.consume(tmpArrMapArray.get(j));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.pc.Vec2D vec, Sink sink) {
            for (int i = 0; i < vec.size(); i++) {
                vec.get(i, tmpVec2DVec1D);
                for (int j = 0; j < tmpVec2DVec1D.size(); j++) {
                    sink.consume(tmpVec2DVec1D.get(j));
                }
            }
        }
    }

    @State(Scope.Thread)
    public static class ProtobufState {
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            raw = TestData.protobufMain().toByteArray();
        }

        void traverse(com.github.peterrk.protocache.pb.Main root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume(root.getU32());
            sink.consume(root.getI64());
            sink.consume(root.getU64());
            sink.consume(root.getFlag());
            sink.consume(root.getMode().getNumber());
            sink.consume(root.getStr());
            sink.consume(root.getData().toByteArray());
            sink.consume(root.getF32());
            sink.consume(root.getF64());
            traverse(root.getObject(), sink);
            for (int i = 0; i < root.getI32VCount(); i++) {
                sink.consume(root.getI32V(i));
            }
            for (int i = 0; i < root.getU64VCount(); i++) {
                sink.consume(root.getU64V(i));
            }
            for (int i = 0; i < root.getStrvCount(); i++) {
                sink.consume(root.getStrv(i));
            }
            for (int i = 0; i < root.getDatavCount(); i++) {
                sink.consume(root.getDatav(i).toByteArray());
            }
            for (int i = 0; i < root.getF32VCount(); i++) {
                sink.consume(root.getF32V(i));
            }
            for (int i = 0; i < root.getF64VCount(); i++) {
                sink.consume(root.getF64V(i));
            }
            for (int i = 0; i < root.getFlagsCount(); i++) {
                sink.consume(root.getFlags(i));
            }
            for (int i = 0; i < root.getObjectvCount(); i++) {
                traverse(root.getObjectv(i), sink);
            }
            sink.consume(root.getTU32());
            sink.consume(root.getTI32());
            sink.consume(root.getTS32());
            sink.consume(root.getTU64());
            sink.consume(root.getTI64());
            sink.consume(root.getTS64());

            for (Map.Entry<String, Integer> entry : root.getIndexMap().entrySet()) {
                sink.consume(entry.getKey());
                sink.consume(entry.getValue());
            }

            for (Map.Entry<Integer, com.github.peterrk.protocache.pb.Small> entry : root.getObjectsMap().entrySet()) {
                sink.consume(entry.getKey());
                traverse(entry.getValue(), sink);
            }

            traverse(root.getMatrix(), sink);

            for (int i = 0; i < root.getVectorCount(); i++) {
                traverse(root.getVector(i), sink);
            }
            traverse(root.getArrays(), sink);
        }

        void traverse(com.github.peterrk.protocache.pb.Small root, Sink sink) {
            sink.consume(root.getI32());
            sink.consume(root.getFlag());
            sink.consume(root.getStr());
        }

        void traverse(com.github.peterrk.protocache.pb.ArrMap root, Sink sink) {
            for (Map.Entry<String, com.github.peterrk.protocache.pb.ArrMap.Array> entry : root.getXMap().entrySet()) {
                sink.consume(entry.getKey());
                com.github.peterrk.protocache.pb.ArrMap.Array vec = entry.getValue();
                for (int i = 0; i < vec.getXCount(); i++) {
                    sink.consume(vec.getX(i));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.pb.Vec2D root, Sink sink) {
            for (int i = 0; i < root.getXCount(); i++) {
                com.github.peterrk.protocache.pb.Vec2D.Vec1D vec = root.getX(i);
                for (int j = 0; j < vec.getXCount(); j++) {
                    sink.consume(vec.getX(j));
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
        byte[] raw;

        @Setup(value = Level.Trial)
        public void setup() throws IOException {
            // Generate test.fb with FlatbuffersFixture.GENERATION_COMMANDS.
            raw = FlatbuffersFixture.read();
        }

        void consume(com.google.flatbuffers.ByteVector bytes, Sink sink) {
            byte[] tmp = new byte[bytes.length()];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = bytes.get(i);
            }
            sink.consume(tmp);
        }

        void traverse(com.github.peterrk.protocache.fb.Main root, Sink sink) {
            sink.consume(root.i32());
            sink.consume((int) root.u32());
            sink.consume(root.i64());
            sink.consume(root.u64());
            sink.consume(root.flag());
            sink.consume((int) root.mode());
            String str = root.str();
            if (str != null) {
                sink.consume(str);
            }
            com.google.flatbuffers.ByteVector dat = root.dataVector(tmpByteVector);
            if (dat != null) {
                consume(dat, sink);
            }
            sink.consume(root.f32());
            sink.consume(root.f64());
            com.github.peterrk.protocache.fb.Small obj1 = root.object(tmpSmall);
            traverse(obj1, sink);

            for (int i = 0; i < root.i32vLength(); i++) {
                sink.consume(root.i32v(i));
            }
            for (int i = 0; i < root.u64vLength(); i++) {
                sink.consume(root.u64v(i));
            }
            for (int i = 0; i < root.strvLength(); i++) {
                sink.consume(root.strv(i));
            }
            for (int i = 0; i < root.datavLength(); i++) {
                consume(root.datav(tmpBytes, i).aliasVector(tmpByteVector), sink);
            }
            for (int i = 0; i < root.f32vLength(); i++) {
                sink.consume(root.f32v(i));
            }
            for (int i = 0; i < root.f64vLength(); i++) {
                sink.consume(root.f64v(i));
            }
            for (int i = 0; i < root.flagsLength(); i++) {
                sink.consume(root.flags(i));
            }
            for (int i = 0; i < root.objectvLength(); i++) {
                traverse(root.objectv(tmpSmall, i), sink);
            }

            sink.consume((int) root.tU32());
            sink.consume(root.tI32());
            sink.consume(root.tS32());
            sink.consume(root.tU64());
            sink.consume(root.tI64());
            sink.consume(root.tS64());

            for (int i = 0; i < root.indexLength(); i++) {
                root.index(tmpMap1Entry, i);
                sink.consume(tmpMap1Entry.key());
                sink.consume(tmpMap1Entry.value());
            }

            for (int i = 0; i < root.objectsLength(); i++) {
                root.objects(tmpMap2Entry, i);
                sink.consume(tmpMap2Entry.key());
                traverse(tmpMap2Entry.value(tmpSmall), sink);
            }

            com.github.peterrk.protocache.fb.Vec2D obj2 = root.matrix(tmpVec2D);
            if (obj2 != null) {
                traverse(obj2, sink);
            }
            for (int i = 0; i < root.vectorLength(); i++) {
                traverse(root.vector(tmpArrMap, i), sink);
            }
            com.github.peterrk.protocache.fb.ArrMap obj3 = root.arrays(tmpArrMap);
            if (obj3 != null) {
                traverse(obj3, sink);
            }
        }

        void traverse(com.github.peterrk.protocache.fb.Small root, Sink sink) {
            sink.consume(root.i32());
            sink.consume(root.flag());
            String str = root.str();
            if (str != null) {
                sink.consume(str);
            }
        }

        void traverse(com.github.peterrk.protocache.fb.ArrMap root, Sink sink) {
            for (int i = 0; i < root.aliasLength(); i++) {
                root.alias(tmpArrMapEntry, i);
                sink.consume(tmpArrMapEntry.key());
                tmpArrMapEntry.value(tmpArray);
                for (int j = 0; j < tmpArray.aliasLength(); j++) {
                    sink.consume(tmpArray.alias(j));
                }
            }
        }

        void traverse(com.github.peterrk.protocache.fb.Vec2D root, Sink sink) {
            for (int i = 0; i < root.aliasLength(); i++) {
                root.alias(tmpVec1D, i);
                for (int j = 0; j < tmpVec1D.aliasLength(); j++) {
                    sink.consume(tmpVec1D.alias(j));
                }
            }
        }
    }
}
