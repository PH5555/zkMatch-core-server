package com.zkrypto.zkMatch.global.utils;

import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BaseDigestUtil {
    public static byte[] generateHash(byte[] input) {
        return generateHash(input, DigestType.SHA256);
    }

    public static byte[] generateHash(String input) throws NoSuchAlgorithmException {
        return generateHash(input.getBytes(StandardCharsets.UTF_8), DigestType.SHA256);
    }

    public static byte[] generateHash(byte[] input, DigestType digestType)  {
        try {
            return getDigest(input, digestType);
        } catch (CustomException e) {
            throw new RuntimeException("Error occurred while generating hash: " + e.getMessage());
        }
    }

    public static byte[] getDigest(byte[] source, DigestType digestType) throws CustomException {
        if (source != null && digestType != null) {
            switch (digestType) {
                case SHA256 -> {
                    return getShaDigest(source, "SHA-256");
                }
                case SHA384 -> {
                    return getShaDigest(source, "SHA-384");
                }
                case SHA512 -> {
                    return getShaDigest(source, "SHA-512");
                }
                default -> throw new CustomException(ErrorCode.CRYPTO_VC_ERROR);
            }
        } else {
            throw new CustomException(ErrorCode.CRYPTO_VC_ERROR);
        }
    }

    private static byte[] getShaDigest(byte[] source, String algorithm) throws CustomException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(source);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorCode.CRYPTO_VC_ERROR);
        }
    }
}