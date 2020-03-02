package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AgentIndicatorsTest {

    @Test
    void test_getInstance() {
        AgentIndicators agentIndicators = AgentIndicators.getInstance();
        Assertions.assertThat(agentIndicators.indicators.size()).isEqualTo(2);
    }
}