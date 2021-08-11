package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class SojMapToStr implements Serializable {

  private static final String FILED_DELIM = "=";
  private static final String COLUMN_DELIM = "&";

  public String evaluate(Map<String, String> sojMap) {
    if (MapUtils.isEmpty(sojMap)) {
      return null;
    }
    StringBuilder sojStr = new StringBuilder();
    for (Map.Entry<String, String> kvPair : sojMap.entrySet()) {
      sojStr.append(kvPair.getKey()).append(FILED_DELIM).append(kvPair.getValue())
          .append(COLUMN_DELIM);
    }
    if (sojStr.length() > 0) {
      return sojStr.substring(0, sojStr.length() - 1);
    }
    return null;
  }
}
