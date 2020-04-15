package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BotDetector<T> {

  Set<Integer> getBotFlagList(T t) throws IOException, InterruptedException;

  void initBotRules();

  void initDynamicRules(RuleManager ruleManager, Set<Rule> rules, List<Long> dynamicRuleIdList,
      String category);

  Set<Rule> rules();
}
