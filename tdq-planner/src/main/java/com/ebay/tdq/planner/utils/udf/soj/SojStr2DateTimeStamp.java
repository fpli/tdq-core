package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.sojourner.common.util.SojTimestamp;
import java.io.Serializable;

public class SojStr2DateTimeStamp implements Serializable {

  public String evaluate(final String sojTimestamp) throws Exception {
    return SojTimestamp.getSojTimestamp(sojTimestamp);
  }
}