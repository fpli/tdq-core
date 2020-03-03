package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import java.io.IOException;
import java.util.Set;

public interface BotDetector<T> {
  Set<Integer> getBotFlagList(T t) throws IOException, InterruptedException;

  void initBotRules();
}
