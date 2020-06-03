package com.ebay.sojourner.business.ubd.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class HomepgCntMetricsTest extends BaseMetricsTest {

  private HomepgCntMetrics homepgCntMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    homepgCntMetrics = new HomepgCntMetrics();
    yaml = loadTestCasesYaml("HomepgCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, homepgCntMetrics);
  }
}
