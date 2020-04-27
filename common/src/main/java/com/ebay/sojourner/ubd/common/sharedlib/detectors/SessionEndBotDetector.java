package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule208;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class SessionEndBotDetector implements BotDetector<UbiSession> {

  private static volatile SessionEndBotDetector sessionEndBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();
  private BotFilter filter = null;

  private SessionEndBotDetector() {
    initBotRules();
    filter = new UbiBotFilter();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
  }

  public static SessionEndBotDetector getInstance() {
    if (sessionEndBotDetector == null) {
      synchronized (SessionEndBotDetector.class) {
        if (sessionEndBotDetector == null) {
          sessionEndBotDetector = new SessionEndBotDetector();
        }
      }
    }
    return sessionEndBotDetector;
  }

  public Set<Rule> rules() {
    return this.botRules;
  }

  @Override
  public Set<Integer> getBotFlagList(UbiSession ubiSession)
      throws IOException, InterruptedException {
    Set<Integer> botRuleList = new LinkedHashSet<Integer>(botRules.size());
    for (Rule rule : botRules) {
      Integer botRule = rule.getBotFlag(ubiSession);
      if (botRule != null && botRule != 0) {
        if (!filter.filter(ubiSession, botRule)) {
          botRuleList.add(botRule);
        }
      }
    }
    return botRuleList;
  }

  @Override
  public void initBotRules() {
    botRules.add(new BotRule208());

  }
}
