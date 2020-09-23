package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SessionAccumulator;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class SrpCntMetricsTest extends BaseMetricsTest {

  SrpCntMetrics srpCntMetrics;

  @BeforeEach
  void setup() throws Exception {
    srpCntMetrics = new SrpCntMetrics();
    yaml = loadTestCasesYaml("SrpCntMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> test_feed_dynamic() throws Exception {
    return generateDynamicTests(yaml, srpCntMetrics);
  }

  @Test
  void test_start() throws Exception {
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    sessionAccumulator.getUbiSession().setSrpCnt(100);

    srpCntMetrics.start(sessionAccumulator);

    assertThat(sessionAccumulator.getUbiSession().getSrpCnt()).isEqualTo(0);
  }

}