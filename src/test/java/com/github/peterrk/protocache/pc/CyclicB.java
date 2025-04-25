package com.github.peterrk.protocache.pc;

public final class CyclicB extends com.github.peterrk.protocache.Message {

	public static final int FIELD_value = 0;
	public static final int FIELD_cyclic = 1;

	public CyclicB() {}
	public CyclicB(byte[] data) { this(data, 0); }
	public CyclicB(byte[] data, int offset) { super(data, offset); }

	public int getValue() { return getInt32(FIELD_value); }
	private com.github.peterrk.protocache.pc.CyclicA _cyclic = null;
	public com.github.peterrk.protocache.pc.CyclicA getCyclic() {
		if (_cyclic == null) {
			_cyclic = getField(FIELD_cyclic, com.github.peterrk.protocache.pc.CyclicA::new);
		}
		return _cyclic;
	}

	@Override
	public void init(byte[] data, int offset) {
		_cyclic = null;
		super.init(data, offset);
	}}
