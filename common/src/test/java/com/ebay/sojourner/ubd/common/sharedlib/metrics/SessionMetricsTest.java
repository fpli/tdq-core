package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SessionMetricsTest {

  SessionMetrics sessionMetrics;

  @Test
  void test_getInstance() {
    sessionMetrics = SessionMetrics.getInstance();
    Assertions.assertThat(sessionMetrics.fieldMetrics.size()).isEqualTo(37);
  }
}
