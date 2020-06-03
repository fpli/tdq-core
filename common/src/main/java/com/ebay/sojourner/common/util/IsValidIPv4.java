package com.ebay.sojourner.common.util;

import org.apache.commons.lang3.StringUtils;

public class IsValidIPv4 {

  public static boolean isValidIP(String instr) {
    if (StringUtils.isBlank(instr)) {
      return false;
    }

    int iplen = 0;
    int ip1, ip2, ip3, ip4;
    String[] ipseg = null;

    iplen = instr.length();

    // Error if length is less than 7 or greater than 15
    if (iplen < 7 || iplen > 15) {
      return false;
    }

    ipseg = instr.split("\\.");

    // if all 4 values not returned then it is error
    if (ipseg.length != 4) {
      return false;
    }

    // if anyone of them is less than 0 or greater than 255 it is error
    try {
      // only trim left blank
      // align with TD udf
      ip1 = Integer.parseInt(ltrim(ipseg[0]));
      ip2 = Integer.parseInt(ltrim(ipseg[1]));
      ip3 = Integer.parseInt(ltrim(ipseg[2]));
      ip4 = Integer.parseInt(ltrim(ipseg[3]));

      if ((ip1 < 0 || ip1 > 255)
          || (ip2 < 0 || ip2 > 255)
          || (ip3 < 0 || ip3 > 255)
          || (ip4 < 0 || ip4 > 255)) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  private static String ltrim(String value) {
    String result = value;
    if (result == null) {
      return result;
    }
    char[] ch = result.toCharArray();
    int index = -1;
    for (int i = 0; i < ch.length; i++) {
      if (Character.isWhitespace(ch[i])) {
        index = i;
      } else {
        break;
      }
    }
    if (index != -1) {
      result = result.substring(index + 1);
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(isValidIP(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0"));
  }
}
