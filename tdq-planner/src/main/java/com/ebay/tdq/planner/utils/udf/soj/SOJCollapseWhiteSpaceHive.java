package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class SOJCollapseWhiteSpaceHive implements Serializable {

  public String evaluate(String str) {
    if (str == null) {
      return null;
    }

    return SOJCollapseWhiteSpace.getString(str);
  }
}
