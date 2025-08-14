package com.zkrypto.zkMatch.api.verify.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PresentMode {
    DIRECT("Direct"),
    INDIRECT("Indirect"),
    PROXY("Proxy");

    private final String displayName;

    PresentMode(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }
}
