package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.sharedlib.indicators.AgentIpIndicators;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentIpAttributeAgg
    implements AggregateFunction<
    IntermediateSession, AgentIpAttributeAccumulator, AgentIpAttributeAccumulator> {

  private AgentIpIndicators agentIpIndicators;

  @Override
  public AgentIpAttributeAccumulator createAccumulator() {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpIndicators = AgentIpIndicators.getInstance();

    try {
      agentIpIndicators.start(agentIpAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return agentIpAttributeAccumulator;
  }

  @Override
  public AgentIpAttributeAccumulator add(
      IntermediateSession intermediateSession,
      AgentIpAttributeAccumulator agentIpAttributeAccumulator) {
    if (agentIpAttributeAccumulator.getAgentIpAttribute().getClientIp() == null
        && agentIpAttributeAccumulator.getAgentIpAttribute().getAgent() == null) {
      agentIpAttributeAccumulator.getAgentIpAttribute()
          .setClientIp(intermediateSession.getClientIp());
      agentIpAttributeAccumulator.getAgentIpAttribute()
          .setAgent(intermediateSession.getUserAgent());
    }
    try {

      agentIpIndicators.feed(intermediateSession, agentIpAttributeAccumulator, true);
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
