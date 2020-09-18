package com.ebay.sojourner.business.indicator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScsCntForBot6IndicatorTest {

  ScsCntForBot6Indicator scsCntForBot6Indicator;

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);

  @BeforeEach
  void setUp() {
    scsCntForBot6Indicator = new ScsCntForBot6Indicator(mockBotFilter);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void start() throws Exception {
    doNothing().when(mockAgentIpAttribute).clear();
    scsCntForBot6Indicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear();
  }

  @Test
  void feed_scsCountLt0() throws Exception {
    when(mockAgentIpAttribute.getScsCountForBot6()).thenReturn(-1);
    scsCntForBot6Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(0)).feed(sessionCore, BotRules.SCS_ON_AGENT);
    verify(mockAgentIpAttribute, times(0)).revert(sessionCore, BotRules.SCS_ON_AGENT);
  }

  @Test
  void feed_scsCountGt0_validSessionCore_SingleClickSession() throws Exception {
    sessionCore.setIp(123);
    sessionCore.setFlags(1);
    when(mockAgentIpAttribute.getScsCountForBot6()).thenReturn(1);
    scsCntForBot6Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.SCS_ON_AGENT);
  }

  @Test
  void feed_scsCountGt0_validSessionCore_nonSingleClickSession() throws Exception {
    sessionCore.setIp(123);
    when(mockAgentIpAttribute.getScsCountForBot6()).thenReturn(1);
    scsCntForBot6Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).revert(sessionCore, BotRules.SCS_ON_AGENT);
  }

  @Test
  void filter_botFilterPass() throws Exception {
    when(mockBotFilter.filter(any(), anyInt())).thenReturn(true);
    boolean result = scsCntForBot6Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    assertThat(result).isTrue();
  }

  @Test
  void filter_botFlagPass() throws Exception {
    when(mockBotFilter.filter(any(), anyInt())).thenReturn(false);
    sessionCore.setBotFlag(5);
    boolean result = scsCntForBot6Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    assertThat(result).isTrue();
  }

  @Test
  void filter_botFlagFalse() throws Exception {
    when(mockBotFilter.filter(any(), anyInt())).thenReturn(false);
    sessionCore.setBotFlag(201);
    boolean result = scsCntForBot6Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    assertThat(result).isTrue();
  }
}