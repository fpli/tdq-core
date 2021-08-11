package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class SojExtractNVP implements Serializable {

  public static String getTagValue(String value, String key, String keyDelimiter, String valueDelimiter) {
    if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(key)) {
      int kLen = key.length();
      int kDelimiterLen = keyDelimiter.length();
      int vDelimiterLen = valueDelimiter.length();
      if (value.startsWith(key + valueDelimiter)) {
        String searchKey = key + valueDelimiter;
        int pos = value.indexOf(keyDelimiter, searchKey.length());
        if (pos >= 0) {
          return value.substring(searchKey.length(), pos);
        } else {
          return value.substring(searchKey.length());
        }
      } else {
        String searchKey = keyDelimiter + key + valueDelimiter;
        int l = kLen + kDelimiterLen + vDelimiterLen;
        int startPos = value.indexOf(searchKey);
        if (startPos > 0) {
          if (value.length() > l + startPos) {
            int endPos = value.indexOf(keyDelimiter, l + startPos);
            if (endPos >= 0) {
              return value.substring(l + startPos, endPos);
            } else {
              return value.substring(l + startPos);
            }
          } else {
            return null;
          }
        } else {
          return null;
        }
      }
    } else {
      return null;
    }
  }

  public static boolean CheckEqual(String f1, String v1) {
    if ((v1 == null && f1 != null) || (v1 != null && f1 == null)) {
      return false;
    } else if (v1 == null && f1 == null) {
      return true;
    } else {
      return v1.equals(f1);
    }
  }

  public static void Verify(String value, String key, String keyDelimiter, String valueDelimiter) {
    String f1 = getTagValue(value, key, keyDelimiter, valueDelimiter);
    String v1 = getTagValue(value, key, keyDelimiter, valueDelimiter);
    if (!CheckEqual(f1, v1)) {
      System.out.println("Failed");
    } else {
      System.out.println("Pass");
    }
  }

  public String evaluate(String payloadValue, String PayloadKey, String keyDelimiter, String valueDelimiter) {
    if (payloadValue == null || PayloadKey == null) {
      return null;
    }
    return getTagValue(payloadValue, PayloadKey, keyDelimiter, valueDelimiter);

  }
}
