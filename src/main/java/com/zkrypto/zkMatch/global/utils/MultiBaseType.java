package com.zkrypto.zkMatch.global.utils;

import java.util.EnumSet;

public enum MultiBaseType {
    base16("f"),
    base16upper("F"),
    base58btc("z"),
    base64url("u"),
    base64("m");

    private String character;

    private MultiBaseType(String character) {
        this.character = character.substring(0, 1);
    }

    public String getCharacter() {
        return this.character;
    }

    public static MultiBaseType getByCharacter(String inputCharacter) {
        for(MultiBaseType value : values()) {
            if (value.character.equalsIgnoreCase(inputCharacter)) {
                return value;
            }
        }

        return base64url;
    }

    public static EnumSet<MultiBaseType> all() {
        return EnumSet.allOf(MultiBaseType.class);
    }
}
