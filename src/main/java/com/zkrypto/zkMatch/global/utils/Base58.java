package com.zkrypto.zkMatch.global.utils;

import java.nio.charset.StandardCharsets;

public class Base58 {
    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final int[] INDEXES = new int[128];

    public static String encode(byte[] input) {
        if (input.length == 0) {
            return "";
        } else {
            input = copyOfRange(input, 0, input.length);

            int zeroCount;
            for(zeroCount = 0; zeroCount < input.length && input[zeroCount] == 0; ++zeroCount) {
            }

            byte[] temp = new byte[input.length * 2];
            int j = temp.length;

            byte mod;
            for(int startAt = zeroCount; startAt < input.length; temp[j] = (byte)ALPHABET[mod]) {
                mod = divmod(input, startAt, 58);
                if (input[startAt] == 0) {
                    ++startAt;
                }

                --j;
            }

            while(j < temp.length && temp[j] == ALPHABET[0]) {
                ++j;
            }

            while(zeroCount-- > 0) {
                --j;
                temp[j] = (byte)ALPHABET[0];
            }

            return new String(temp, j, temp.length - j, StandardCharsets.US_ASCII);
        }
    }

    public static byte[] decode(String input) {
        if (input.length() == 0) {
            return new byte[0];
        } else {
            byte[] input58 = new byte[input.length()];

            for(int i = 0; i < input.length(); ++i) {
                char c = input.charAt(i);
                int digit58 = c < 128 ? INDEXES[c] : -1;
                if (digit58 < 0) {
                    return null;
                }

                input58[i] = (byte)digit58;
            }

            int zeroCount;
            for(zeroCount = 0; zeroCount < input58.length && input58[zeroCount] == 0; ++zeroCount) {
            }

            byte[] temp = new byte[input.length()];
            int j = temp.length;

            byte mod;
            for(int startAt = zeroCount; startAt < input58.length; temp[j] = mod) {
                mod = divmod(input58, startAt, 256);
                if (input58[startAt] == 0) {
                    ++startAt;
                }

                --j;
            }

            while(j < temp.length && temp[j] == 0) {
                ++j;
            }

            return copyOfRange(temp, j - zeroCount, temp.length);
        }
    }

    private static byte divmod(byte[] number, int startAt, int base) {
        int remainder = 0;

        for(int i = startAt; i < number.length; ++i) {
            int digit = number[i] & 255;
            int temp = (base == 58 ? remainder * 256 : remainder * 58) + digit;
            number[i] = (byte)(temp / base);
            remainder = temp % base;
        }

        return (byte)remainder;
    }

    private static byte[] copyOfRange(byte[] source, int from, int to) {
        byte[] range = new byte[to - from];
        System.arraycopy(source, from, range, 0, range.length);
        return range;
    }

    static {
        for(int i = 0; i < INDEXES.length; ++i) {
            INDEXES[i] = -1;
        }

        for(int i = 0; i < ALPHABET.length; INDEXES[ALPHABET[i]] = i++) {
        }

    }
}
