package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SessionAccumulator;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class MultiColsMetricsTest extends BaseMetricsTest {

  MultiColsMetrics multiColsMetrics;

  @BeforeEach
  void setup() throws Exception {
    multiColsMetrics = new MultiColsMetrics();
    yaml = loadTestCasesYaml("MultiColsMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> test_feed_dynamic() throws Exception {
    return generateDynamicTests(yaml, multiColsMetrics);
  }

  @Test
  void test_start() throws Exception {
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    multiColsMetrics.start(sessionAccumulator);

    assertThat(sessionAccumulator.getUbiSession().getClickId()).isEqualTo(Integer.MAX_VALUE);
    assertThat(sessionAccumulator.getUbiSession().getPageIdForUAIP()).isEqualTo(Integer.MAX_VALUE);
    assertThat(sessionAccumulator.getUbiSession().getHashCode()).isEqualTo(Integer.MAX_VALUE);
  }
}