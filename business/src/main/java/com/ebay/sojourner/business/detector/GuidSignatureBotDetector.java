package com.ebay.sojourner.business.detector;

import com.ebay.sojourner.business.rule.BotRule15_Cross;
import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GuidSignatureBotDetector implements BotDetector<GuidAttribute> {

  private static volatile GuidSignatureBotDetector singnatureBotDetector;
  private Set<Rule> botRules = new HashSet<>();

  private GuidSignatureBotDetector() {
    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static GuidSignatureBotDetector getInstance() {
    if (singnatureBotDetector == null) {
      synchronized (GuidSignatureBotDetector.class) {
        if (singnatureBotDetector == null) {
          singnatureBotDetector = new GuidSignatureBotDetector();
        }
      }
    }
    return singnatureBotDetector;
  }

  @Override
  public Set<Integer> getBotFlagList(GuidAttribute guidAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<>();
    if (guidAttribute != null) {
      for (Rule rule : botRules) {
        int botFlag = rule.getBotFlag(guidAttribute);
        if (botFlag != 0) {
          botflagSet.add(botFlag);
        }
      }
    }
    return botflagSet;
  }

  @Override
  public void initBotRules() {
    botRules.add(new BotRule15_Cross());
  }
}
