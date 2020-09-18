package com.ebay.sojourner.business.indicator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScsCntForBot7IndicatorTest {

  ScsCntForBot7Indicator scsCntForBot7Indicator;

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  SessionCore sessionCore;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  AgentIpAttribute mockAgentIpAttribute = mock(AgentIpAttribute.class);

  @BeforeEach
  void setUp() {
    scsCntForBot7Indicator = new ScsCntForBot7Indicator(mockBotFilter);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentIpAttributeAccumulator.setAgentIpAttribute(mockAgentIpAttribute);
    sessionCore = new SessionCore();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockAgentIpAttribute).clear();
    scsCntForBot7Indicator.start(agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).clear();
  }

  @Test
  void test_feed_ScsCountForBot7_Gt0_singleClickSession() throws Exception {
    sessionCore.setUserAgent(new AgentHash(123L, 123L));
    sessionCore.setFlags(1);
    when(mockAgentIpAttribute.getScsCountForBot7()).thenReturn(1);
    scsCntForBot7Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).feed(sessionCore, BotRules.SCS_ON_IP);
  }

  @Test
  void test_feed_ScsCountForBot7_Gt0_nonSingleClickSession() throws Exception {
    sessionCore.setUserAgent(new AgentHash(123L, 123L));
    when(mockAgentIpAttribute.getScsCountForBot7()).thenReturn(1);
    scsCntForBot7Indicator.feed(sessionCore, agentIpAttributeAccumulator);
    verify(mockAgentIpAttribute, times(1)).revert(sessionCore, BotRules.SCS_ON_IP);
  }

  @Test
  void test_filter_botFilter_true() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(true);
    boolean result = scsCntForBot7Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_botFlag() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    sessionCore.setBotFlag(8);
    boolean result = scsCntForBot7Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void test_filter_IpIsNull() throws Exception {
    when(mockBotFilter.filter(any(), any())).thenReturn(false);
    boolean result = scsCntForBot7Indicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isTrue();
  }


}