package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.AgentSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.AgentIndicators;
import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentAttributeAgg implements
    AggregateFunction<AgentIpAttribute, AgentAttributeAccumulator, AgentAttributeAccumulator> {

  @Override
  public AgentAttributeAccumulator createAccumulator() {

    AgentAttributeAccumulator agentAttributeAccumulator = new AgentAttributeAccumulator();

    try {
      AgentIndicators.getInstance().start(agentAttributeAccumulator);
    } catch (Exception e) {
      log.error("init agent indicators failed", e);
    }

    return agentAttributeAccumulator;
  }

  @Override
  public AgentAttributeAccumulator add(AgentIpAttribute agentIpAttribute,
      AgentAttributeAccumulator agentAttributeAccumulator) {

    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();

    if (agentAttribute.getAgent() == null) {
      agentAttribute.setAgent(agentIpAttribute.getAgent());
    }

    try {
      AgentIndicators.getInstance().feed(agentIpAttribute, agentAttributeAccumulator);
    } catch (Exception e) {
      log.error("start agent indicators collection failed", e);
    }

    Set<Integer> agentBotFlag = null;
    Map<Integer, Integer> signatureStates = agentAttributeAccumulator.getSignatureStates();

    try {
      if (signatureStates.containsValue(0) || signatureStates.containsValue(1)) {
        agentBotFlag = AgentSignatureBotDetector.getInstance().getBotFlagList(agentAttribute);
        if (agentBotFlag.contains(6)) {
          switch (signatureStates.get(6)) {
            case 0:
              signatureStates.put(6, 1);
              break;
            case 1:
              signatureStates.put(6, 2);
              break;
          }
        } else if (agentBotFlag.contains(220)) {
          switch (signatureStates.get(220)) {
            case 0:
              signatureStates.put(220, 1);
              break;
            case 1:
              signatureStates.put(220, 2);
              break;
          }
        } else if (agentBotFlag.contains(221)) {
          switch (signatureStates.get(221)) {
            case 0:
              signatureStates.put(221, 1);
              break;
            case 1:
              signatureStates.put(221, 2);
              break;
          }
        }
      }
    } catch (IOException | InterruptedException e) {
      log.error("start get agent botFlagList failed", e);
    }

    Set<Integer> botFlagList = SignatureUtils
        .setBotFlags(agentBotFlag, agentAttribute.getBotFlagList());
    agentAttribute.setBotFlagList(botFlagList);
    return agentAttributeAccumulator;
  }

  @Override
  public AgentAttributeAccumulator getResult(AgentAttributeAccumulator agentAttributeAccumulator) {
    return agentAttributeAccumulator;
  }

  @Override
  public AgentAttributeAccumulator merge(AgentAttributeAccumulator a, AgentAttributeAccumulator b) {
    return null;
  }
}
