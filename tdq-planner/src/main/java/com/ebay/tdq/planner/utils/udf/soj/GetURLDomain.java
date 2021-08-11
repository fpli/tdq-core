package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class GetURLDomain implements Serializable {

  public String evaluate(String url) {
    if (url == null) {
      return null;
    }

    //check :
    int posStart = url.indexOf(":");
    if (posStart < 0) {
      return null;
    }

    //verify // after :
    if (url.length() < posStart + 3
        || !"//".equals(url.substring(posStart + 1, posStart + 3))) {
      return null;
    } else {
      posStart += 2;
    }

    //check uid
    int posUID = url.indexOf("@", posStart + 1);
    //get position of /
    int posPath = url.indexOf("/", posStart + 1);

    //@ is not separator of uid/pwd
    if (posUID >= 0 && posPath >= 0 && posPath < posUID) {
      posUID = -1;
    }

    if (posUID >= 0) {
      posStart = posUID;
    }
    if (posPath < 0) {
      posPath = url.length();
    }

    // check port
    int posEnd = url.indexOf(":", posStart + 1);
    if (posEnd >= 0 && posEnd < posPath) {
      posPath = posEnd;
    }

    String temp = url.substring(posStart + 1, posPath);
    return temp.substring(0, lastAlphaNumber(temp) + 1);
  }

  int lastAlphaNumber(String str) {
    return lastAlphaNumber(str, str.length() - 1, 0);
  }

  int lastAlphaNumber(String str, int startPos, int endPos) {
    for (int i = startPos; i >= endPos; i--) {
      if (Character.isAlphabetic(str.charAt(i))) {
        return i;
      }
    }
    return -1;
  }
}
