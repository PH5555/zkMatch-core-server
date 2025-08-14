package com.zkrypto.zkMatch.global.utils;

import java.util.Base64;
import java.util.HexFormat;

public class MultiBaseUtil {
    public static String encode(byte[] source, MultiBaseType baseType) {
        String character = baseType.getCharacter();
        switch (baseType) {
            case base16:
                String enc16Data = HexFormat.of().formatHex(source);
                StringBuilder sb16 = new StringBuilder(enc16Data);
                sb16.insert(0, character);
                return new String(sb16);
            case base16upper:
                String enc16UpperData = HexFormat.of().withUpperCase().formatHex(source);
                StringBuilder sb16Upper = new StringBuilder(enc16UpperData);
                sb16Upper.insert(0, character);
                return new String(sb16Upper);
            case base58btc:
                String enc58Data = Base58.encode(source);
                StringBuilder sb58 = new StringBuilder(enc58Data);
                sb58.insert(0, character);
                return new String(sb58);
            case base64:
                String enc64Data = Base64.getEncoder().withoutPadding().encodeToString(source);
                StringBuilder sb64 = new StringBuilder(enc64Data);
                sb64.insert(0, character);
                return new String(sb64);
            case base64url:
                String enc64urlData = Base64.getUrlEncoder().withoutPadding().encodeToString(source);
                StringBuilder sb64url = new StringBuilder(enc64urlData);
                sb64url.insert(0, character);
                return new String(sb64url);
            default:
                throw new RuntimeException();
        }
    }
}
