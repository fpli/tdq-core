package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.Rules;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector implements BotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;
  private static Set<Long> dynamicRuleIdSet = new CopyOnWriteArraySet<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();
  // private RuleManager ruleManager = RuleManager.getInstance();

  private EventBotDetector() {
    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static Set<Long> dynamicRuleIdSet() {
    return dynamicRuleIdSet;
  }

  /*
  public RuleManager ruleManager() {
    return ruleManager;
  }
  */


  public static EventBotDetector getInstance() {
    if (eventBotDetector == null) {
      synchronized (EventBotDetector.class) {
        if (eventBotDetector == null) {
          eventBotDetector = new EventBotDetector();
        }
      }
    }
    return eventBotDetector;
  }

  @Override
  public Set<Integer> getBotFlagList(UbiEvent ubiEvent) throws IOException, InterruptedException {
    Set<Integer> botRuleList = new LinkedHashSet<>(botRules.size());
    // botRules.addAll(ruleManager.sqlEventRules());
    // dynamicRuleIdSet.addAll(ruleManager.ruleIdSet());
    for (Rule rule : botRules) {
      // rule.init();
      int botRule = rule.getBotFlag(ubiEvent);
      if (botRule != 0) {
        botRuleList.add(botRule);
      }
    }
    return botRuleList;
  }

  // static rules
  @Override
  public void initBotRules() {
    botRules.add(new BotRule1());
    botRules.add(Rules.ICF_RULE_1_COMPILER);
    botRules.add(Rules.ICF_RULE_2_COMPILER);
    botRules.add(Rules.ICF_RULE_3_COMPILER);
    botRules.add(Rules.ICF_RULE_4_COMPILER);
    botRules.add(Rules.ICF_RULE_5_COMPILER);
    botRules.add(Rules.ICF_RULE_6_COMPILER);
    botRules.add(Rules.ICF_RULE_7_COMPILER);
    botRules.add(Rules.ICF_RULE_10_COMPILER);
    botRules.add(Rules.ICF_RULE_11_COMPILER);
    botRules.add(Rules.ICF_RULE_12_COMPILER);
    botRules.add(Rules.ICF_RULE_13_COMPILER);
    botRules.add(Rules.ICF_RULE_56_COMPILER);
  }
}
