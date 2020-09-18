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

class NewBotIndicatorTest {

  NewBotIndicator newBotIndicator;

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);

  @BeforeEach
  void setUp() {
    newBotIndicator = new NewBotIndicator(mockBotFilter);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockAgentIpAttribute).clear();
    newBotIndicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear();
  }

  @Test
  void test_feed() throws Exception {
    doNothing().when(mockAgentIpAttribute).feed(sessionCore, BotRules.SAME_AGENT_IP);
    newBotIndicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.SAME_AGENT_IP);
  }

  @Test
  void test_filter_botFilterTrue_externalIpIsNull() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(true);
    sessionCore.setExInternalIp(123);
    boolean result = newBotIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFilterFalse_externalIpIsNull() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    boolean result = newBotIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFilterFalse_externalIpIsNotNull() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    boolean result = newBotIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFlagTrue() throws Exception {
    sessionCore.setBotFlag(5);
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    boolean result = newBotIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFlagFalse() throws Exception {
    sessionCore.setBotFlag(201);
    sessionCore.setIp(123);
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    boolean result = newBotIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

}