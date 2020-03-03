package com.ebay.sojourner.ubd.common.rule;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;

public class BotRuleForSuspectAgent extends AbstractBotRule<AgentAttribute> {

  public static final int SESSION_CNT_THRESHOLD = 100;

  @Override
  public int getBotFlag(AgentAttribute agentAttribute) {
    int totalSessionCnt = agentAttribute.getTotalSessionCnt();
    if (totalSessionCnt > SESSION_CNT_THRESHOLD) {
      float nocguidBaseline = totalSessionCnt * 0.95f;
      float spsBaseline = totalSessionCnt * 0.97f;
      float directVisitBaseline = totalSessionCnt * 0.93f;
      if ((agentAttribute.getNocguidSessionCnt() * 1.0f) > nocguidBaseline
          || (agentAttribute.getSpsSessionCnt() * 1.0f) > spsBaseline
          || (agentAttribute.getNouidSessionCnt() * 1.0f) > directVisitBaseline
          || (agentAttribute.getDirectSessionCnt() * 1.0f) > directVisitBaseline
          || (agentAttribute.getMktgSessionCnt() * 1.0f) > directVisitBaseline
          || (agentAttribute.getIpCountForSuspect() * 1.0f) < (totalSessionCnt * 0.01f)) {
        return 220;
      }
    }
    return 0;
  }
}
