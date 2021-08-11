package com.ebay.tdq.planner.utils.udf.soj;

import org.apache.commons.lang.StringUtils;


public class SOJBase64ToLong {

  public static Long getLong(String b64) {
    if (StringUtils.isBlank(b64) || (b64.length() % 4) != 0) {
      return null;
    }

    try {
      byte s[] = new byte[8];
      byte s1[] = Base64Ebay.decode(b64, true);
      for (int i = 0; i < s1.length; i++) {
        s[i] = s1[i];
      }
      return toLong(s);
    } catch (Exception e) {
      return null;
    }
  }

  private static Long toLong(byte[] x) {
    return ((((long) x[7] & 0xff) << 56) | (((long) x[6] & 0xff) << 48)
        | (((long) x[5] & 0xff) << 40) | (((long) x[4] & 0xff) << 32)
        | (((long) x[3] & 0xff) << 24) | (((long) x[2] & 0xff) << 16)
        | (((long) x[1] & 0xff) << 8) | (((long) x[0] & 0xff) << 0));

  }
}
