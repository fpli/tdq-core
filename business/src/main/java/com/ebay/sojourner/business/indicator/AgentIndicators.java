package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.util.BotFilter;
import com.ebay.sojourner.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentIndicators
    extends AttributeIndicators<AgentIpAttribute, AgentAttributeAccumulator> {

  private static volatile AgentIndicators agentIpIndicators;
  private BotFilter botFilter;

  public AgentIndicators() {
    botFilter = new UbiBotFilter();
    initIndicators();
    try {
      init();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public static AgentIndicators getInstance() {
    if (agentIpIndicators == null) {
      synchronized (AgentIndicators.class) {
        if (agentIpIndicators == null) {
          agentIpIndicators = new AgentIndicators();
        }
      }
    }
    return agentIpIndicators;
  }

  @Override
  public void initIndicators() {
    addIndicators(new ScsCntForBot6UAIndicator(botFilter));
    addIndicators(new SuspectAgentUAIndicator(botFilter));
  }
}
