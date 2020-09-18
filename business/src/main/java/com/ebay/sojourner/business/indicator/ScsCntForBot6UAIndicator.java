package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.BotRules;

public class ScsCntForBot6UAIndicator extends
    AbstractIndicator<AgentIpAttribute, AgentAttributeAccumulator> {

  public ScsCntForBot6UAIndicator(BotFilter botFilter) {
    this.botFilter = botFilter;
  }

  @Override
  public void start(AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    agentAttributeAccumulator.getAgentAttribute().clear();
  }

  @Override
  public void feed(AgentIpAttribute agentIpAttribute,
                   AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    agentAttributeAccumulator.getAgentAttribute().feed(agentIpAttribute, BotRules.SCS_ON_AGENT);
  }

  @Override
  public boolean filter(AgentIpAttribute agentIpAttribute,
                        AgentAttributeAccumulator agentAttributeAccumulator) throws Exception {
    return false;
  }

}
