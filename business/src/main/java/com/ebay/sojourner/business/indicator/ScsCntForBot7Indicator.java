package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.SessionCoreHelper;

public class ScsCntForBot7Indicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public ScsCntForBot7Indicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear();
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.SCS_ON_IP);
    } else if (target instanceof IpAttributeAccumulator) {
      IpAttributeAccumulator agentIpAttributeAccumulator = (IpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getIpAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
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
    } else {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator AgentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      AgentIpAttributeAccumulator.getAgentIpAttribute().merge(agentIpAttribute, BotRules.SCS_ON_IP);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
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
    return false;
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
