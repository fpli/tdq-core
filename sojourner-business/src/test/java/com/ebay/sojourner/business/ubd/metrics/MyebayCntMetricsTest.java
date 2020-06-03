package com.ebay.sojourner.business.ubd.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MyebayCntMetricsTest extends BaseMetricsTest {

  private MyebayCntMetrics myebayCntMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    myebayCntMetrics = new MyebayCntMetrics();
    yaml = loadTestCasesYaml("MyebayCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, myebayCntMetrics);
  }
}
