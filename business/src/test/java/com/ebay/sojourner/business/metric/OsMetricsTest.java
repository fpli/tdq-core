package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SessionAccumulator;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class OsMetricsTest extends BaseMetricsTest {

  OsMetrics osMetrics;

  @BeforeEach
  void setup() throws Exception {
    osMetrics = new OsMetrics();
    yaml = loadTestCasesYaml("OsMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> test_feed_dynamic() throws Exception {
    return generateDynamicTests(yaml, osMetrics);
  }

  @Test
  void test_start() throws Exception {
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    sessionAccumulator.getUbiSession().setOsFamily("abc");
    sessionAccumulator.getUbiSession().setOsVersion("100");

    osMetrics.start(sessionAccumulator);

    assertThat(sessionAccumulator.getUbiSession().getOsFamily()).isNull();
    assertThat(sessionAccumulator.getUbiSession().getOsVersion()).isNull();
  }
}