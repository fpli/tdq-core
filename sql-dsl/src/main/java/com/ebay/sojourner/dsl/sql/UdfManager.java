package com.ebay.sojourner.dsl.sql;

import com.ebay.sojourner.common.util.BitUtils;

public class UdfManager {

  public boolean checkBit(long bits, int index) {
    return BitUtils.isBitSet(bits, index - 1);
  }
}
