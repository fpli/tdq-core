package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.indicators.AgentIpIndicators;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentIpAttributeAgg
    implements AggregateFunction<
    SessionCore, AgentIpAttributeAccumulator, AgentIpAttributeAccumulator> {

  @Override
  public AgentIpAttributeAccumulator createAccumulator() {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();

    try {
      AgentIpIndicators.getInstance().start(agentIpAttributeAccumulator);
    } catch (Exception e) {
      log.error("init pre agent ip indicators failed", e);
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
      log.error("start pre agent ip indicators collection failed", e);
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
