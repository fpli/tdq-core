package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;

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
  public void feed(Source source, Target target, boolean isNeeded) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      AgentIpAttributeAccumulator agentIpAttributeAccumulator =
          (AgentIpAttributeAccumulator) target;
      agentIpAttributeAccumulator
          .getAgentIpAttribute()
          .feed(intermediateSession, BotRules.DECLARED_AGENT, isNeeded);
    } else if (source instanceof AgentIpAttribute) {
      AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
      AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
      agentAttributeAccumulator
          .getAgentAttribute()
          .feed(agentIpAttribute, BotRules.DECLARED_AGENT, isNeeded);
    }
  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    if (source instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) source;
      int targetFlag = BotRules.DECLARED_AGENT;
      if (botFilter.filter(intermediateSession, targetFlag)) {
        return true;
      }
      return intermediateSession.getUserAgent() == null;
    }
    return false;
  }
}
