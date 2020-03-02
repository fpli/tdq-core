package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

class SuspectIPIndicatorTest {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator;
    IpAttributeAccumulator ipAttributeAccumulator;
    UbiSession ubiSession;
    @Mock UbiBotFilter mockBotFilter;

    @BeforeEach
    void setup() {
        initMocks(this);
        agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
        ipAttributeAccumulator = new IpAttributeAccumulator();
        ubiSession = new UbiSession();
    }

    @Test
    void test_start_AgentIpAttributeAccumulator() throws Exception {
        SuspectIPIndicator<UbiSession, AgentIpAttributeAccumulator> suspectIPIndicator = new SuspectIPIndicator<>(mockBotFilter);
        suspectIPIndicator.start(agentIpAttributeAccumulator);
    }

    @Test
    void test_start_IpAttributeAccumulator() throws Exception {
        SuspectIPIndicator<UbiSession, IpAttributeAccumulator> suspectIPIndicator = new SuspectIPIndicator<>(mockBotFilter);
        suspectIPIndicator.start(ipAttributeAccumulator);
    }

    @Test
    void test_feed_UbiSession() throws Exception {
        SuspectIPIndicator<UbiSession, AgentIpAttributeAccumulator> suspectIPIndicator = new SuspectIPIndicator<>(mockBotFilter);
        suspectIPIndicator.feed(ubiSession, agentIpAttributeAccumulator, true);
    }

    @Test
    void test_feed_AgentIpAttribute() throws Exception {
        AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
        SuspectIPIndicator<AgentIpAttribute, IpAttributeAccumulator> suspectIPIndicator = new SuspectIPIndicator<>(mockBotFilter);
        suspectIPIndicator.feed(agentIpAttribute, ipAttributeAccumulator, true);
    }

    @Test
    void test_filter_botFilter_pass() throws Exception {
        when(mockBotFilter.filter(ubiSession, 202)).thenReturn(true);
        SuspectIPIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectIPIndicator<>(mockBotFilter);
        boolean result = suspectAgentIndicator.filter(ubiSession, agentIpAttributeAccumulator);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void test_filter_botFilter_false() throws Exception {
        when(mockBotFilter.filter(ubiSession, 202)).thenReturn(false);
        SuspectIPIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectIPIndicator<>(mockBotFilter);
        ubiSession.setUserAgent(null);
        boolean result = suspectAgentIndicator.filter(ubiSession, agentIpAttributeAccumulator);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void test_filter_notUbiSession() throws Exception {
        AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
        SuspectIPIndicator<AgentIpAttribute, IpAttributeAccumulator> suspectAgentIndicator = new SuspectIPIndicator<>(mockBotFilter);
        boolean result = suspectAgentIndicator.filter(agentIpAttribute, ipAttributeAccumulator);
        Assertions.assertThat(result).isFalse();
    }
}