package com.demo.example.authenticator.util;


public final class CommonUtil {
    public static boolean equals(Object obj, Object obj2) {
        return (obj == null && obj2 == null) || (obj != null && obj.equals(obj2));
    }

    private CommonUtil() {
    }
}
