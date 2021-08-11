package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class StrBetweenList implements Serializable {

  public static String getStrBetweenList(String str, String start, String end) {
    if (str == null) {
      return null;
    }

    int startLen = 0;
    int endLen = 0;

    if (start != null) {
      startLen = start.length();
    }

    if (end != null) {
      endLen = end.length();
    }

    int foundStart = 0;
    int startIndex = -1;
    int endIndex = -1;
    outer:
    for (int i = 0; i < str.length(); i++) {
      if (foundStart == 0) {
        if (start == null) {
          foundStart = 1;
        } else {
          for (int j = 0; j < startLen; j++) {
            if (str.charAt(i) == start.charAt(j)) {
              foundStart = 1;
              startIndex = i;
              break;
            }
          }
        }
      } else if (foundStart == 1) {
        if (end == null) {
          return null;
        } else {
          for (int k = 0; k < endLen; k++) {
            if (str.charAt(i) == end.charAt(k)) {
              endIndex = i;
              break outer;
            }
          }
        }
      }
    }

    if (foundStart == 0) {
      return null;
    } else {
      if (startIndex == str.length() - 1) {
        return null;
      } else if (endIndex < 0) {
        return str.substring(startIndex + 1);
      } else if (startIndex + 1 == endIndex) {
        return null;
      } else {
        return str.substring(startIndex + 1, endIndex);
      }
    }
  }

  public String evaluate(String str, String start, String end) {
    return getStrBetweenList(str, start, end);
  }
}
