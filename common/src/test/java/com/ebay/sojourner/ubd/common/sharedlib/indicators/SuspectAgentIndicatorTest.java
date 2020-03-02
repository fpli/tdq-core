package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.util.UbiBotFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

class SuspectAgentIndicatorTest {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator;
    AgentAttributeAccumulator agentAttributeAccumulator;
    UbiSession ubiSession;
    @Mock UbiBotFilter mockBotFilter;

    @BeforeEach
    void setup() {
        initMocks(this);
        agentIpAttributeAccumulator = new AgentIpAttributeAccumulator();
        agentAttributeAccumulator = new AgentAttributeAccumulator();
        ubiSession = new UbiSession();
    }

    @Test
    void test_start_AgentIpAttributeAccumulator() throws Exception {
        SuspectAgentIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        suspectAgentIndicator.start(agentIpAttributeAccumulator);
    }

    @Test
    void test_start_AgentAttributeAccumulator() throws Exception {
        SuspectAgentIndicator<UbiSession, AgentAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        suspectAgentIndicator.start(agentAttributeAccumulator);
    }

    @Test
    void test_feed_UbiSession() throws Exception {
        SuspectAgentIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        suspectAgentIndicator.feed(ubiSession, agentIpAttributeAccumulator, true);
    }

    @Test
    void test_feed_AgentIpAttribute() throws Exception {
        AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
        SuspectAgentIndicator<AgentIpAttribute, AgentAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        suspectAgentIndicator.feed(agentIpAttribute, agentAttributeAccumulator, true);
    }

    @Test
    void test_filter_botFilter_pass() throws Exception {
        when(mockBotFilter.filter(ubiSession, 202)).thenReturn(true);
        SuspectAgentIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        boolean result = suspectAgentIndicator.filter(ubiSession, agentIpAttributeAccumulator);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void test_filter_botFilter_false() throws Exception {
        when(mockBotFilter.filter(ubiSession, 202)).thenReturn(false);
        SuspectAgentIndicator<UbiSession, AgentIpAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        ubiSession.setUserAgent(null);
        boolean result = suspectAgentIndicator.filter(ubiSession, agentIpAttributeAccumulator);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void test_filter_notUbiSession() throws Exception {
        AgentIpAttribute agentIpAttribute = new AgentIpAttribute();
        SuspectAgentIndicator<AgentIpAttribute, AgentAttributeAccumulator> suspectAgentIndicator = new SuspectAgentIndicator<>(mockBotFilter);
        boolean result = suspectAgentIndicator.filter(agentIpAttribute, agentAttributeAccumulator);
        Assertions.assertThat(result).isFalse();
    }
}