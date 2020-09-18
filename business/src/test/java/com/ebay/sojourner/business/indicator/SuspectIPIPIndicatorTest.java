package com.ebay.sojourner.business.indicator;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.util.BotRules;
import com.ebay.sojourner.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuspectIPIPIndicatorTest {

  SuspectIPIPIndicator suspectIPIPIndicator;

  AgentIpAttribute agentIpAttribute;
  IpAttributeAccumulator ipAttributeAccumulator;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  IpAttribute mockIpAttribute = mock(IpAttribute.class);

  @BeforeEach
  void setUp() {
    suspectIPIPIndicator = new SuspectIPIPIndicator(mockBotFilter);
    ipAttributeAccumulator = new IpAttributeAccumulator();
    ipAttributeAccumulator.setIpAttribute(mockIpAttribute);
    agentIpAttribute = new AgentIpAttribute();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockIpAttribute).clear();
    suspectIPIPIndicator.start(ipAttributeAccumulator);
    verify(mockIpAttribute, times(1)).clear();
  }

  @Test
  void test_feed() throws Exception {
    doNothing().when(mockIpAttribute).feed(agentIpAttribute, BotRules.SUSPECTED_IP_ON_AGENT);
    suspectIPIPIndicator.feed(agentIpAttribute, ipAttributeAccumulator);
    verify(mockIpAttribute, times(1)).feed(agentIpAttribute, BotRules.SUSPECTED_IP_ON_AGENT);
  }


  @Test
  void test_filter() throws Exception {
    boolean result = suspectIPIPIndicator.filter(agentIpAttribute, ipAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }
}