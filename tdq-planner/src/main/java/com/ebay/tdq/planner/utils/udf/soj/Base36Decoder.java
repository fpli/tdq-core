package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class Base36Decoder implements Serializable {

  public String evaluate(String enc) {
    if (enc == null) {
      return null;
    }
    StringBuilder decoded = new StringBuilder();
    if (enc.contains(",")) {
      String[] numArray = enc.split(",");
      for (String num : numArray) {
        if (decoded.length() > 0) {
          decoded.append(",");
        }
        try {
          decoded.append(Long.parseLong(num, 36));
        } catch (Exception e) {
          return null;
        }
      }
    } else {
      try {
        decoded.append(Long.parseLong(enc, 36));
      } catch (NumberFormatException e) {
        return null;
      }
    }
    return (decoded.toString());
  }
}
