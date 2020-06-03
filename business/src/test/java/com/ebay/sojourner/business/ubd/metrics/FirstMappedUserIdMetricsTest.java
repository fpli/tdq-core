package com.ebay.sojourner.business.ubd.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class FirstMappedUserIdMetricsTest extends BaseMetricsTest {

  private FirstMappedUserIdMetrics firstMappedUserIdMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    firstMappedUserIdMetrics = new FirstMappedUserIdMetrics();
    yaml = loadTestCasesYaml("FirstMappedUserIdMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, firstMappedUserIdMetrics);
  }
}
