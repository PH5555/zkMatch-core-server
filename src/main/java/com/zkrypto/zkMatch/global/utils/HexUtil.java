package com.zkrypto.zkMatch.global.utils;

public class HexUtil {
    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}