package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.rule.BotRule1;
import com.ebay.sojourner.ubd.common.rule.BotRule10;
import com.ebay.sojourner.ubd.common.rule.BotRule11;
import com.ebay.sojourner.ubd.common.rule.BotRule12;
import com.ebay.sojourner.ubd.common.rule.BotRule15;
import com.ebay.sojourner.ubd.common.rule.BotRule203;
import com.ebay.sojourner.ubd.common.rule.BotRule204;
import com.ebay.sojourner.ubd.common.rule.BotRule205;
import com.ebay.sojourner.ubd.common.rule.BotRule206;
import com.ebay.sojourner.ubd.common.rule.BotRule207;
import com.ebay.sojourner.ubd.common.rule.BotRule212;
import com.ebay.sojourner.ubd.common.rule.BotRule215;
import com.ebay.sojourner.ubd.common.rule.BotRule9;
import com.ebay.sojourner.ubd.common.rule.Rule;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class SessionBotDetector implements BotDetector<UbiSession> {

  private static volatile SessionBotDetector sessionBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();
  private BotFilter filter = null;

  private SessionBotDetector() {
    initBotRules();
    filter = new UbiBotFilter();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
  }

  public static SessionBotDetector getInstance() {
    if (sessionBotDetector == null) {
      synchronized (SessionBotDetector.class) {
        if (sessionBotDetector == null) {
          sessionBotDetector = new SessionBotDetector();
        }
      }
    }
    return sessionBotDetector;
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
    botRules.add(new BotRule1());
    botRules.add(new BotRule15());
    botRules.add(new BotRule9());
    botRules.add(new BotRule10());
    botRules.add(new BotRule12());
    botRules.add(new BotRule203());
    botRules.add(new BotRule204());
    botRules.add(new BotRule205());
    botRules.add(new BotRule206());
    botRules.add(new BotRule207());
    //    botRules.add(new BotRule208());
    botRules.add(new BotRule212());
    botRules.add(new BotRule215());
    botRules.add(new BotRule11());
  }
}
