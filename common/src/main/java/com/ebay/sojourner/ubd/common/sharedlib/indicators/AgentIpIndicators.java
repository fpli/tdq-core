package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.BotFilter;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentIpIndicators
    extends AttributeIndicators<UbiSession, AgentIpAttributeAccumulator> {

  private static volatile AgentIpIndicators agentIpIndicators;
  private BotFilter botFilter;

  public AgentIpIndicators() {
    botFilter = new UbiBotFilter();
    initIndicators();
    try {
      init();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  public static AgentIpIndicators getInstance() {
    if (agentIpIndicators == null) {
      synchronized (AgentIpIndicators.class) {
        if (agentIpIndicators == null) {
          agentIpIndicators = new AgentIpIndicators();
        }
      }
    }
    return agentIpIndicators;
  }

  @Override
  public void initIndicators() {
    addIndicators(new ScsCountForBot5Indicator<>(botFilter));
    addIndicators(new ScsCountForBot6Indicator<>(botFilter));
    addIndicators(new ScsCountForBot7Indicator<>(botFilter));
    addIndicators(new ScsCountForBot8Indicator<>(botFilter));
    addIndicators(new SuspectAgentIndicator<>(botFilter));
    addIndicators(new SuspectIPIndicator<>(botFilter));
  }
}
