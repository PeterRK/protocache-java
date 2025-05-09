// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: test.proto
// Protobuf Java Version: 4.30.2

package com.github.peterrk.protocache.pb;

/**
 * Protobuf type {@code test.CyclicB}
 */
public final class CyclicB extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:test.CyclicB)
    CyclicBOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 30,
      /* patch= */ 2,
      /* suffix= */ "",
      CyclicB.class.getName());
  }
  // Use CyclicB.newBuilder() to construct.
  private CyclicB(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private CyclicB() {
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.github.peterrk.protocache.pb.Test.internal_static_test_CyclicB_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.github.peterrk.protocache.pb.Test.internal_static_test_CyclicB_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.github.peterrk.protocache.pb.CyclicB.class, com.github.peterrk.protocache.pb.CyclicB.Builder.class);
  }

  private int bitField0_;
  public static final int VALUE_FIELD_NUMBER = 1;
  private int value_ = 0;
  /**
   * <code>int32 value = 1;</code>
   * @return The value.
   */
  @java.lang.Override
  public int getValue() {
    return value_;
  }

  public static final int CYCLIC_FIELD_NUMBER = 2;
  private com.github.peterrk.protocache.pb.CyclicA cyclic_;
  /**
   * <code>.test.CyclicA cyclic = 2;</code>
   * @return Whether the cyclic field is set.
   */
  @java.lang.Override
  public boolean hasCyclic() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>.test.CyclicA cyclic = 2;</code>
   * @return The cyclic.
   */
  @java.lang.Override
  public com.github.peterrk.protocache.pb.CyclicA getCyclic() {
    return cyclic_ == null ? com.github.peterrk.protocache.pb.CyclicA.getDefaultInstance() : cyclic_;
  }
  /**
   * <code>.test.CyclicA cyclic = 2;</code>
   */
  @java.lang.Override
  public com.github.peterrk.protocache.pb.CyclicAOrBuilder getCyclicOrBuilder() {
    return cyclic_ == null ? com.github.peterrk.protocache.pb.CyclicA.getDefaultInstance() : cyclic_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (value_ != 0) {
      output.writeInt32(1, value_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(2, getCyclic());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (value_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, value_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getCyclic());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.github.peterrk.protocache.pb.CyclicB)) {
      return super.equals(obj);
    }
    com.github.peterrk.protocache.pb.CyclicB other = (com.github.peterrk.protocache.pb.CyclicB) obj;

    if (getValue()
        != other.getValue()) return false;
    if (hasCyclic() != other.hasCyclic()) return false;
    if (hasCyclic()) {
      if (!getCyclic()
          .equals(other.getCyclic())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + VALUE_FIELD_NUMBER;
    hash = (53 * hash) + getValue();
    if (hasCyclic()) {
      hash = (37 * hash) + CYCLIC_FIELD_NUMBER;
      hash = (53 * hash) + getCyclic().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.github.peterrk.protocache.pb.CyclicB parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.github.peterrk.protocache.pb.CyclicB parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static com.github.peterrk.protocache.pb.CyclicB parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.github.peterrk.protocache.pb.CyclicB prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code test.CyclicB}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:test.CyclicB)
      com.github.peterrk.protocache.pb.CyclicBOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.github.peterrk.protocache.pb.Test.internal_static_test_CyclicB_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.github.peterrk.protocache.pb.Test.internal_static_test_CyclicB_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.github.peterrk.protocache.pb.CyclicB.class, com.github.peterrk.protocache.pb.CyclicB.Builder.class);
    }

    // Construct using com.github.peterrk.protocache.pb.CyclicB.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage
              .alwaysUseFieldBuilders) {
        internalGetCyclicFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      value_ = 0;
      cyclic_ = null;
      if (cyclicBuilder_ != null) {
        cyclicBuilder_.dispose();
        cyclicBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.github.peterrk.protocache.pb.Test.internal_static_test_CyclicB_descriptor;
    }

    @java.lang.Override
    public com.github.peterrk.protocache.pb.CyclicB getDefaultInstanceForType() {
      return com.github.peterrk.protocache.pb.CyclicB.getDefaultInstance();
    }

    @java.lang.Override
    public com.github.peterrk.protocache.pb.CyclicB build() {
      com.github.peterrk.protocache.pb.CyclicB result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.github.peterrk.protocache.pb.CyclicB buildPartial() {
      com.github.peterrk.protocache.pb.CyclicB result = new com.github.peterrk.protocache.pb.CyclicB(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.github.peterrk.protocache.pb.CyclicB result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.value_ = value_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.cyclic_ = cyclicBuilder_ == null
            ? cyclic_
            : cyclicBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.github.peterrk.protocache.pb.CyclicB) {
        return mergeFrom((com.github.peterrk.protocache.pb.CyclicB)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.github.peterrk.protocache.pb.CyclicB other) {
      if (other == com.github.peterrk.protocache.pb.CyclicB.getDefaultInstance()) return this;
      if (other.getValue() != 0) {
        setValue(other.getValue());
      }
      if (other.hasCyclic()) {
        mergeCyclic(other.getCyclic());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              value_ = input.readInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              input.readMessage(
                  internalGetCyclicFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private int value_ ;
    /**
     * <code>int32 value = 1;</code>
     * @return The value.
     */
    @java.lang.Override
    public int getValue() {
      return value_;
    }
    /**
     * <code>int32 value = 1;</code>
     * @param value The value to set.
     * @return This builder for chaining.
     */
    public Builder setValue(int value) {

      value_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int32 value = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearValue() {
      bitField0_ = (bitField0_ & ~0x00000001);
      value_ = 0;
      onChanged();
      return this;
    }

    private com.github.peterrk.protocache.pb.CyclicA cyclic_;
    private com.google.protobuf.SingleFieldBuilder<
        com.github.peterrk.protocache.pb.CyclicA, com.github.peterrk.protocache.pb.CyclicA.Builder, com.github.peterrk.protocache.pb.CyclicAOrBuilder> cyclicBuilder_;
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     * @return Whether the cyclic field is set.
     */
    public boolean hasCyclic() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     * @return The cyclic.
     */
    public com.github.peterrk.protocache.pb.CyclicA getCyclic() {
      if (cyclicBuilder_ == null) {
        return cyclic_ == null ? com.github.peterrk.protocache.pb.CyclicA.getDefaultInstance() : cyclic_;
      } else {
        return cyclicBuilder_.getMessage();
      }
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public Builder setCyclic(com.github.peterrk.protocache.pb.CyclicA value) {
      if (cyclicBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        cyclic_ = value;
      } else {
        cyclicBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public Builder setCyclic(
        com.github.peterrk.protocache.pb.CyclicA.Builder builderForValue) {
      if (cyclicBuilder_ == null) {
        cyclic_ = builderForValue.build();
      } else {
        cyclicBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public Builder mergeCyclic(com.github.peterrk.protocache.pb.CyclicA value) {
      if (cyclicBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0) &&
          cyclic_ != null &&
          cyclic_ != com.github.peterrk.protocache.pb.CyclicA.getDefaultInstance()) {
          getCyclicBuilder().mergeFrom(value);
        } else {
          cyclic_ = value;
        }
      } else {
        cyclicBuilder_.mergeFrom(value);
      }
      if (cyclic_ != null) {
        bitField0_ |= 0x00000002;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public Builder clearCyclic() {
      bitField0_ = (bitField0_ & ~0x00000002);
      cyclic_ = null;
      if (cyclicBuilder_ != null) {
        cyclicBuilder_.dispose();
        cyclicBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public com.github.peterrk.protocache.pb.CyclicA.Builder getCyclicBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return internalGetCyclicFieldBuilder().getBuilder();
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    public com.github.peterrk.protocache.pb.CyclicAOrBuilder getCyclicOrBuilder() {
      if (cyclicBuilder_ != null) {
        return cyclicBuilder_.getMessageOrBuilder();
      } else {
        return cyclic_ == null ?
            com.github.peterrk.protocache.pb.CyclicA.getDefaultInstance() : cyclic_;
      }
    }
    /**
     * <code>.test.CyclicA cyclic = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
        com.github.peterrk.protocache.pb.CyclicA, com.github.peterrk.protocache.pb.CyclicA.Builder, com.github.peterrk.protocache.pb.CyclicAOrBuilder> 
        internalGetCyclicFieldBuilder() {
      if (cyclicBuilder_ == null) {
        cyclicBuilder_ = new com.google.protobuf.SingleFieldBuilder<
            com.github.peterrk.protocache.pb.CyclicA, com.github.peterrk.protocache.pb.CyclicA.Builder, com.github.peterrk.protocache.pb.CyclicAOrBuilder>(
                getCyclic(),
                getParentForChildren(),
                isClean());
        cyclic_ = null;
      }
      return cyclicBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:test.CyclicB)
  }

  // @@protoc_insertion_point(class_scope:test.CyclicB)
  private static final com.github.peterrk.protocache.pb.CyclicB DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.github.peterrk.protocache.pb.CyclicB();
  }

  public static com.github.peterrk.protocache.pb.CyclicB getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<CyclicB>
      PARSER = new com.google.protobuf.AbstractParser<CyclicB>() {
    @java.lang.Override
    public CyclicB parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<CyclicB> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CyclicB> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.github.peterrk.protocache.pb.CyclicB getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

