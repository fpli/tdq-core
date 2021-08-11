package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class GetURLPath implements Serializable {

  public String evaluate(String url) {
    if (url == null) {
      return null;
    }

    //check :
    int pos = url.indexOf(":");
    if (pos < 0) {
      return null;
    }

    //verify // after :
    if (url.length() < pos + 3
        || !"//".equals(url.substring(pos + 1, pos + 3))) {
      return null;
    }
    pos += 2;

    //get position of /
    pos = url.indexOf("/", pos + 1);
    if (pos < 0 || pos == url.length()) {
      return null;
    }

    //get position of ?
    int pos2 = url.indexOf("?", pos + 1);
    if (pos2 < 0) {
      return url.substring(pos);
    } else {
      return url.substring(pos, pos2);
    }
  }
}