package com.ebay.sojourner.business.indicator;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class SuspectIPIndicatorTest {

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  IpAttributeAccumulator ipAttributeAccumulator;
  IntermediateSession intermediateSession;
  @Mock
  UbiBotFilter mockBotFilter;

  @BeforeEach
  void setup() {
    initMocks(this);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    ipAttributeAccumulator = new IpAttributeAccumulator();
    intermediateSession = new IntermediateSession();
  }

  @Test
  void test_start_AgentIpAttributeAccumulator() throws Exception {
    SuspectIPIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.start(agentIpAttributeAccumulator);
  }

  @Test
  void test_start_IpAttributeAccumulator() throws Exception {
    SuspectIPIndicator<IntermediateSession, IpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.start(ipAttributeAccumulator);
  }

  @Test
  void test_feed_UbiSession() throws Exception {
    SuspectIPIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.feed(intermediateSession, agentIpAttributeAccumulator);
  }

  @Test
  void test_feed_AgentIpAttribute() throws Exception {
    AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
    SuspectIPIndicator<AgentIpAttribute, AgentIpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.feed(agentIpAttribute, agentIpAttributeAccumulator);
  }

  @Test
  void test_filter_botFilter_pass() throws Exception {
    when(mockBotFilter.filter(intermediateSession, 202)).thenReturn(true);
    SuspectIPIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    boolean result = suspectAgentIndicator.filter(intermediateSession, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void test_filter_botFilter_false() throws Exception {
    when(mockBotFilter.filter(intermediateSession, 202)).thenReturn(false);
    SuspectIPIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    intermediateSession.setUserAgent(null);
    boolean result = suspectAgentIndicator.filter(intermediateSession, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void test_filter_notUbiSession() throws Exception {
    AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
    SuspectIPIndicator<AgentIpAttribute, IpAttributeAccumulator> suspectAgentIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    boolean result = suspectAgentIndicator.filter(agentIpAttribute, ipAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }
}
