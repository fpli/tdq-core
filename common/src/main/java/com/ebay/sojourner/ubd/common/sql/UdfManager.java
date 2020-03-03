package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.sharedlib.util.SOJExtractFlag;
import com.ebay.sojourner.ubd.common.util.BitUtils;

public class UdfManager {

  public boolean checkBit(long bits, int index) {
    return BitUtils.checkBit(bits, index) == 1;
  }

  public boolean matchFlag(String flags, int bitPosition, int expectedValue) {
    int result = SOJExtractFlag.extractFlag(flags, bitPosition);
    return (result == expectedValue);
  }

  public static class SquareFunction {
    public int eval(int a) {
      return a * a;
    }
  }

  public static class FirstFunction {
    public FirstFunction() {}

    public String init() {
      return "";
    }

    public String add(String accumulator, String v) {
      if ("".equals(accumulator)) {
        return v;
      } else {
        return accumulator;
      }
    }

    public String merge(String accumulator0, String accumulator1) {
      return accumulator0;
    }

    public String result(String accumulator) {
      return accumulator;
    }
  }
}
