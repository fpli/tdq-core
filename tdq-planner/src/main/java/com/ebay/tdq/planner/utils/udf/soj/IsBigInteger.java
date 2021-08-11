package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class IsBigInteger implements Serializable {

  public int evaluate(String inst) {
    if (inst == null) {
      return 0;
    }
    try {
      Long.parseLong(inst);
      return 1;
    } catch (Exception e) {
      if (inst.length() == 0) {
        return 1;
      } else {
        return 0;
      }
    }
  }
}
