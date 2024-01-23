// Copyright (c) 2023, Ruan Kunliang.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.

package com.github.peterrk.protocache;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;

public class ProtoCache {
    public static byte[] Serialize(Message message) {
        Descriptors.Descriptor descriptor = message.getDescriptorForType();

        return null;
    }
}
