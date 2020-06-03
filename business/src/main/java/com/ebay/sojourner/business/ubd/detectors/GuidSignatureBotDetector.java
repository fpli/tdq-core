package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.business.ubd.rule.BotRule15_Cross;
import com.ebay.sojourner.business.ubd.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class GuidSignatureBotDetector implements BotDetector<GuidAttribute> {

  private static volatile GuidSignatureBotDetector singnatureBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();

  private GuidSignatureBotDetector() {

    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
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

  public Set<Rule> rules() {
    return this.botRules;
  }

  @Override
  public Set<Integer> getBotFlagList(GuidAttribute guidAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<Integer>();
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
