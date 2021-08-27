package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.json.JSONObject;

/**
 * xiaoding on 2017-12-28
 */
public class SojJsonParse implements Serializable {

  public String evaluate(String jsonStr, String jsonKey) {
    if (jsonStr == null || jsonKey == null) {
      return null;
    }
    JSONObject obj = new JSONObject(jsonStr);
    if (obj.has(jsonKey)) {
      return (obj.getString(jsonKey));
    } else {
      return null;
    }

  }

}
