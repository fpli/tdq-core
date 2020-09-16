package com.ebay.sojourner.business.detector;

import java.io.IOException;
import java.util.Set;

public interface BotDetector<T> {

  Set<Integer> getBotFlagList(T t) throws IOException, InterruptedException;

  void initBotRules();

  /*
  void initDynamicRules(RuleManager ruleManager, Set<Rule> rules, List<Long> dynamicRuleIdList,
      String category);

  Set<Rule> rules();
  */
}
