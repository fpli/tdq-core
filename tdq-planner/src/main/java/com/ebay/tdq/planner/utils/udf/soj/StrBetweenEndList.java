package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class StrBetweenEndList implements Serializable {

  public String evaluate(String url, String start, String end) {
    if (StringUtils.isBlank(url)) {
      return null;
    }
    int startPos;
    int endPos;

    if (!StringUtils.isBlank(start)) {
      startPos = url.indexOf(start);
      if (startPos < 0) {
        return null;
      } else {
        startPos += start.length();
        if (startPos == url.length()) {
          return null;
        }
      }
    } else {
      startPos = 0;
    }

    if (StringUtils.isBlank(end)) {
      return url.substring(startPos);
    }

    endPos = url.length();
    int len = end.length();
    for (int i = 0; i < len; ++i) {
      char c = end.charAt(i);
      int l = url.indexOf(c, startPos);
      if (l != -1 && l < endPos) {
        endPos = l;
      }
    }

    return (startPos != 0 || endPos != 0) && endPos > startPos ? url.substring(startPos, endPos) : null;

  }
}
