package com.ebay.sojourner.common.util;

import com.ebay.sojourner.common.model.UbiEvent;

public class FlagUtils {

  public static boolean matchFlag(UbiEvent event, int bitPosition, int expectedValue) {
    int result = SOJExtractFlag.extractFlag(event.getFlags(), bitPosition);
    return (result == expectedValue);
  }
}
