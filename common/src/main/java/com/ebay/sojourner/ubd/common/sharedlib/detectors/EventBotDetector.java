package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.sql.Rules;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventBotDetector extends AbstractBotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();

  private EventBotDetector() {
    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
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

  public Set<Rule> rules() {
    return this.botRules;
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
  }
}
