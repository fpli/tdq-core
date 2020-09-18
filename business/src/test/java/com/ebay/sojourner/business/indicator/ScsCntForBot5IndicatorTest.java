package com.ebay.sojourner.business.indicator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScsCntForBot5IndicatorTest {

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  IpAttributeAccumulator ipAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);
  IpAttribute mockIpAttribute = mock(IpAttribute.class);

  @BeforeEach
  void setUp() {
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    ipAttributeAccumulator = new IpAttributeAccumulator();
    ipAttributeAccumulator.setIpAttribute(mockIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void start_AgentIpAttributeAccumulator() throws Exception {
    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    scsCntForBot5Indicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear();
  }

  @Test
  void start_IpAttributeAccumulator() throws Exception {
    ScsCntForBot5Indicator<SessionCore, IpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    scsCntForBot5Indicator.start(ipAttributeAccumulator);
    verify(mockIpAttribute, times(1)).clear();
  }

  @Test
  void feed_SessionCore_scsCntLt0() throws Exception {
    when(mockAgentIpAttribute.getScsCountForBot5()).thenReturn(-1);
    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);

    scsCntForBot5Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(0)).feed(sessionCore, BotRules.SCS_ON_AGENTIP);
  }

  @Test
  void feed_SessionCore_scsCntGt0_SingleClickSession() throws Exception {
    when(mockAgentIpAttribute.getScsCountForBot5()).thenReturn(1);
    sessionCore.setIp(123);
    sessionCore.setFlags(1);

    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);

    scsCntForBot5Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.SCS_ON_AGENTIP);
  }

  @Test
  void feed_SessionCore_scsCntGt0_nonSingleClickSession() throws Exception {
    when(mockAgentIpAttribute.getScsCountForBot5()).thenReturn(1);
    sessionCore.setIp(123);

    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);

    scsCntForBot5Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).revert(sessionCore, BotRules.SCS_ON_AGENTIP);
  }

  @Test
  void feed_AgentIpAttribute() throws Exception {
    doNothing().when(mockAgentIpAttribute).merge(mockAgentIpAttribute, BotRules.SCS_ON_AGENTIP);
    ScsCntForBot5Indicator<AgentIpAttribute, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);

    scsCntForBot5Indicator.feed(mockAgentIpAttribute, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).merge(mockAgentIpAttribute, BotRules.SCS_ON_AGENTIP);
  }

  @Test
  void filter_SessionCore_botFilterTrue() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(true);
    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    boolean result = scsCntForBot5Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result)
        .isTrue();
  }

  @Test
  void filter_SessionCore_botFilterFalse() throws Exception {
    sessionCore.setBotFlag(5);
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    boolean result = scsCntForBot5Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result)
        .isTrue();
  }

  @Test
  void filter_SessionCore_botFilterFalse_botFlagFalse() throws Exception {
    sessionCore.setBotFlag(201);
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    ScsCntForBot5Indicator<SessionCore, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    boolean result = scsCntForBot5Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result)
        .isFalse();
  }

  @Test
  void filter_nonSessionCore() throws Exception {
    ScsCntForBot5Indicator<AgentIpAttribute, AgentIpAttributeAccumulator> scsCntForBot5Indicator
        = new ScsCntForBot5Indicator<>(mockBotFilter);
    boolean result = scsCntForBot5Indicator.filter(mockAgentIpAttribute, agentIpAttributeAccumulator);
    Assertions.assertThat(result)
        .isFalse();
  }
}