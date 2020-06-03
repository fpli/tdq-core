package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;

public class ScsCntForBot6Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCntForBot6Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_ON_AGENT);
      agentIpAttributeAccumulator.getAgentIpAttribute().setIpCount(0);
    }
  }

  @Override
  public void feed(Source source, Target target) throws Exception {

    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
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
          && agentIpAttributeAccumulator.getAgentIpAttribute().getIpCount() <= 0) {
        agentIpAttributeAccumulator.getAgentIpAttribute().setIpCount(1);
      }
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator
          .getAgentIpAttribute()
          .merge(agentIpAttribute, BotRules.SCS_ON_AGENT);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      int targetFlag = BotRules.SCS_ON_AGENT;
      if (botFilter.filter(sessionCore, targetFlag)) {
        return true;
      }
      if (sessionCore.getBotFlag() > 0 && sessionCore.getBotFlag() < 200) {
        return true;
      }
      return sessionCore.getUserAgent() == null || (sessionCore.getUserAgent().getAgentHash1() == 0L
          && sessionCore.getUserAgent().getAgentHash2() == 0L);
    }
    return false;
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
