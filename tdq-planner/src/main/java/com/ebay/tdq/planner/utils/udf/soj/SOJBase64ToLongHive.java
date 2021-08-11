package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class SOJBase64ToLongHive implements Serializable {

  public Long evaluate(String b64) {
    if (b64 == null) {
      return null;
    }

    return SOJBase64ToLong.getLong(b64);
  }
}