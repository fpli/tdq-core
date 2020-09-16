package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class BinCntMetricsTest extends BaseMetricsTest {

  private BinCntMetrics binCntMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    binCntMetrics = new BinCntMetrics();
    yaml = loadTestCasesYaml("BinCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, binCntMetrics);
  }
}
