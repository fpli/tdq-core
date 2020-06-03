package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionCore;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.AgentIpIndicators;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentIpAttributeAgg
    implements AggregateFunction<
    SessionCore, AgentIpAttributeAccumulator, AgentIpAttributeAccumulator> {

  // private AgentIpIndicators agentIpIndicators;

  @Override
  public AgentIpAttributeAccumulator createAccumulator() {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    // agentIpIndicators = AgentIpIndicators.getInstance();

    try {
      AgentIpIndicators.getInstance().start(agentIpAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return agentIpAttributeAccumulator;
  }

  @Override
  public AgentIpAttributeAccumulator add(
      SessionCore sessionCore,
      AgentIpAttributeAccumulator agentIpAttributeAccumulator) {
    if (agentIpAttributeAccumulator.getAgentIpAttribute().getClientIp() == null
        && agentIpAttributeAccumulator.getAgentIpAttribute().getAgent() == null) {
      agentIpAttributeAccumulator.getAgentIpAttribute()
          .setClientIp(sessionCore.getIp());
      agentIpAttributeAccumulator.getAgentIpAttribute()
          .setAgent(sessionCore.getUserAgent());
    }
    try {

      AgentIpIndicators.getInstance().feed(sessionCore, agentIpAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return agentIpAttributeAccumulator;
  }

  @Override
  public AgentIpAttributeAccumulator getResult(
      AgentIpAttributeAccumulator agentIpAttributeAccumulator) {
    return agentIpAttributeAccumulator;
  }

  @Override
  public AgentIpAttributeAccumulator merge(
      AgentIpAttributeAccumulator a, AgentIpAttributeAccumulator b) {
    return null;
  }
}
