package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectAgentIndicator extends
    AbstractIndicator<SessionCore, AgentIpAttributeAccumulator> {

  public SuspectAgentIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.DECLARED_AGENT);
  }

  @Override
  public void feed(SessionCore sessionCore,
                   AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute().feed(sessionCore, BotRules.DECLARED_AGENT);
  }

  @Override
  public boolean filter(SessionCore sessionCore,
                        AgentIpAttributeAccumulator target) throws Exception {
    int targetFlag = BotRules.DECLARED_AGENT;
    if (botFilter.filter(sessionCore, targetFlag)) {
      return true;
    }
    return sessionCore.getUserAgent() == null || (sessionCore.getUserAgent().getAgentHash1() == 0L
        && sessionCore.getUserAgent().getAgentHash2() == 0L);
  }
}
