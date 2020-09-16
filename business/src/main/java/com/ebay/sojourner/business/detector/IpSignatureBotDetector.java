package com.ebay.sojourner.business.detector;

import com.ebay.sojourner.business.rule.BotRule7;
import com.ebay.sojourner.business.rule.BotRuleForDeclarativeHost;
import com.ebay.sojourner.business.rule.BotRuleForSuspectIP;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IpSignatureBotDetector implements BotDetector<IpAttribute> {

  private static volatile IpSignatureBotDetector singnatureBotDetector;
  private Set<Rule> botRules = new HashSet<>();

  private IpSignatureBotDetector() {
    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
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

  @Override
  public Set<Integer> getBotFlagList(IpAttribute ipAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<>();
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
