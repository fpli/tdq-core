package com.ebay.tdq.planner.utils.udf.soj;

import com.google.common.primitives.Ints;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

public class IsInteger implements Serializable {

  public int evaluate(String inst) {
    if (inst == null || StringUtils.isEmpty(inst)) {
      return (0);
    }
    // Never use JDK's Integer.valueOf which throws exception for invalid integer and hurts performance
    Integer value = null;
    if (inst.charAt(0) != '+') {
      value = Ints.tryParse(inst);
    } else {
      value = Ints.tryParse(inst.substring(1));
    }

    if (value == null) {
      return 0;
    } else {
      return 1;
    }
  }
}