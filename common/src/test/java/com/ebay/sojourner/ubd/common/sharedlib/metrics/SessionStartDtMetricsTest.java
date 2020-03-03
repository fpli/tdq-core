package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SessionStartDtMetricsTest extends BaseMetricsTest {
  private SessionStartDtMetrics sessionStartDtMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    sessionStartDtMetrics = new SessionStartDtMetrics();
    yaml = loadTestCasesYaml("SessionStartDtMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, sessionStartDtMetrics);
  }
}
