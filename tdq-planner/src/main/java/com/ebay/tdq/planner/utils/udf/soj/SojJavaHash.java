package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

/**
 * Created by qingxu1 on 2017/7/10.
 */
public class SojJavaHash implements Serializable {

  public int evaluate(String guid, String constant, String experiment_id, int mod_value) {

    if (guid == null) {
      return (0);
    }

    if (String.valueOf(mod_value).equals("null")) {
      return (0);
    }

    return (SojMd5Hash.sojJavaHash(guid, constant, experiment_id, mod_value));

  }
}
