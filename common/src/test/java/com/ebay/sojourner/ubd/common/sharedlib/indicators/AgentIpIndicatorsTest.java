package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AgentIpIndicatorsTest {

    @Test
    void test_getInstance() {
        AgentIpIndicators agentIpIndicators = AgentIpIndicators.getInstance();
        Assertions.assertThat(agentIpIndicators.indicators.size()).isEqualTo(6);
    }
}