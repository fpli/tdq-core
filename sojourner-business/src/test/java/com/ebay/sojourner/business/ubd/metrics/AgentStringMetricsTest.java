package com.ebay.sojourner.business.ubd.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class AgentStringMetricsTest extends BaseMetricsTest {

  private AgentStringMetrics agentStringMetrics;
  private JsonNode yaml;

  @BeforeEach
  public void setup() throws Exception {
    agentStringMetrics = new AgentStringMetrics();
    yaml = loadTestCasesYaml("AgentStringMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, agentStringMetrics);
  }
}
