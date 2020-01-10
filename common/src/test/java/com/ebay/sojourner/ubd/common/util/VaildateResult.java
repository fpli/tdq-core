package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VaildateResult {

    public static Boolean validateString(Object expectResult, String str) {

        if (TypeTransUtil.ObjectToString(expectResult).equals(str)) {
            return true;
        } else {
            log.warn("Expect: {}, but actual value is: {}", expectResult, str);
            return false;
        }
    }

    public static Boolean validateInteger(Object expectResult, Integer integer) {

        if (TypeTransUtil.ObjectToInteger(expectResult).equals(integer)) {
            return true;
        } else {
            return false;
        }
    }
}
