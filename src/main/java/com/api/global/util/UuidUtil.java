package com.api.global.util;

import java.util.UUID;

public class UuidUtil {

    public static String makeUuid() {
        return UUID.randomUUID().toString();
    }
}
