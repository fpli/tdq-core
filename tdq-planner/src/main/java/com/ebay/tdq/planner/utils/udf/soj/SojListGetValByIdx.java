package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.sojourner.common.util.SOJListGetValueByIndex;
import java.io.Serializable;

public class SojListGetValByIdx implements Serializable {

  public String evaluate(final String str_vec, String delim, int index) {
    if (str_vec == null || delim == null || index < 1) {
      return null;
    }

    return SOJListGetValueByIndex.getValueByIndex(str_vec, delim, index);

  }
}