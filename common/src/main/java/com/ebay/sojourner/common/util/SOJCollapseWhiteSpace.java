package com.ebay.sojourner.common.util;

import org.apache.commons.lang3.StringUtils;

public class SOJCollapseWhiteSpace {

  public static String getString(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < str.length() - 1; i++) {
      if ((int) str.charAt(i) == 32 && (int) str.charAt(i + 1) == 32) {
        continue;
      }
      sb.append(str.charAt(i));
    }

    sb.append(str.charAt(str.length() - 1));

    return sb.toString();
  }
}
