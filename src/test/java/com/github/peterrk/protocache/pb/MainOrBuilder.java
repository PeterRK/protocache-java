// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: test.proto

package com.github.peterrk.protocache.pb;

public interface MainOrBuilder extends
    // @@protoc_insertion_point(interface_extends:test.Main)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 i32 = 1;</code>
   * @return The i32.
   */
  int getI32();

  /**
   * <code>uint32 u32 = 2;</code>
   * @return The u32.
   */
  int getU32();

  /**
   * <code>int64 i64 = 3;</code>
   * @return The i64.
   */
  long getI64();

  /**
   * <code>uint64 u64 = 4;</code>
   * @return The u64.
   */
  long getU64();

  /**
   * <code>bool flag = 5;</code>
   * @return The flag.
   */
  boolean getFlag();

  /**
   * <code>.test.Mode mode = 6;</code>
   * @return The enum numeric value on the wire for mode.
   */
  int getModeValue();
  /**
   * <code>.test.Mode mode = 6;</code>
   * @return The mode.
   */
  com.github.peterrk.protocache.pb.Mode getMode();

  /**
   * <code>string str = 7;</code>
   * @return The str.
   */
  java.lang.String getStr();
  /**
   * <code>string str = 7;</code>
   * @return The bytes for str.
   */
  com.google.protobuf.ByteString
      getStrBytes();

  /**
   * <code>bytes data = 8;</code>
   * @return The data.
   */
  com.google.protobuf.ByteString getData();

  /**
   * <code>float f32 = 9;</code>
   * @return The f32.
   */
  float getF32();

  /**
   * <code>double f64 = 10;</code>
   * @return The f64.
   */
  double getF64();

  /**
   * <code>.test.Small object = 11;</code>
   * @return Whether the object field is set.
   */
  boolean hasObject();
  /**
   * <code>.test.Small object = 11;</code>
   * @return The object.
   */
  com.github.peterrk.protocache.pb.Small getObject();
  /**
   * <code>.test.Small object = 11;</code>
   */
  com.github.peterrk.protocache.pb.SmallOrBuilder getObjectOrBuilder();

  /**
   * <code>repeated int32 i32v = 12;</code>
   * @return A list containing the i32v.
   */
  java.util.List<java.lang.Integer> getI32VList();
  /**
   * <code>repeated int32 i32v = 12;</code>
   * @return The count of i32v.
   */
  int getI32VCount();
  /**
   * <code>repeated int32 i32v = 12;</code>
   * @param index The index of the element to return.
   * @return The i32v at the given index.
   */
  int getI32V(int index);

  /**
   * <code>repeated uint64 u64v = 13;</code>
   * @return A list containing the u64v.
   */
  java.util.List<java.lang.Long> getU64VList();
  /**
   * <code>repeated uint64 u64v = 13;</code>
   * @return The count of u64v.
   */
  int getU64VCount();
  /**
   * <code>repeated uint64 u64v = 13;</code>
   * @param index The index of the element to return.
   * @return The u64v at the given index.
   */
  long getU64V(int index);

  /**
   * <code>repeated string strv = 14;</code>
   * @return A list containing the strv.
   */
  java.util.List<java.lang.String>
      getStrvList();
  /**
   * <code>repeated string strv = 14;</code>
   * @return The count of strv.
   */
  int getStrvCount();
  /**
   * <code>repeated string strv = 14;</code>
   * @param index The index of the element to return.
   * @return The strv at the given index.
   */
  java.lang.String getStrv(int index);
  /**
   * <code>repeated string strv = 14;</code>
   * @param index The index of the value to return.
   * @return The bytes of the strv at the given index.
   */
  com.google.protobuf.ByteString
      getStrvBytes(int index);

  /**
   * <code>repeated bytes datav = 15;</code>
   * @return A list containing the datav.
   */
  java.util.List<com.google.protobuf.ByteString> getDatavList();
  /**
   * <code>repeated bytes datav = 15;</code>
   * @return The count of datav.
   */
  int getDatavCount();
  /**
   * <code>repeated bytes datav = 15;</code>
   * @param index The index of the element to return.
   * @return The datav at the given index.
   */
  com.google.protobuf.ByteString getDatav(int index);

  /**
   * <code>repeated float f32v = 16;</code>
   * @return A list containing the f32v.
   */
  java.util.List<java.lang.Float> getF32VList();
  /**
   * <code>repeated float f32v = 16;</code>
   * @return The count of f32v.
   */
  int getF32VCount();
  /**
   * <code>repeated float f32v = 16;</code>
   * @param index The index of the element to return.
   * @return The f32v at the given index.
   */
  float getF32V(int index);

  /**
   * <code>repeated double f64v = 17;</code>
   * @return A list containing the f64v.
   */
  java.util.List<java.lang.Double> getF64VList();
  /**
   * <code>repeated double f64v = 17;</code>
   * @return The count of f64v.
   */
  int getF64VCount();
  /**
   * <code>repeated double f64v = 17;</code>
   * @param index The index of the element to return.
   * @return The f64v at the given index.
   */
  double getF64V(int index);

  /**
   * <code>repeated bool flags = 18;</code>
   * @return A list containing the flags.
   */
  java.util.List<java.lang.Boolean> getFlagsList();
  /**
   * <code>repeated bool flags = 18;</code>
   * @return The count of flags.
   */
  int getFlagsCount();
  /**
   * <code>repeated bool flags = 18;</code>
   * @param index The index of the element to return.
   * @return The flags at the given index.
   */
  boolean getFlags(int index);

  /**
   * <code>repeated .test.Small objectv = 19;</code>
   */
  java.util.List<com.github.peterrk.protocache.pb.Small> 
      getObjectvList();
  /**
   * <code>repeated .test.Small objectv = 19;</code>
   */
  com.github.peterrk.protocache.pb.Small getObjectv(int index);
  /**
   * <code>repeated .test.Small objectv = 19;</code>
   */
  int getObjectvCount();
  /**
   * <code>repeated .test.Small objectv = 19;</code>
   */
  java.util.List<? extends com.github.peterrk.protocache.pb.SmallOrBuilder> 
      getObjectvOrBuilderList();
  /**
   * <code>repeated .test.Small objectv = 19;</code>
   */
  com.github.peterrk.protocache.pb.SmallOrBuilder getObjectvOrBuilder(
      int index);

  /**
   * <code>fixed32 t_u32 = 20;</code>
   * @return The tU32.
   */
  int getTU32();

  /**
   * <code>sfixed32 t_i32 = 21;</code>
   * @return The tI32.
   */
  int getTI32();

  /**
   * <code>sint32 t_s32 = 22;</code>
   * @return The tS32.
   */
  int getTS32();

  /**
   * <code>fixed64 t_u64 = 23;</code>
   * @return The tU64.
   */
  long getTU64();

  /**
   * <code>sfixed64 t_i64 = 24;</code>
   * @return The tI64.
   */
  long getTI64();

  /**
   * <code>sint64 t_s64 = 25;</code>
   * @return The tS64.
   */
  long getTS64();

  /**
   * <code>map&lt;string, int32&gt; index = 26;</code>
   */
  int getIndexCount();
  /**
   * <code>map&lt;string, int32&gt; index = 26;</code>
   */
  boolean containsIndex(
      java.lang.String key);
  /**
   * Use {@link #getIndexMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.Integer>
  getIndex();
  /**
   * <code>map&lt;string, int32&gt; index = 26;</code>
   */
  java.util.Map<java.lang.String, java.lang.Integer>
  getIndexMap();
  /**
   * <code>map&lt;string, int32&gt; index = 26;</code>
   */

  int getIndexOrDefault(
      java.lang.String key,
      int defaultValue);
  /**
   * <code>map&lt;string, int32&gt; index = 26;</code>
   */

  int getIndexOrThrow(
      java.lang.String key);

  /**
   * <code>map&lt;int32, .test.Small&gt; objects = 27;</code>
   */
  int getObjectsCount();
  /**
   * <code>map&lt;int32, .test.Small&gt; objects = 27;</code>
   */
  boolean containsObjects(
      int key);
  /**
   * Use {@link #getObjectsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Integer, com.github.peterrk.protocache.pb.Small>
  getObjects();
  /**
   * <code>map&lt;int32, .test.Small&gt; objects = 27;</code>
   */
  java.util.Map<java.lang.Integer, com.github.peterrk.protocache.pb.Small>
  getObjectsMap();
  /**
   * <code>map&lt;int32, .test.Small&gt; objects = 27;</code>
   */

  com.github.peterrk.protocache.pb.Small getObjectsOrDefault(
      int key,
      com.github.peterrk.protocache.pb.Small defaultValue);
  /**
   * <code>map&lt;int32, .test.Small&gt; objects = 27;</code>
   */

  com.github.peterrk.protocache.pb.Small getObjectsOrThrow(
      int key);

  /**
   * <code>.test.Vec2D matrix = 28;</code>
   * @return Whether the matrix field is set.
   */
  boolean hasMatrix();
  /**
   * <code>.test.Vec2D matrix = 28;</code>
   * @return The matrix.
   */
  com.github.peterrk.protocache.pb.Vec2D getMatrix();
  /**
   * <code>.test.Vec2D matrix = 28;</code>
   */
  com.github.peterrk.protocache.pb.Vec2DOrBuilder getMatrixOrBuilder();

  /**
   * <code>repeated .test.ArrMap vector = 29;</code>
   */
  java.util.List<com.github.peterrk.protocache.pb.ArrMap> 
      getVectorList();
  /**
   * <code>repeated .test.ArrMap vector = 29;</code>
   */
  com.github.peterrk.protocache.pb.ArrMap getVector(int index);
  /**
   * <code>repeated .test.ArrMap vector = 29;</code>
   */
  int getVectorCount();
  /**
   * <code>repeated .test.ArrMap vector = 29;</code>
   */
  java.util.List<? extends com.github.peterrk.protocache.pb.ArrMapOrBuilder> 
      getVectorOrBuilderList();
  /**
   * <code>repeated .test.ArrMap vector = 29;</code>
   */
  com.github.peterrk.protocache.pb.ArrMapOrBuilder getVectorOrBuilder(
      int index);

  /**
   * <code>.test.ArrMap arrays = 30;</code>
   * @return Whether the arrays field is set.
   */
  boolean hasArrays();
  /**
   * <code>.test.ArrMap arrays = 30;</code>
   * @return The arrays.
   */
  com.github.peterrk.protocache.pb.ArrMap getArrays();
  /**
   * <code>.test.ArrMap arrays = 30;</code>
   */
  com.github.peterrk.protocache.pb.ArrMapOrBuilder getArraysOrBuilder();
}