package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuidIndicatorsTest {

    @Test
    void test_getInstance() {
        GuidIndicators guidIndicators = GuidIndicators.getInstance();
        Assertions.assertThat(guidIndicators.indicators.size()).isEqualTo(1);
    }
}