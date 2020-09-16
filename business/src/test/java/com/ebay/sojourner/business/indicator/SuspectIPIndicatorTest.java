package com.ebay.sojourner.business.indicator;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@Disabled
class SuspectIPIndicatorTest {

  AgentIpAttributeAccumulator agentIpAttributeAccumulator;
  IpAttributeAccumulator ipAttributeAccumulator;
  SessionCore sessionCore;
  @Mock
  UbiBotFilter mockBotFilter;

  @BeforeEach
  void setup() {
    initMocks(this);
    agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
    ipAttributeAccumulator = new IpAttributeAccumulator();
    sessionCore = new SessionCore();
  }

  @Test
  void test_start_AgentIpAttributeAccumulator() throws Exception {
    SuspectIPIndicator<SessionCore, AgentIpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.start(agentIpAttributeAccumulator);
  }

  @Test
  void test_start_IpAttributeAccumulator() throws Exception {
    SuspectIPIndicator<SessionCore, IpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.start(ipAttributeAccumulator);
  }

  @Test
  void test_feed_UbiSession() throws Exception {
    SuspectIPIndicator<SessionCore, AgentIpAttributeAccumulator> suspectIPIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    suspectIPIndicator.feed(sessionCore, agentIpAttributeAccumulator);
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
    when(mockBotFilter.filter(sessionCore, 202)).thenReturn(true);
    SuspectIPIndicator<SessionCore, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    boolean result = suspectAgentIndicator.filter(sessionCore, agentIpAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void test_filter_botFilter_false() throws Exception {
    when(mockBotFilter.filter(sessionCore, 202)).thenReturn(false);
    SuspectIPIndicator<SessionCore, AgentIpAttributeAccumulator> suspectAgentIndicator =
        new SuspectIPIndicator<>(mockBotFilter);
    sessionCore.setUserAgent(null);
    boolean result = suspectAgentIndicator.filter(sessionCore, agentIpAttributeAccumulator);
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
