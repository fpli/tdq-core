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

class SuspectIPIndicatorTest {

  SuspectIPIndicator suspectIPIndicator;

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);

  @BeforeEach
  void setup() {
    suspectIPIndicator = new SuspectIPIndicator(mockBotFilter);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockAgentIpAttribute).clear(BotRules.SUSPECTED_IP_ON_AGENT);
    suspectIPIndicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear(BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Test
  void test_feed() throws Exception {
    doNothing().when(mockAgentIpAttribute).feed(sessionCore, BotRules.SUSPECTED_IP_ON_AGENT);
    suspectIPIndicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.SUSPECTED_IP_ON_AGENT);
  }

  @Test
  void test_filter_botFilter_true() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(true);
    boolean result = suspectIPIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFilter_ip_is_null() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    sessionCore.setIp(null);
    boolean result = suspectIPIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }
}
