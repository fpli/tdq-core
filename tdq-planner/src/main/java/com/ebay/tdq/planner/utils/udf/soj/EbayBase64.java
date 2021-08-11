package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class EbayBase64 implements Serializable {

  public String evaluate(String encodedText) {
    return evaluate(encodedText, true);
  }

  public String evaluate(String encodedText, boolean isStandard) {
    if (encodedText == null) {
      return null;
    }

    byte[] bytes = Base64Ebay.decode(encodedText, isStandard);
    return new String(bytes);
  }
}
