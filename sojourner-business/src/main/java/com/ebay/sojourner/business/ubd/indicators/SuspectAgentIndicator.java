package com.ebay.sojourner.business.ubd.indicators;

import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectAgentIndicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public SuspectAgentIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    if (target instanceof AgentIpAttributeAccumulator) {
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator.getAgentIpAttribute().clear(BotRules.DECLARED_AGENT);
    } else if (target instanceof AgentAttributeAccumulator) {
      AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
      agentAttributeAccumulator.getAgentAttribute().clear();
    }
  }

  @Override
  public void feed(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator
          .getAgentIpAttribute()
          .feed(sessionCore, BotRules.DECLARED_AGENT);
    } else if (source instanceof AgentIpAttribute) {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentIpAttributeAccumulator agentAttributeAccumulator = (AgentIpAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentIpAttribute()
          .merge(agentIpAttribute, BotRules.DECLARED_AGENT);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof SessionCore) {
      SessionCore sessionCore = (SessionCore) source;
      int targetFlag = BotRules.DECLARED_AGENT;
      if (botFilter.filter(sessionCore, targetFlag)) {
        return true;
      }
      return sessionCore.getUserAgent() == null || (sessionCore.getUserAgent().getAgentHash1() == 0L
          && sessionCore.getUserAgent().getAgentHash2() == 0L);
    }
    return false;
  }
}
