package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class IpIndicatorsTest {

  @Test
  void test_getInstance() {
    IpIndicators ipIndicators = IpIndicators.getInstance();
    Assertions.assertThat(ipIndicators.indicators.size()).isEqualTo(2);
  }
}
