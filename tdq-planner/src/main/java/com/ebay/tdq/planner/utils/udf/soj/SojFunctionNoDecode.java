package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.tdq.planner.utils.udf.utils.SojUtils;
import java.io.Serializable;
import java.util.Map;

public class SojFunctionNoDecode implements Serializable {

  public Map<String, String> evaluate(String payload, String keywords) {
    if (payload == null || keywords == null) {
      return null;
    }
    return SojUtils.parsePayload(payload, keywords, 0);
  }
}