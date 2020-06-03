package com.ebay.sojourner.business.ubd.detectors;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule6;
import com.ebay.sojourner.ubd.common.rule.BotRuleForDeclarativeAgent;
import com.ebay.sojourner.ubd.common.rule.BotRuleForSuspectAgent;
import com.ebay.sojourner.ubd.common.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class AgentSignatureBotDetector implements BotDetector<AgentAttribute> {

  private static volatile AgentSignatureBotDetector agentIpSignatureBotDetector;
  private static List<Long> dynamicRuleIdList = new CopyOnWriteArrayList<>();
  private Set<Rule> botRules = new CopyOnWriteArraySet<>();

  private AgentSignatureBotDetector() {

    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
  }

  public static List<Long> dynamicRuleIdList() {
    return dynamicRuleIdList;
  }

  public static AgentSignatureBotDetector getInstance() {
    if (agentIpSignatureBotDetector == null) {
      synchronized (AgentSignatureBotDetector.class) {
        if (agentIpSignatureBotDetector == null) {
          agentIpSignatureBotDetector = new AgentSignatureBotDetector();
        }
      }
    }
    return agentIpSignatureBotDetector;
  }

  public Set<Rule> rules() {
    return this.botRules;
  }

  @Override
  public Set<Integer> getBotFlagList(AgentAttribute agentAttribute)
      throws IOException, InterruptedException {
    Set<Integer> botflagSet = new HashSet<Integer>();
    if (agentAttribute != null) {
      for (Rule rule : botRules) {
        int botFlag = rule.getBotFlag(agentAttribute);
        if (botFlag != 0) {
          botflagSet.add(botFlag);
        }
      }
    }

    return botflagSet;
  }

  @Override
  public void initBotRules() {
    botRules.add(new BotRule6());
    botRules.add(new BotRuleForSuspectAgent());
    botRules.add(new BotRuleForDeclarativeAgent());
  }
}
