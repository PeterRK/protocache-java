package com.github.peterrk.protocache.pc;

public final class Small extends com.github.peterrk.protocache.Message {
    public static final int FIELD_i32 = 0;
    public static final int FIELD_flag = 1;
    public static final int FIELD_str = 2;

    public Small() {
    }

    public Small(com.github.peterrk.protocache.DataView data) {
        super(data);
    }

    public int getI32() {
        return getInt32(FIELD_i32);
    }

    public boolean getFlag() {
        return getBool(FIELD_flag);
    }

    public String getStr() {
        return getStr(FIELD_str);
    }
}
