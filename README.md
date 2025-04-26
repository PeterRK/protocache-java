# ProtoCache Java

Alternative flat binary format for [Protobuf schema](https://protobuf.dev/programming-guides/proto3/). It' works like FlatBuffers, but it's usually smaller and surpports map. Flat means no deserialization overhead. [A benchmark](src/test/java/com/github/peterrk/protocache/AccessBenchmark.java) shows the Protobuf has considerable deserialization overhead and significant reflection overhead. FlatBuffers is fast but wastes space. ProtoCache takes balance of data size and read speed, so it's useful in data caching.

|  | Protobuf | ProtoCache | FlatBuffers | Fury | Fury-Java |
|:-------|----:|----:|----:|----:|----:|
| Data Size | 574B | 780B | 1296B | 989B | 565B |
| Compressed Size | 566B | 571B | 856B | 852B | 532B |
| Decode + Traverse | 2624ns | 902ns | 1280ns | 2646ns | 1252ns |
| Decmpress | 411ns | 626ns | 1323ns | 851ns | 443ns |

Without zero-copy technique, the Java version is slow. [Fury](https://fury.apache.org) claims better performace than Protobuf and FlatBuffers, but our benchmark shows that's not truth unless giving up cross-language compatibility.

See detail in [C++ version](https://github.com/peterrk/protocache).

## Code Gen
```sh
protoc --pcjv_out=. test.proto
```
A protobuf compiler plugin called `protoc-gen-pcjv` is [available](https://github.com/peterrk/protocache/blob/main/tools/protoc-gen-pcjv.cc) to generate java package. The generated files are short and human friendly. Don't mind to edit them if nessasery.

## Basic APIs
```java
pb.Main pb = pb.Main.parseFrom(raw);
raw = ProtoCache.serialize(pb);

pc.Main root = new pc.Main(raw);
```
Serializing a protobuf message with `ProtoCache.serialize` is the only way to create protocache binary at present. It's easy to access by wrapping the data with generated code.

## Reflection
TODO
