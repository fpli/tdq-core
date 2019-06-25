package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import java.util.Set;

public interface BotDetector<T> {
    Set<Integer> getBotFlagList(T t);
    void initBotRules();
}
