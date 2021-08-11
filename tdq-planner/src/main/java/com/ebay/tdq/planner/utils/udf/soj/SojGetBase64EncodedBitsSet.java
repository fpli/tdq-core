package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

/**
 * Created by qingxu1 on 2017/7/5.
 */
public class SojGetBase64EncodedBitsSet implements Serializable {

  public String evaluate(String sojFlag) {
    if (sojFlag == null) {
      return null;
    }

    return SojGetBase64EncodedBitsSets.getBase64EncodedBitsSetValue(sojFlag);

  }
}