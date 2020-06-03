package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.business.ubd.detectors.AgentSignatureBotDetector;
import com.ebay.sojourner.business.ubd.indicators.AgentIndicators;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.Constants;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.AggregateFunction;

@Slf4j
public class AgentAttributeAgg
    implements AggregateFunction<
    AgentIpAttribute, AgentAttributeAccumulator, AgentAttributeAccumulator> {

  private static final String AGENT = Constants.AGENT_LEVEL;
  // private AgentIndicators agentIndicators;
  // private AgentSignatureBotDetector agentSignatureBotDetector;
  // private RuleManager ruleManager;

  @Override
  public AgentAttributeAccumulator createAccumulator() {

    AgentAttributeAccumulator agentAttributeAccumulator = new AgentAttributeAccumulator();
    // agentIndicators = AgentIndicators.getInstance();
    // agentSignatureBotDetector = AgentSignatureBotDetector.getInstance();
    // ruleManager = RuleManager.getInstance();

    try {
      AgentIndicators.getInstance().start(agentAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
    }
    return agentAttributeAccumulator;
  }

  @Override
  public AgentAttributeAccumulator add(
      AgentIpAttribute agentIpAttribute, AgentAttributeAccumulator agentAttributeAccumulator) {
    if (agentAttributeAccumulator.getAgentAttribute().getAgent() == null) {

      agentAttributeAccumulator.getAgentAttribute().setAgent(agentIpAttribute.getAgent());
    }
    try {
      AgentIndicators.getInstance().feed(agentIpAttribute, agentAttributeAccumulator);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Set<Integer> agentBotFlag = null;

    try {
      if (agentAttributeAccumulator.getBotFlagStatus().containsValue(0)
          || agentAttributeAccumulator.getBotFlagStatus().containsValue(1)) {
        /*
        agentSignatureBotDetector.initDynamicRules(ruleManager, agentSignatureBotDetector.rules(),
            AgentSignatureBotDetector.dynamicRuleIdList(), AGENT);
            */
        agentBotFlag =
            AgentSignatureBotDetector.getInstance()
                .getBotFlagList(agentAttributeAccumulator.getAgentAttribute());
        if (agentBotFlag.contains(6)) {
          switch (agentAttributeAccumulator.getBotFlagStatus().get(6)) {
            case 0:
              agentAttributeAccumulator.getBotFlagStatus().put(6, 1);
              break;
            case 1:
              agentAttributeAccumulator.getBotFlagStatus().put(6, 2);
              break;
          }
        } else if (agentBotFlag.contains(220)) {
          switch (agentAttributeAccumulator.getBotFlagStatus().get(220)) {
            case 0:
              agentAttributeAccumulator.getBotFlagStatus().put(220, 1);
              break;
            case 1:
              agentAttributeAccumulator.getBotFlagStatus().put(220, 2);
              break;
          }
        } else if (agentBotFlag.contains(221)) {
          switch (agentAttributeAccumulator.getBotFlagStatus().get(221)) {
            case 0:
              agentAttributeAccumulator.getBotFlagStatus().put(221, 1);
              break;
            case 1:
              agentAttributeAccumulator.getBotFlagStatus().put(221, 2);
              break;
          }
        }
      }
    } catch (IOException | InterruptedException e) {
      log.error("agent getBotFlagList error", e);
    }

    Set<Integer> botFlagList = agentAttributeAccumulator.getAgentAttribute().getBotFlagList();

    if (agentBotFlag != null && agentBotFlag.size() > 0) {
      botFlagList.addAll(agentBotFlag);
    }

    agentAttributeAccumulator.getAgentAttribute().setBotFlagList(botFlagList);

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
