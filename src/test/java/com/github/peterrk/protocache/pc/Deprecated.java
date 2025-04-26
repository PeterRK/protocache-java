package com.github.peterrk.protocache.pc;

public final class Deprecated extends com.github.peterrk.protocache.Message {
	public final static class Valid extends com.github.peterrk.protocache.Message {

		public static final int FIELD_val = 0;

		public Valid() {}
		public Valid(byte[] data) { this(data, 0); }
		public Valid(byte[] data, int offset) { super(data, offset); }

		public int getVal() { return fetchInt32(FIELD_val); }
	}
}
