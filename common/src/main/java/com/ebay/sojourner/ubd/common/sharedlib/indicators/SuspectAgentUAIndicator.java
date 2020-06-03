package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.BotRules;

public class SuspectAgentUAIndicator<Source, Target> extends AbstractIndicator<Source, Target> {

  public SuspectAgentUAIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(Target target) throws Exception {
    AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
    agentAttributeAccumulator.getAgentAttribute().clear();
  }

  @Override
  public void feed(Source source, Target target) throws Exception {

    AgentIpAttribute agentIpAttribute = (AgentIpAttribute) source;
    AgentAttributeAccumulator agentAttributeAccumulator = (AgentAttributeAccumulator) target;
    agentAttributeAccumulator
        .getAgentAttribute()
        .feed(agentIpAttribute, BotRules.DECLARED_AGENT);

  }

  @Override
  public boolean filter(Source source, Target target) throws Exception {
    return false;
  }
}
