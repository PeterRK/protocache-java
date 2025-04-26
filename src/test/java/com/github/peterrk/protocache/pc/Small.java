package com.github.peterrk.protocache.pc;

public final class Small extends com.github.peterrk.protocache.Message {

	public static final int FIELD_i32 = 0;
	public static final int FIELD_flag = 1;
	public static final int FIELD_str = 3;

	public Small() {}
	public Small(byte[] data) { this(data, 0); }
	public Small(byte[] data, int offset) { super(data, offset); }

	public int getI32() { return fetchInt32(FIELD_i32); }
	public boolean getFlag() { return fetchBool(FIELD_flag); }
	private String _str = null;
	public String getStr() {
		if (_str == null) {
			_str = fetchString(FIELD_str);
		}
		return _str;
	}

	@Override
	public void init(byte[] data, int offset) {
		_str = null;
		super.init(data, offset);
	}}
