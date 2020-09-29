package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class SessionDwellMetricsTest extends BaseMetricsTest {

  SessionDwellMetrics sessionDwellMetrics;
  JsonNode yaml;

  @BeforeEach
  void setup() throws Exception {
    sessionDwellMetrics = new SessionDwellMetrics();
    yaml = loadTestCasesYaml("SessionDwellMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, sessionDwellMetrics);
  }
}
