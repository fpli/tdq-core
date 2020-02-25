package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckIcfTypeUtil {

    public static int getIcfRuleType(long icfBinary, int checkLocation) {
        if (checkLocation > 64) {
            log.error("the max is 64");
        } else {
            int icfType = (icfBinary & (1 << (checkLocation - 1))) > 0 ? 1 : 0;
            return icfType;
        }
        return 0;
    }
}
