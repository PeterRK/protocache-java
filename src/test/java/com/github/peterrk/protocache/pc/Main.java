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
	public static final int FIELD_modev = 31;

	public Main() {}
	public Main(byte[] data) { this(data, 0); }
	public Main(byte[] data, int offset) { super(data, offset); }

	public int getI32() { return getInt32(FIELD_i32); }
	public int getU32() { return getInt32(FIELD_u32); }
	public long getI64() { return getInt64(FIELD_i64); }
	public long getU64() { return getInt64(FIELD_u64); }
	public boolean getFlag() { return getBool(FIELD_flag); }
	public int getMode() { return getInt32(FIELD_mode); }
	private String _str = null;
	public String getStr() {
		if (_str == null) {
			_str = getStr(FIELD_str);
		}
		return _str;
	}
	private byte[] _data = null;
	public byte[] getData() {
		if (_data == null) {
			_data = getBytes(FIELD_data);
		}
		return _data;
	}
	public float getF32() { return getFloat32(FIELD_f32); }
	public double getF64() { return getFloat64(FIELD_f64); }
	private com.github.peterrk.protocache.pc.Small _object = null;
	public com.github.peterrk.protocache.pc.Small getObject() {
		if (_object == null) {
			_object = getField(FIELD_object, com.github.peterrk.protocache.pc.Small::new);
		}
		return _object;
	}
	private com.github.peterrk.protocache.Int32Array _i32v = null;
	public com.github.peterrk.protocache.Int32Array getI32V() {
		if (_i32v == null) {
			_i32v = getInt32Array(FIELD_i32v);
		}
		return _i32v;
	}
	private com.github.peterrk.protocache.Int64Array _u64v = null;
	public com.github.peterrk.protocache.Int64Array getU64V() {
		if (_u64v == null) {
			_u64v = getInt64Array(FIELD_u64v);
		}
		return _u64v;
	}
	private com.github.peterrk.protocache.StrArray _strv = null;
	public com.github.peterrk.protocache.StrArray getStrv() {
		if (_strv == null) {
			_strv = getStrArray(FIELD_strv);
		}
		return _strv;
	}
	private com.github.peterrk.protocache.BytesArray _datav = null;
	public com.github.peterrk.protocache.BytesArray getDatav() {
		if (_datav == null) {
			_datav = getBytesArray(FIELD_datav);
		}
		return _datav;
	}
	private com.github.peterrk.protocache.Float32Array _f32v = null;
	public com.github.peterrk.protocache.Float32Array getF32V() {
		if (_f32v == null) {
			_f32v = getFloat32Array(FIELD_f32v);
		}
		return _f32v;
	}
	private com.github.peterrk.protocache.Float64Array _f64v = null;
	public com.github.peterrk.protocache.Float64Array getF64V() {
		if (_f64v == null) {
			_f64v = getFloat64Array(FIELD_f64v);
		}
		return _f64v;
	}
	private com.github.peterrk.protocache.BoolArray _flags = null;
	public com.github.peterrk.protocache.BoolArray getFlags() {
		if (_flags == null) {
			_flags = getBoolArray(FIELD_flags);
		}
		return _flags;
	}
	private com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.Small> _objectv = null;
	public com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.Small> getObjectv() {
		if (_objectv == null) {
			_objectv = getField(FIELD_objectv, com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.Small>::new);
		}
		return _objectv;
	}
	public int getTU32() { return getInt32(FIELD_t_u32); }
	public int getTI32() { return getInt32(FIELD_t_i32); }
	public int getTS32() { return getInt32(FIELD_t_s32); }
	public long getTU64() { return getInt64(FIELD_t_u64); }
	public long getTI64() { return getInt64(FIELD_t_i64); }
	public long getTS64() { return getInt64(FIELD_t_s64); }
	private com.github.peterrk.protocache.StrDict<com.github.peterrk.protocache.Int32> _index = null;
	public com.github.peterrk.protocache.StrDict<com.github.peterrk.protocache.Int32> getIndex() {
		if (_index == null) {
			_index = getField(FIELD_index, com.github.peterrk.protocache.StrDict<com.github.peterrk.protocache.Int32>::new);
		}
		return _index;
	}
	private com.github.peterrk.protocache.Int32Dict<com.github.peterrk.protocache.pc.Small> _objects = null;
	public com.github.peterrk.protocache.Int32Dict<com.github.peterrk.protocache.pc.Small> getObjects() {
		if (_objects == null) {
			_objects = getField(FIELD_objects, com.github.peterrk.protocache.Int32Dict<com.github.peterrk.protocache.pc.Small>::new);
		}
		return _objects;
	}
	private com.github.peterrk.protocache.pc.Vec2D _matrix = null;
	public com.github.peterrk.protocache.pc.Vec2D getMatrix() {
		if (_matrix == null) {
			_matrix = getField(FIELD_matrix, com.github.peterrk.protocache.pc.Vec2D::new);
		}
		return _matrix;
	}
	private com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.ArrMap> _vector = null;
	public com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.ArrMap> getVector() {
		if (_vector == null) {
			_vector = getField(FIELD_vector, com.github.peterrk.protocache.Array<com.github.peterrk.protocache.pc.ArrMap>::new);
		}
		return _vector;
	}
	private com.github.peterrk.protocache.pc.ArrMap _arrays = null;
	public com.github.peterrk.protocache.pc.ArrMap getArrays() {
		if (_arrays == null) {
			_arrays = getField(FIELD_arrays, com.github.peterrk.protocache.pc.ArrMap::new);
		}
		return _arrays;
	}
	private com.github.peterrk.protocache.Int32Array _modev = null;
	public com.github.peterrk.protocache.Int32Array getModev() {
		if (_modev == null) {
			_modev = getInt32Array(FIELD_modev);
		}
		return _modev;
	}

	@Override
	public void init(byte[] data, int offset) {
		_str = null;
		_data = null;
		_object = null;
		_i32v = null;
		_u64v = null;
		_strv = null;
		_datav = null;
		_f32v = null;
		_f64v = null;
		_flags = null;
		_objectv = null;
		_index = null;
		_objects = null;
		_matrix = null;
		_vector = null;
		_arrays = null;
		_modev = null;
		super.init(data, offset);
	}}
