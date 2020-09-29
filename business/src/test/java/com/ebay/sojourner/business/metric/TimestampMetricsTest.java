package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.business.parser.PageIndicator;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.powermock.reflect.Whitebox;

class TimestampMetricsTest extends BaseMetricsTest {

  TimestampMetrics timestampMetrics;
  JsonNode yaml;

  @BeforeEach
  void setup() throws Exception {
    timestampMetrics = new TimestampMetrics();
    yaml = loadTestCasesYaml("TimestampMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> test_feed_dynamic() throws Exception {
    return generateDynamicTests(yaml, timestampMetrics);
  }


  @Test
  void test_start() throws Exception {
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    sessionAccumulator.getUbiSession().setAbsStartTimestamp(123L);
    sessionAccumulator.getUbiSession().setAbsEndTimestamp(123L);
    sessionAccumulator.getUbiSession().setStartTimestamp(123L);
    sessionAccumulator.getUbiSession().setEndTimestamp(123L);

    timestampMetrics.start(sessionAccumulator);

    assertThat(sessionAccumulator.getUbiSession().getAbsStartTimestamp()).isNull();
    assertThat(sessionAccumulator.getUbiSession().getAbsEndTimestamp()).isNull();
    assertThat(sessionAccumulator.getUbiSession().getStartTimestamp()).isNull();
    assertThat(sessionAccumulator.getUbiSession().getEndTimestamp()).isNull();
  }


  @Test
  void test_init() throws Exception {
    timestampMetrics.init();
    PageIndicator pageIndicator = Whitebox.getInternalState(timestampMetrics, "indicator");
    Set<Integer> agentExcludeSet = Whitebox.getInternalState(timestampMetrics, "agentExcludeSet");
    assertThat(pageIndicator).isNotNull();
    assertThat(agentExcludeSet).isNotNull();
    assertThat(agentExcludeSet.size()).isEqualTo(4);
  }

}
