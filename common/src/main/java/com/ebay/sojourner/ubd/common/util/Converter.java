package com.ebay.sojourner.ubd.common.util;

public class Converter {

    public static String binaryToHex(String s) {
        return Long.toHexString(Long.parseLong(s, 2));
    }

    public static String hexToBinary(String s) {
        return Long.toBinaryString(Long.parseLong(s, 16));
    }

    public static long hexToDec(String s) {
        return Long.parseLong(s,16);
    }
}
