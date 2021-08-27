package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class StrBetweenStr implements Serializable {

  public String evaluate(String str, String start, String end) {
    if (str == null) {
      return null;
    }
    int strLen = str.length();

    int startLen = 0;
    if (start != null) {
      startLen = start.length();
    }

    int endLen = 0;
    if (end != null) {
      endLen = end.length();
    }

    if (startLen > strLen) {
      return null;
    }

    int startPos;
    if (startLen == 0) {
      startPos = 0;
    } else {
      int index = str.indexOf(start);
      if (index == -1) {
        return null;
      }
      startPos = index + startLen;
    }

    int endPos;
    if (endLen == 0) {
      endPos = strLen;
    } else {
      int index = str.indexOf(end, startPos);
      if (index == -1) {
        endPos = strLen;
      } else {
        endPos = index;
      }
    }

    return str.substring(startPos, endPos);
  }
}
