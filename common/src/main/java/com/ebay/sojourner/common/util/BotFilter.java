package com.ebay.sojourner.common.util;

public interface BotFilter {

  // return true means the session should be filtered
  boolean filter(Object intermediateSession, Integer targetFlag)
      throws InterruptedException;
}
