package com.ebay.sojourner.ubd.common.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

public class IsValidPrivateIPv4 {

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
      // only trim white space
      ip1 = Integer.parseInt(ipseg[0].trim());
      ip2 = Integer.parseInt(ipseg[1].trim());
      ip3 = Integer.parseInt(ipseg[2].trim());
      ip4 = Integer.parseInt(ipseg[3].trim());

      if ((ip1 < 0 || ip1 > 255)
          || (ip2 < 0 || ip2 > 255)
          || (ip3 < 0 || ip3 > 255)
          || (ip4 < 0 || ip4 > 255)) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }

    return ip1 == 10 || ip1 == 192 && ip2 == 168 || ip1 == 172 && (ip2 >= 16 && ip2 <= 31);
  }
}
