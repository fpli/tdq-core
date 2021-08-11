package com.ebay.tdq.planner.utils.udf.soj;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class SojStringToMap implements Serializable {

  public Map<Integer, String> evaluate(String text, String delimiter1) {
    Map<Integer, String> ret = new HashMap<>();
    if (text == null) {
      return ret;
    }
    String[] values = text.split(delimiter1);

    for (int i = 0; i < values.length; i++) {
      if (StringUtils.isBlank(values[i])) {
        continue;
      }

      ret.put(i + 1, values[i]);
    }

    return ret;
  }
}
