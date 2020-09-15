package com.ebay.sojourner.business.ubd.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class WatchCntMetricTest extends BaseMetricsTest {

  private WatchCntMetric watchCntMetric;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    watchCntMetric = new WatchCntMetric();
    yaml = loadTestCasesYaml("WatchCntMetricTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, watchCntMetric);
  }
}
