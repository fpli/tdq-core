package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.tdq.planner.utils.udf.utils.SojTimestamp;
import java.io.Serializable;

public class SojStr2Date implements Serializable {

  public String evaluate(final String sojTimestamp) {
    return SojTimestamp.getDateToSojTimestamp(sojTimestamp);
  }
}