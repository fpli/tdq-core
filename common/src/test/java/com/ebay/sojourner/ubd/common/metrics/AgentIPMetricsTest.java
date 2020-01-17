package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.AgentIPMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

@ExtendWith(MockitoExtension.class)

class AgentIPMetricsTest extends BaseMetricsTest {

    private AgentIPMetrics agentIPMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        agentIPMetrics = new AgentIPMetrics();
        yaml = loadTestCasesYaml("AgentIPMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, agentIPMetrics);
    }
}