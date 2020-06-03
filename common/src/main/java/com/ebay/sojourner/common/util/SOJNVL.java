package com.ebay.sojourner.common.util;

import org.apache.commons.lang3.StringUtils;

public class SOJNVL {

  public static final String[] KV_DELIMITER = {"&", "&_", "&!"};
  public static final String BLANK_STRING = "";

  public static String getTagValue(String value, String key) {
    if (StringUtils.isBlank(value) || StringUtils.isBlank(key)) {
      return null;
    }

    // ensure string starts with &
    value = "&" + value;

    for (int i = 0; i < KV_DELIMITER.length; i++) {
      String search = KV_DELIMITER[i] + key + "=";
      int startPos = StringUtils.indexOf(value, search);
      if (startPos > -1) {
        int endPos = value.length();
        for (String delimiter : KV_DELIMITER) {
          int extraIdx = StringUtils.indexOf(value, delimiter, startPos + search.length());
          if (extraIdx > -1) {
            endPos = extraIdx;
            break;
          }
        }

        String substring = StringUtils.substring(value, startPos, endPos);
        String[] kvPair = substring.split("=", 2);
        return BLANK_STRING.equals(kvPair[1]) ? null : kvPair[1];
      }
    }
    return null;
  }
}
