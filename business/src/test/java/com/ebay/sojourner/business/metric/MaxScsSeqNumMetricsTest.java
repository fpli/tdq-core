package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MaxScsSeqNumMetricsTest extends BaseMetricsTest {

  private MaxScsSeqNumMetrics maxScsSeqNumMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    maxScsSeqNumMetrics = new MaxScsSeqNumMetrics();
    yaml = loadTestCasesYaml("MaxScsSeqNumMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, maxScsSeqNumMetrics);
  }
}
