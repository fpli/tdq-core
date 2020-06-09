package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.AgentIpSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.AgentIpIndicatorsSliding;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentIpAttributeAggSliding implements
    AggregateFunction<AgentIpAttribute, AgentIpAttributeAccumulator, AgentIpAttributeAccumulator> {

  @Override
  public AgentIpAttributeAccumulator createAccumulator() {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();

    try {
      AgentIpIndicatorsSliding.getInstance().start(agentIpAttributeAccumulator);
    } catch (Exception e) {
      log.error("init agent ip indicators failed", e);
    }

    return agentIpAttributeAccumulator;
  }

  @Override
  public AgentIpAttributeAccumulator add(AgentIpAttribute agentIpAttribute,
      AgentIpAttributeAccumulator agentIpAttributeAccumulator) {

    if (agentIpAttributeAccumulator.getAgentIpAttribute().getClientIp() == null
        && agentIpAttributeAccumulator.getAgentIpAttribute().getAgent() == null) {
      agentIpAttributeAccumulator.getAgentIpAttribute().setClientIp(agentIpAttribute.getClientIp());
      agentIpAttributeAccumulator.getAgentIpAttribute().setAgent(agentIpAttribute.getAgent());
    }

    try {
      AgentIpIndicatorsSliding.getInstance()
          .feed(agentIpAttribute, agentIpAttributeAccumulator);
    } catch (Exception e) {
      log.error("start agent ip indicators collection failed", e);
    }

    Set<Integer> agentIpBotFlag = null;
    Map<Integer, Integer> signatureStates = agentIpAttributeAccumulator.getSignatureStates();

    try {
      if (signatureStates.containsValue(0) || signatureStates.containsValue(1)) {
        agentIpBotFlag = AgentIpSignatureBotDetector.getInstance()
            .getBotFlagList(agentIpAttributeAccumulator.getAgentIpAttribute());
        if (agentIpBotFlag.contains(5)) {
          switch (signatureStates.get(5)) {
            case 0:
              signatureStates.put(5, 1);
              break;
            case 1:
              signatureStates.put(5, 2);
              break;
          }
        } else if (agentIpBotFlag.contains(8)) {
          switch (signatureStates.get(8)) {
            case 0:
              signatureStates.put(8, 1);
              break;
            case 1:
              signatureStates.put(8, 2);
              break;
          }
        }
      }
    } catch (IOException | InterruptedException e) {
      log.error("start get agent ip botFlagList failed", e);
    }

    Set<Integer> botFlagList = SignatureUtils.setBotFlags(agentIpBotFlag,
        agentIpAttributeAccumulator.getAgentIpAttribute().getBotFlagList());
    agentIpAttributeAccumulator.getAgentIpAttribute().setBotFlagList(botFlagList);
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
