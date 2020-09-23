package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class SingleClickFlagMetricsTest extends BaseMetricsTest {

  SingleClickFlagMetrics singleClickFlagMetrics;
  JsonNode yaml;

  @BeforeEach
  void setup() throws Exception {
    singleClickFlagMetrics = new SingleClickFlagMetrics();
    yaml = loadTestCasesYaml("SingleClickFlagMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, singleClickFlagMetrics);
  }
}
