package com.github.peterrk.protocache.pc;

import com.github.peterrk.protocache.Dictionary;
import com.github.peterrk.protocache.Float32Array;
import com.github.peterrk.protocache.Str;

public final class ArrMap extends Dictionary<Str, ArrMap.Array> {

    public final static class Array extends Float32Array {
    }
}
