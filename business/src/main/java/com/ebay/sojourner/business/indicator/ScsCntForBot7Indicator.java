package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;

public class ScsCntForBot7Indicator extends
    AbstractIndicator<SessionCore, AgentIpAttributeAccumulator> {

  public ScsCntForBot7Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute().clear();
  }

  @Override
  public void feed(SessionCore sessionCore,
                   AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot7() >= 0) {
      if (isValid(sessionCore)) {
        if (SessionCoreHelper.isSingleClickSession(sessionCore)) {
          agentIpAttributeAccumulator
              .getAgentIpAttribute()
              .feed(sessionCore, BotRules.SCS_ON_IP);
        } else {
          agentIpAttributeAccumulator
              .getAgentIpAttribute()
              .revert(sessionCore, BotRules.SCS_ON_IP);
        }
      }
    }
  }

  @Override
  public boolean filter(SessionCore sessionCore,
                        AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    int targetFlag = BotRules.SCS_ON_IP;
    if (botFilter.filter(sessionCore, targetFlag)) {
      return true;
    }
    if (sessionCore.getBotFlag() != null && sessionCore.getBotFlag() > 0
        && sessionCore.getBotFlag() < 200) {
      return true;
    }
    return sessionCore.getIp() == null || sessionCore.getIp() == 0L;
  }

  private boolean isValid(SessionCore sessionCore) {
    return !SessionCoreHelper.isNonIframRdtCountZero(sessionCore)
        && !isAgentBlank(sessionCore.getUserAgent())
        && !SessionCoreHelper.isSingleClickNull(sessionCore);
  }

  protected boolean isAgentBlank(AgentHash agent) {
    return agent == null || agent.getAgentHash1() == null
        || agent.getAgentHash2() == null || agent.getAgentHash1() == 0L
        || agent.getAgentHash2() == 0L;
  }
}
