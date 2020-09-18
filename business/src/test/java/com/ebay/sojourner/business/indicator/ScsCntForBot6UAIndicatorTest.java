package com.ebay.sojourner.business.indicator;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScsCntForBot6UAIndicatorTest {

  ScsCntForBot6UAIndicator scsCntForBot6UAIndicator;

  AgentAttributeAccumulator agentAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentAttribute mockAgentAttribute = mock(AgentAttribute.class);
  AgentIpAttribute agentIpAttribute;

  @BeforeEach
  void setUp() {
    scsCntForBot6UAIndicator = new ScsCntForBot6UAIndicator(mockBotFilter);
    agentAttributeAccumulator = new AgentAttributeAccumulator();
    agentAttributeAccumulator.setAgentAttribute(mockAgentAttribute);
    sessionCore = new SessionCore();
    agentIpAttribute = new AgentIpAttribute();
  }

  @Test
  void start() throws Exception {
    doNothing().when(mockAgentAttribute).clear();
    scsCntForBot6UAIndicator.start(agentAttributeAccumulator);
    verify(mockAgentAttribute, times(1)).clear();
  }

  @Test
  void feed() throws Exception {
    doNothing().when(mockAgentAttribute).feed(agentIpAttribute, BotRules.SCS_ON_AGENT);
    scsCntForBot6UAIndicator.feed(agentIpAttribute, agentAttributeAccumulator);
    verify(mockAgentAttribute, times(1)).feed(agentIpAttribute, BotRules.SCS_ON_AGENT);
  }

  @Test
  void filter() throws Exception {
    boolean result = scsCntForBot6UAIndicator.filter(agentIpAttribute, agentAttributeAccumulator);
    Assertions.assertThat(result)
        .isFalse();
  }
}