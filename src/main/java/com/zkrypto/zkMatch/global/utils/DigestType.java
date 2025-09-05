package com.zkrypto.zkMatch.global.utils;

import java.util.EnumSet;

public enum DigestType {
    SHA256("sha256"),
    SHA512("sha512"),
    SHA384("sha384");

    private String rawValue;

    private DigestType(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public static DigestType fromString(String text) {
        for(DigestType b : values()) {
            if (b.rawValue.equalsIgnoreCase(text)) {
                return b;
            }
        }

        return SHA384;
    }

    public static EnumSet<DigestType> all() {
        return EnumSet.allOf(DigestType.class);
    }
}