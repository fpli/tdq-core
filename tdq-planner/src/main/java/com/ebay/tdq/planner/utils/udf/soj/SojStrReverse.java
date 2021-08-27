package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class SojStrReverse implements Serializable {

  public String evaluate(String value) {
    if (value == null || value.length() == 0) {
      return null;
    }
    return StringUtils.reverse(value);
  }
}
