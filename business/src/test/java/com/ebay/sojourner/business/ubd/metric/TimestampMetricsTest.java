package com.ebay.sojourner.business.ubd.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class TimestampMetricsTest extends BaseMetricsTest {

  private TimestampMetrics timestampMetrics;
  private JsonNode yaml;


  @BeforeEach
  public void setup() throws Exception {
    timestampMetrics = new TimestampMetrics();
    yaml = loadTestCasesYaml("TimestampMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, timestampMetrics);
  }


}
