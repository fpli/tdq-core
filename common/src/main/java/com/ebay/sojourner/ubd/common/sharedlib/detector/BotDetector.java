package com.ebay.sojourner.ubd.common.sharedlib.detector;

import java.util.List;
import java.util.Set;

public interface BotDetector<T> {
    Set<Integer> getBotFlagList(T t);
    void initBotRules();
}
