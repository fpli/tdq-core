package com.ebay.sojourner.ubd.common.util;

public interface BotFilter {

  // return true means the session should be filtered
  boolean filter(Object intermediateSession, Integer targetFlag)
      throws InterruptedException;
}
