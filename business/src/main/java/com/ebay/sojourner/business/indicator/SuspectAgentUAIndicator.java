package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class SuspectAgentUAIndicator extends
    AbstractIndicator<AgentIpAttribute, AgentAttributeAccumulator> {

  public SuspectAgentUAIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    agentAttributeAccumulator.getAgentAttribute().clear();
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute,
                   AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    agentAttributeAccumulator.getAgentAttribute().feed(agentIpAttribute, BotRules.DECLARED_AGENT);
  }

  @Override
  public boolean filter(AgentIpAttribute agentIpAttribute,
                        AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    return false;
  }
}
