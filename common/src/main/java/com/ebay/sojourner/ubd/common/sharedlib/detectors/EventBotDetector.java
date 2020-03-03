package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule1;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule10;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule11;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule12;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule13;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule2;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule3;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule4;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule5;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule56;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule6;
import com.ebay.sojourner.ubd.common.rule.icf.IcfRule7;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class EventBotDetector implements BotDetector<UbiEvent> {

  private static volatile EventBotDetector eventBotDetector;
  private Set<Rule> botRules = new LinkedHashSet<Rule>();

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

  @Override
  public void initBotRules() {
    botRules.add(new BotRule1());
    botRules.add(new IcfRule1());
    botRules.add(new IcfRule2());
    botRules.add(new IcfRule3());
    botRules.add(new IcfRule4());
    botRules.add(new IcfRule5());
    botRules.add(new IcfRule6());
    botRules.add(new IcfRule7());
    botRules.add(new IcfRule10());
    botRules.add(new IcfRule11());
    botRules.add(new IcfRule12());
    botRules.add(new IcfRule13());
    botRules.add(new IcfRule56());
  }
}
