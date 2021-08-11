package com.ebay.tdq.planner.utils.udf.utils;

/**
 * Created by xiaoding on 2017/1/20.
 */
public class FunctionUtil {

  public static boolean isContain(String str, String subStr) {
    if (str.contains(subStr)) {
      return true;
    } else {
      return false;
    }
  }
}
