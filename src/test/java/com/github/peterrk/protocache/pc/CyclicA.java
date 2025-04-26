package com.github.peterrk.protocache.pc;

public final class CyclicA extends com.github.peterrk.protocache.Message {

	public static final int FIELD_value = 0;
	public static final int FIELD_cyclic = 1;

	public CyclicA() {}
	public CyclicA(byte[] data) { this(data, 0); }
	public CyclicA(byte[] data, int offset) { super(data, offset); }

	public int getValue() { return fetchInt32(FIELD_value); }
	private com.github.peterrk.protocache.pc.CyclicB _cyclic = null;
	public com.github.peterrk.protocache.pc.CyclicB getCyclic() {
		if (_cyclic == null) {
			_cyclic = fetchObject(FIELD_cyclic, new com.github.peterrk.protocache.pc.CyclicB());
		}
		return _cyclic;
	}

	@Override
	public void init(byte[] data, int offset) {
		_cyclic = null;
		super.init(data, offset);
	}}
