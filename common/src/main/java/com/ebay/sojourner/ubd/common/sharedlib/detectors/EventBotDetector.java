package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.RuleManager;
import com.ebay.sojourner.ubd.common.sql.Rules;
import com.ebay.sojourner.ubd.common.sql.SqlEventRule;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector implements BotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;
  private Set<SqlEventRule> botRules = new CopyOnWriteArraySet<>();

  @Getter
  private static RuleManager ruleManager = RuleManager.getInstance();

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
    Set<Integer> botRuleList = new LinkedHashSet<>(botRules.size());
    // log.info("before dynamic rules:" + botRules.size());
    // this.botRules = ruleManager.getSqlEventRuleSet();
    // log.info("after dynamic rules:" + botRules.size());
    for (Rule rule : botRules) {
      // rule.init();
      int botRule = rule.getBotFlag(ubiEvent);
      if (botRule != 0) {
        botRuleList.add(botRule);
      }
    }

    // log.info("botFlagList size is:" + botRuleList.size());

    return botRuleList;
  }

  // static rules
  @Override
  public void initBotRules() {
    botRules.add(Rules.RULE_1_COMPILER);
    /*
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
    */
  }
}
