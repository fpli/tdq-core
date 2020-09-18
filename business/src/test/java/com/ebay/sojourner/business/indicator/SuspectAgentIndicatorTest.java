package com.ebay.sojourner.business.indicator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuspectAgentIndicatorTest {

  SuspectAgentIndicator suspectAgentIndicator;

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);

  @BeforeEach
  void setup() {
    suspectAgentIndicator = new SuspectAgentIndicator(mockBotFilter);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockAgentIpAttribute).clear(BotRules.DECLARED_AGENT);
    suspectAgentIndicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear(BotRules.DECLARED_AGENT);
  }

  @Test
  void test_feed() throws Exception {
    doNothing().when(mockAgentIpAttribute).feed(sessionCore, BotRules.DECLARED_AGENT);
    suspectAgentIndicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.DECLARED_AGENT);
  }

  @Test
  void test_filter_botFilter_true() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(true);
    boolean result = suspectAgentIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFilter_false() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    sessionCore.setUserAgent(null);
    boolean result = suspectAgentIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }
}
