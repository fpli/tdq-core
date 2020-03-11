package com.ebay.sojourner.ubd.common.sql;

public class SqlRuleUtils {

  public static void printBotFlag(int botFlag) {
    System.out.println("bot flag = " + botFlag);
  }

  public static void printDuration(String name, long startNano, long endNano) {
    System.out.printf("duration (%-17s) = %dms\n", name, ((endNano - startNano) / 1000 / 1000));
  }
}
