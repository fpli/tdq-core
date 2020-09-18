package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectIPIndicator extends
    AbstractIndicator<SessionCore, AgentIpAttributeAccumulator> {

  public SuspectIPIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute()
        .clear(BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Override
  public void feed(SessionCore sessionCore,
                   AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute()
        .feed(sessionCore, BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Override
  public boolean filter(SessionCore sessionCore,
                        AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    int targetFlag = BotRules.DECLARED_AGENT;
    if (botFilter.filter(sessionCore, targetFlag)) {
      return true;
    }
    return sessionCore.getIp() == null;
  }
}
