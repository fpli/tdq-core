package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class IsBitSet implements Serializable {

  private static int isBitSet(byte[] decodedBuckets, int position) {
    int bucket = position / 8;
    if (bucket < decodedBuckets.length) {
      int actualFlag = decodedBuckets[bucket];
      int bitLocation = position % 8;
      return actualFlag >> 7 - bitLocation & 1;
    }
    return 0;
  }

  public Boolean evaluate(final String content, int position) {
    if (content == null || position < 0) {
      return false;
    }

    byte[] flagBytes = Base64Ebay.decode(content, false);
    return isBitSet(flagBytes, 0) != 0;
  }
}