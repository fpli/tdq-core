package com.ebay.sojourner.ubd.common.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

public class GUID2IP {
  private static final int[] hexlookup = {
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 0-15
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 16-31
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 32-47
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8,
    9,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 48-63
    -1,
    10,
    11,
    12,
    13,
    14,
    15,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 64-79
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 80-95
    -1,
    10,
    11,
    12,
    13,
    14,
    15,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1, // 96-111
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1,
    -1 // 112-127
  };

  private static long power2(int base, int n) {
    long i, p;

    if (n == 0) return 1;

    p = 1;
    for (i = 1; i <= n; ++i) p = p * base;
    return p;
  }

  private static int Hex2Int(String in) {
    int i;
    int len = in.length();
    int out = 0;
    int dec = 0;
    int val = 0;

    for (i = 0; i < len; i++) {
      dec = hexlookup[in.charAt(i)];
      val = (int) power2(16, len - i - 1);
      out += val * dec;
    }
    return out;
  }

  private static String toBytes(int i) {
    int[] b = new int[4];

    b[0] = (byte) (i >> 24);
    b[1] = (byte) (i >> 16);
    b[2] = (byte) (i >> 8);
    b[3] = (byte) (i);

    for (int j = 0; j < 4; j++) {
      if (b[j] < 0) b[j] += 256;
    }

    return b[0] + "." + b[1] + "." + b[2] + "." + b[3];
  }

  public static String getIP(String guid) {
    // use SimpleDateFormat to format the output
    // set TimeZone to "GMT" align with TD udf
    if (StringUtils.isBlank(guid) || guid.trim().length() != 32) return null;

    String s = guid.substring(11, 19);
    int ip = Hex2Int(s);
    String ipAddress = toBytes(ip);

    return ipAddress;
  }
}
