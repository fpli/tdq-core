package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IcfRuleUtils {

  public static int getIcfRuleType(long icfBinary, int checkLocation) {
    if (checkLocation > 64) {
      log.error("the max is 64");
    } else {
      int icfType = BitUtils.checkBit(icfBinary, checkLocation);
      if (icfType == 1) {
        if (checkLocation <= 7 && checkLocation >= 0) {
          return Integer.valueOf(BotRules.ICF_Prefix1 + "" + checkLocation);
        } else if (checkLocation >= 10 && checkLocation <= 11) {
          return Integer.valueOf(BotRules.ICF_Prefix1 + "" + (checkLocation - 2));
        } else if (checkLocation >= 12 && checkLocation <= 13) {
          return Integer.valueOf(BotRules.ICF_Prefix2 + "" + (checkLocation - 2));
        } else if (checkLocation == 56) {
          return Integer.valueOf(BotRules.ICF_Prefix2 + "" + 12);
        }
      }
    }
    return 0;
  }
}
