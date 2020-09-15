package com.ebay.sojourner.business.ubd.indicator;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class SuspectAgentIndicatorTest {

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  AgentAttributeAccumulator agentAttributeAccumulator;
  IntermediateSession intermediateSession;
  @Mock
  UbiBotFilter mockBotFilter;

  @BeforeEach
  void setup() {
    initMocks(this);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    agentAttributeAccumulator = new AgentAttributeAccumulator();
    intermediateSession = new IntermediateSession();
  }

  @Test
  void test_start_AgentIpAttributeAccumulator() throws Exception {
    SuspectAgentIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    suspectAgentIndicator.start(agentIpAttributeAccumulator);
  }

  @Test
  void test_start_AgentAttributeAccumulator() throws Exception {
    SuspectAgentIndicator<IntermediateSession, AgentAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    suspectAgentIndicator.start(agentAttributeAccumulator);
  }

  @Test
  void test_feed_UbiSession() throws Exception {
    SuspectAgentIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    suspectAgentIndicator.feed(intermediateSession, agentIpAttributeAccumulator);
  }

  @Test
  void test_feed_AgentIpAttribute() throws Exception {
    AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
    SuspectAgentIndicator<AgentIpAttribute, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    suspectAgentIndicator.feed(agentIpAttribute, agentIpAttributeAccumulator);
  }

  @Test
  void test_filter_botFilter_pass() throws Exception {
    when(mockBotFilter.filter(intermediateSession, 202)).thenReturn(true);
    SuspectAgentIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    boolean result = suspectAgentIndicator.filter(intermediateSession, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void test_filter_botFilter_false() throws Exception {
    when(mockBotFilter.filter(intermediateSession, 202)).thenReturn(false);
    SuspectAgentIndicator<IntermediateSession, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    intermediateSession.setUserAgent(null);
    boolean result = suspectAgentIndicator.filter(intermediateSession, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void test_filter_notUbiSession() throws Exception {
    AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
    SuspectAgentIndicator<AgentIpAttribute, AgentAttributeAccumulator> suspectAgentIndicator =
        new SuspectAgentIndicator<>(mockBotFilter);
    boolean result = suspectAgentIndicator.filter(agentIpAttribute, agentAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }
}
