# ProtoCache Java

Alternative flat binary format for [Protobuf schema](https://protobuf.dev/programming-guides/proto3/). It' works like FlatBuffers, but it's usually smaller and surpports map. Flat means no deserialization overhead. [A benchmark](test/benchmark) shows the Protobuf has considerable deserialization overhead and significant reflection overhead. FlatBuffers is fast but wastes space. ProtoCache takes balance of data size and read speed, so it's useful in data caching.

See detail in [C++ version](https://github.com/peterrk/protocache).

## Code Gen
```sh
protoc --pcjv_out=. test.proto
```
A protobuf compiler plugin called `protoc-gen-pcjv` is [available](https://github.com/peterrk/protocache/blob/main/tools/protoc-gen-pcjv.cc) to generate java package. The generated files are short and human friendly. Don't mind to edit them if nessasery.

## Basic APIs
```go
pb.Main pb = pb.Main.parseFrom(raw);
raw = ProtoCache.serialize(pb);

pc.Main root = new pc.Main(new DataView(raw));
```
Serializing a protobuf message with protocache.Serialize is the only way to create protocache binary at present. It's easy to access by wrapping the data with generated code.

## Reflection
TODO