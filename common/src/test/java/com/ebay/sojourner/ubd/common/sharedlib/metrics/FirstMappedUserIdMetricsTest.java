package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class FirstMappedUserIdMetricsTest extends BaseMetricsTest {
    private FirstMappedUserIdMetrics firstMappedUserIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        firstMappedUserIdMetrics = new FirstMappedUserIdMetrics();
        yaml = loadTestCasesYaml("FirstMappedUserIdMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, firstMappedUserIdMetrics);
    }
}
