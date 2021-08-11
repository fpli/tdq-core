package com.ebay.tdq.planner.utils.udf.soj;

import org.apache.commons.lang.StringUtils;

/**
 * @author juntzhang
 */
public class SOJStrBetweenEndlist {

  public SOJStrBetweenEndlist() {
  }

  public static String getStringBetweenEndList(String url, char startchar, char endchar) {
    if (StringUtils.isBlank(url)) {
      return null;
    } else {
      int startPos = url.indexOf(startchar);
      if (startPos < 0) {
        startPos = 0;
      } else {
        ++startPos;
      }

      int endPos = url.indexOf(endchar, startPos);
      if (endPos < 0) {
        endPos = url.length();
      }

      return url.substring(startPos, endPos);
    }
  }
}
