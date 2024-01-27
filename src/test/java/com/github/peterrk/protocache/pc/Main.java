package com.github.peterrk.protocache.pc;

public final class Main extends com.github.peterrk.protocache.Message {

	public static final int FIELD_i32 = 0;
	public static final int FIELD_u32 = 1;
	public static final int FIELD_i64 = 2;
	public static final int FIELD_u64 = 3;
	public static final int FIELD_flag = 4;
	public static final int FIELD_mode = 5;
	public static final int FIELD_str = 6;
	public static final int FIELD_data = 7;
	public static final int FIELD_f32 = 8;
	public static final int FIELD_f64 = 9;
	public static final int FIELD_object = 10;
	public static final int FIELD_i32v = 11;
	public static final int FIELD_u64v = 12;
	public static final int FIELD_strv = 13;
	public static final int FIELD_datav = 14;
	public static final int FIELD_f32v = 15;
	public static final int FIELD_f64v = 16;
	public static final int FIELD_flags = 17;
	public static final int FIELD_objectv = 18;
	public static final int FIELD_t_u32 = 19;
	public static final int FIELD_t_i32 = 20;
	public static final int FIELD_t_s32 = 21;
	public static final int FIELD_t_u64 = 22;
	public static final int FIELD_t_i64 = 23;
	public static final int FIELD_t_s64 = 24;
	public static final int FIELD_index = 25;
	public static final int FIELD_objects = 26;
	public static final int FIELD_matrix = 27;
	public static final int FIELD_vector = 28;
	public static final int FIELD_arrays = 29;

	public Main(){}
	public Main(com.github.peterrk.protocache.DataView data){ super(data); }

	public int getI32() { return getInt32(FIELD_i32); }
	public int getU32() { return getInt32(FIELD_u32); }
	public long getI64() { return getInt64(FIELD_i64); }
	public long getU64() { return getInt64(FIELD_u64); }
	public boolean getFlag() { return getBool(FIELD_flag); }
	public int getMode() { return getInt32(FIELD_mode); }
	public String getStr() { return getStr(FIELD_str); }
	public byte[] getData() { return getBytes(FIELD_data); }
	public float getF32() { return getFloat32(FIELD_f32); }
	public double getF64() { return getFloat64(FIELD_f64); }
	public com.github.peterrk.protocache.pc.Small getObject() {
		return getField(FIELD_object, com.github.peterrk.protocache.pc.Small::new);
	}
	public com.github.peterrk.protocache.Int32Array getI32V() { return getInt32Array(FIELD_i32v); }
	public com.github.peterrk.protocache.Int64Array getU64V() { return getInt64Array(FIELD_u64v); }
	public com.github.peterrk.protocache.StrArray getStrv() { return getStrArray(FIELD_strv); }
	public com.github.peterrk.protocache.BytesArray getDatav() { return getBytesArray(FIELD_datav); }
	public com.github.peterrk.protocache.Float32Array getF32V() { return getFloat32Array(FIELD_f32v); }
	public com.github.peterrk.protocache.Float64Array getF64V() { return getFloat64Array(FIELD_f64v); }
	public com.github.peterrk.protocache.BoolArray getFlags() { return getBoolArray(FIELD_flags); }
	public com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.Small> getObjectv() {
		return getField(FIELD_objectv, com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.Small>::new);
	}
	public int getTU32() { return getInt32(FIELD_t_u32); }
	public int getTI32() { return getInt32(FIELD_t_i32); }
	public int getTS32() { return getInt32(FIELD_t_s32); }
	public long getTU64() { return getInt64(FIELD_t_u64); }
	public long getTI64() { return getInt64(FIELD_t_i64); }
	public long getTS64() { return getInt64(FIELD_t_s64); }
	public com.github.peterrk.protocache.Dictionary<com.github.peterrk.protocache.Str,com.github.peterrk.protocache.Int32> getIndex() {
		return getField(FIELD_index, com.github.peterrk.protocache.Dictionary<com.github.peterrk.protocache.Str,com.github.peterrk.protocache.Int32>::new);
	}
	public com.github.peterrk.protocache.Dictionary<com.github.peterrk.protocache.Int32,com.github.peterrk.protocache.pc.Small> getObjects() {
		return getField(FIELD_objects, com.github.peterrk.protocache.Dictionary<com.github.peterrk.protocache.Int32,com.github.peterrk.protocache.pc.Small>::new);
	}
	public com.github.peterrk.protocache.pc.Vec2D getMatrix() {
		return getField(FIELD_matrix, com.github.peterrk.protocache.pc.Vec2D::new);
	}
	public com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.ArrMap> getVector() {
		return getField(FIELD_vector, com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.ArrMap>::new);
	}
	public com.github.peterrk.protocache.pc.ArrMap getArrays() {
		return getField(FIELD_arrays, com.github.peterrk.protocache.pc.ArrMap::new);
	}
}
