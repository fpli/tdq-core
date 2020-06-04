package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.business.ubd.rule.BotRule7;
import com.ebay.sojourner.business.ubd.rule.BotRuleForDeclarativeHost;
import com.ebay.sojourner.business.ubd.rule.BotRuleForSuspectIP;
import com.ebay.sojourner.common.model.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class IpSignatureBotDetector implements BotDetector<IpAttribute> {

  private static volatile IpSignatureBotDetector singnatureBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();

  private IpSignatureBotDetector() {

    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
  }

  public static IpSignatureBotDetector getInstance() {
    if (singnatureBotDetector == null) {
      synchronized (IpSignatureBotDetector.class) {
        if (singnatureBotDetector == null) {
          singnatureBotDetector = new IpSignatureBotDetector();
        }
      }
    }
    return singnatureBotDetector;
  }

  public Set<Rule> rules() {
    return this.botRules;
  }

  @Override
  public Set<Integer> getBotFlagList(IpAttribute ipAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<Integer>();
    if (ipAttribute != null) {
      for (Rule rule : botRules) {
        int botFlag = rule.getBotFlag(ipAttribute);
        if (botFlag != 0) {
          botflagSet.add(botFlag);
        }
      }
    }

    return botflagSet;
  }

  @Override
  public void initBotRules() {
    botRules.add(new BotRule7());
    botRules.add(new BotRuleForSuspectIP());
    botRules.add(new BotRuleForDeclarativeHost());
  }
}
