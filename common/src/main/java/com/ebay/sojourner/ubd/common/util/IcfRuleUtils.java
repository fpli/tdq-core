package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IcfRuleUtils {

    public static int getIcfRuleType(long icfBinary, int checkLocation) {
        if (checkLocation > 64) {
            log.error("the max is 64");
        } else {
            int icfType = (icfBinary & (1 << (checkLocation - 1))) > 0 ? 1 : 0;
            if (icfType == 1) {
                if (checkLocation < 10 && checkLocation >= 0) {
                    return Integer.valueOf(BotRules.ICF_Prefix1 + "" + checkLocation);
                } else if (checkLocation >= 10 && checkLocation <= 64) {
                    return Integer.valueOf(BotRules.ICF_Prefix2 + "" + checkLocation);
                }
            }
        }
        return 0;
    }
}
