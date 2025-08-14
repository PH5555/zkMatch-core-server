package com.zkrypto.zkMatch.global.utils;

public class BaseMultibaseUtil {
    public static String encode(byte[] inputData, MultiBaseType multiBaseType) {
        try {
            return MultiBaseUtil.encode(inputData, multiBaseType);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while encoding the input data.");
        }
    }
}
