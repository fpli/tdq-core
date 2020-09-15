package com.ebay.sojourner.business.ubd.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SingleClickFlagMetricsTest extends BaseMetricsTest {

  private SingleClickFlagMetrics singleClickFlagMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    singleClickFlagMetrics = new SingleClickFlagMetrics();
    yaml = loadTestCasesYaml("SingleClickFlagMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, singleClickFlagMetrics);
  }
}
