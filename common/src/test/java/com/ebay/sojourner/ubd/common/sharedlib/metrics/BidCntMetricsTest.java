package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BidCntMetricsTest extends BaseMetricsTest {

  private BidCntMetrics bidCntMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    bidCntMetrics = new BidCntMetrics();
    yaml = loadTestCasesYaml("BidCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, bidCntMetrics);
  }
}
