package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.AgentStringMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

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
