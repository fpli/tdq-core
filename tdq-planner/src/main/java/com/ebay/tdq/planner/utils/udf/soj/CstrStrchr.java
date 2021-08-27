package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class CstrStrchr implements Serializable {

  public String evaluate(String str, String chr) {

    if (str == null) {
      return null;
    }

    if (StringUtils.isEmpty(chr)) {
      return null;
    }

    char ch = chr.charAt(0);

    int index = str.indexOf(ch);
    if (index < 0) {
      return null;
    } else {
      return str.substring(index);
    }
  }
}
