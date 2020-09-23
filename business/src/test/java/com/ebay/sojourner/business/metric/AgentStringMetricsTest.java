package com.ebay.sojourner.business.metric;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class AgentStringMetricsTest extends BaseMetricsTest {

  private AgentStringMetrics agentStringMetrics;
  private JsonNode yaml;

  @BeforeEach
  void setup() throws Exception {
    agentStringMetrics = new AgentStringMetrics();
    yaml = loadTestCasesYaml("AgentStringMetricsTest.yaml");
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, agentStringMetrics);
  }
}
