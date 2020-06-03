package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule5;
import com.ebay.sojourner.ubd.common.rule.BotRule8;
import com.ebay.sojourner.ubd.common.rule.BotRuleForNewBot;
import com.ebay.sojourner.ubd.common.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class AgentIpSignatureBotDetector implements BotDetector<AgentIpAttribute> {

  private static volatile AgentIpSignatureBotDetector agentIpSignatureBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();

  private AgentIpSignatureBotDetector() {

    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
  }

  public static AgentIpSignatureBotDetector getInstance() {
    if (agentIpSignatureBotDetector == null) {
      synchronized (AgentIpSignatureBotDetector.class) {
        if (agentIpSignatureBotDetector == null) {
          agentIpSignatureBotDetector = new AgentIpSignatureBotDetector();
        }
      }
    }
    return agentIpSignatureBotDetector;
  }

  public Set<Rule> rules() {
    return this.botRules;
  }

  @Override
  public Set<Integer> getBotFlagList(AgentIpAttribute agentIpAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<Integer>();
    if (agentIpAttribute != null) {
      for (Rule rule : botRules) {
        int botFlag = rule.getBotFlag(agentIpAttribute);
        if (botFlag != 0) {
          botflagSet.add(botFlag);
        }
      }
    }

    return botflagSet;
  }

  @Override
  public void initBotRules() {
    botRules.add(new BotRule5());
    botRules.add(new BotRule8());
    botRules.add(new BotRuleForNewBot());
  }
}
