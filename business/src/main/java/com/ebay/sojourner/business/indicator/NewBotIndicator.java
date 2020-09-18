package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;

public class NewBotIndicator extends AbstractIndicator<SessionCore, AgentIpAttributeAccumulator> {

  public NewBotIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute()
        .clear();
  }

  @Override
  public void feed(SessionCore sessionCore,
                   AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute()
        .feed(sessionCore, BotRules.SAME_AGENT_IP);
  }

  @Override
  public boolean filter(SessionCore sessionCore,
                        AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    int targetFlag = BotRules.SAME_AGENT_IP;
    if (botFilter.filter(sessionCore, targetFlag)
        || SessionCoreHelper.getExInternalIp(sessionCore) == null) {
      return true;
    }
    return sessionCore.getBotFlag() != null
        && sessionCore.getBotFlag() > 0
        && sessionCore.getBotFlag() <= 200;

  }
}
