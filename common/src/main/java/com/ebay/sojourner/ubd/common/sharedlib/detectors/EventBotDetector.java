package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import com.ebay.sojourner.ubd.common.sql.Rules;
import com.ebay.sojourner.ubd.common.sql.SqlEventRule;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector implements BotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;
  private Set<Rule> botRules = new CopyOnWriteArraySet<Rule>();
  private List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();

  private EventBotDetector() {
    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

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
    Set<Integer> botRuleList = new LinkedHashSet<Integer>(botRules.size());
    for (Rule rule : botRules) {
      Integer botRule = rule.getBotFlag(ubiEvent);
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

  //TODO:dynamic rules
  public void initDynamicRules() {
    List<SqlEventRule> dynamicEventRules = RuleManager.getInstance().sqlEventRules();
    log.info("before hot deploy bot rules:" + botRules.size());
    if (botRules.isEmpty() && !dynamicEventRules.isEmpty()) {
      botRules.addAll(dynamicEventRules);
      for (SqlEventRule sqlEventRule : dynamicEventRules) {
        dynamicRuleIdList.add(sqlEventRule.getRuleId());
        sqlEventRule.init();
      }
    } else if (!botRules.isEmpty() && !dynamicEventRules.isEmpty()) {
      // update
      for (Rule rule : botRules) {
        if (rule instanceof SqlEventRule) {
          SqlEventRule dynamicRule = (SqlEventRule) rule;
          if (dynamicRule.getVersion() != 0 && dynamicRule.getRuleId() != 0) {
            dynamicRuleIdList.add(dynamicRule.getRuleId());
            for (SqlEventRule sqlEventRule : dynamicEventRules) {
              if (sqlEventRule.getRuleId() == dynamicRule.getRuleId()
                  && sqlEventRule.getVersion() > dynamicRule.getVersion()) {
                botRules.remove(dynamicRule);
                botRules.add(sqlEventRule);
                  sqlEventRule.init();
              }
            }
          }
        }
      }

      // add
      for (SqlEventRule sqlEventRule : dynamicEventRules) {
        if (!dynamicRuleIdList.contains(sqlEventRule.getRuleId())) {
          dynamicRuleIdList.add(sqlEventRule.getRuleId());
          botRules.add(sqlEventRule);
          sqlEventRule.init();
        }
      }
    }
    log.info("after hot deploy bot rules:" + botRules.size());
  }
}
