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

class ScsCntForBot7IPIndicatorTest {

  ScsCntForBot7IPIndicator scsCntForBot7IPIndicator;

  AgentIpAttribute agentIpAttribute;
  IpAttributeAccumulator ipAttributeAccumulator;
  UbiBotFilter mockBotFilter = mock(UbiBotFilter.class);
  IpAttribute mockIpAttribute = mock(IpAttribute.class);

  @BeforeEach
  void setUp() {
    scsCntForBot7IPIndicator = new ScsCntForBot7IPIndicator(mockBotFilter);
    ipAttributeAccumulator = new IpAttributeAccumulator();
    ipAttributeAccumulator.setIpAttribute(mockIpAttribute);
    agentIpAttribute = new AgentIpAttribute();
  }

  @Test
  void test_start() throws Exception {
    doNothing().when(mockIpAttribute).clear();
    scsCntForBot7IPIndicator.start(ipAttributeAccumulator);
    verify(mockIpAttribute, times(1)).clear();
  }

  @Test
  void test_feed() throws Exception {
    doNothing().when(mockIpAttribute).feed(agentIpAttribute, BotRules.SCS_ON_IP);
    scsCntForBot7IPIndicator.feed(agentIpAttribute, ipAttributeAccumulator);
    verify(mockIpAttribute, times(1)).feed(agentIpAttribute, BotRules.SCS_ON_IP);
  }


  @Test
  void test_filter() throws Exception {
    boolean result = scsCntForBot7IPIndicator.filter(agentIpAttribute, ipAttributeAccumulator);
    Assertions.assertThat(result).isFalse();
  }

}