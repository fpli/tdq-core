package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScsCntForBot6Indicator extends
    AbstractIndicator<SessionCore, AgentIpAttributeAccumulator> {

  public ScsCntForBot6Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    agentIpAttributeAccumulator.getAgentIpAttribute().clear();
  }

  @Override
  public void feed(SessionCore sessionCore,
                   AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    if (agentIpAttributeAccumulator.getAgentIpAttribute().getScsCountForBot6() < 0) {
      return;
    } else {
      if (isValid(sessionCore)) {
        if (SessionCoreHelper.isSingleClickSession(sessionCore)) {
          agentIpAttributeAccumulator
              .getAgentIpAttribute()
              .feed(sessionCore, BotRules.SCS_ON_AGENT);
        } else {
          agentIpAttributeAccumulator
              .getAgentIpAttribute()
              .revert(sessionCore, BotRules.SCS_ON_AGENT);
        }
      }
    }

    if (!SessionCoreHelper.isNonIframRdtCountZero(sessionCore)
        && !isIpBlank(sessionCore.getIp())
        && agentIpAttributeAccumulator.getAgentIpAttribute().getIpSet().size() <= 0) {

      agentIpAttributeAccumulator.getAgentIpAttribute()
          .getIpSet()
          .add(sessionCore.getIp());
    }
  }

  @Override
  public boolean filter(SessionCore sessionCore,
                        AgentIpAttributeAccumulator agentIpAttributeAccumulator) throws Exception {
    int targetFlag = BotRules.SCS_ON_AGENT;
    if (botFilter.filter(sessionCore, targetFlag)) {
      return true;
    }
    if (sessionCore.getBotFlag() > 0 && sessionCore.getBotFlag() < 200) {
      return true;
    }

    return sessionCore.getUserAgent() == null
        || (sessionCore.getUserAgent().getAgentHash1() == 0L
            && sessionCore.getUserAgent().getAgentHash2() == 0L);
  }

  private boolean isValid(SessionCore sessionCore) {
    return !SessionCoreHelper.isNonIframRdtCountZero(sessionCore)
        && !isIpBlank(sessionCore.getIp())
        && !SessionCoreHelper.isSingleClickNull(sessionCore);
  }

  protected boolean isIpBlank(Integer ip) {
    return ip == null || ip == 0;
  }
}
