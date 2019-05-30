package com.ebay.sojourner.ubd.common.sharedlib.detector;

import java.util.List;

public interface BotDetector<T> {
    List<Integer> getBotFlagList(T t);
    void initBotRules();
}
