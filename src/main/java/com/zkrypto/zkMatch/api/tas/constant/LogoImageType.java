package com.zkrypto.zkMatch.api.tas.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum LogoImageType {

    @SerializedName("png")
    PNG("png"),
    @SerializedName("jpg")
    JPG("jpg");

    private final String displayName;

    LogoImageType(String displayName) {
        this.displayName = displayName;
    }
    @Override
    @JsonValue
    public String toString() {
        return displayName;
    }
}