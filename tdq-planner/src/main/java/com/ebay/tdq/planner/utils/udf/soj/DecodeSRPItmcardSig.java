package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class DecodeSRPItmcardSig implements Serializable {

  private SOJURLDecodeEscapeHive sojurlDecodeEscape;
  private SojListGetValByIdx sojListGetValByIdx;
  private SojGetBase64EncodedBitsSet sojGetBase64EncodedBitsSet;
  private SojValueInList sojValueInList;

  public DecodeSRPItmcardSig() {
    super();
    sojurlDecodeEscape = new SOJURLDecodeEscapeHive();
    sojListGetValByIdx = new SojListGetValByIdx();
    sojGetBase64EncodedBitsSet = new SojGetBase64EncodedBitsSet();
    sojValueInList = new SojValueInList();
  }

  public int evaluate(String itmlt48sig, int itemIndex, int bitPosition) {

    String urlDecoded = sojurlDecodeEscape.evaluate(itmlt48sig, "%");

    String val = sojListGetValByIdx.evaluate(urlDecoded, ",", itemIndex);

    if (val == null) {
      return 0;
    }

    int totalCount = val.length() * 6;

    int countEqual = (val.length() - val.replaceAll("=", "").length()) * 8;

    int bitPos = totalCount - countEqual - 1 - bitPosition;
    String bitSet = sojGetBase64EncodedBitsSet.evaluate(val);
    if (bitSet == null) {
      return 0;
    }

    String ret = sojValueInList.evaluate(bitSet.toString(), ",", String.valueOf(bitPos));
    if (ret == null) {
      return 0;
    } else {
      return 1;
    }

  }
}
