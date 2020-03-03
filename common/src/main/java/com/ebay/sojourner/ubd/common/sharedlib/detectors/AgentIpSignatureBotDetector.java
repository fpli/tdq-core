package com.ebay.sojourner.ubd.common.sharedlib.detectors;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.rule.BotRule5;
import com.ebay.sojourner.ubd.common.rule.BotRule8;
import com.ebay.sojourner.ubd.common.rule.Rule;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class AgentIpSignatureBotDetector implements BotDetector<AgentIpAttribute> {

  private static volatile AgentIpSignatureBotDetector agentIpSignatureBotDetector;
  private Set<Rule> botRules = new LinkedHashSet<Rule>();

  private AgentIpSignatureBotDetector() {

    initBotRules();
    for (Rule rule : botRules) {
      rule.init();
    }
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

  @Override
  public Set<Integer> getBotFlagList(AgentIpAttribute agentIpAttribute)
      throws IOException, InterruptedException {
    Set<Integer> signature = null;
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
  }

  private Set<Integer> scanSignature(
      String inColumnName, String inColumnValue, String outColumnName, String bucketName) {
    //        return CouchBaseManager.getInstance().getSignatureWithColumn(inColumnName,
    // inColumnValue, outColumnName);
    return null;
  }
}
