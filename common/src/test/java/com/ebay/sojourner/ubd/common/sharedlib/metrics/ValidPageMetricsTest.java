package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class ValidPageMetricsTest extends BaseMetricsTest {
    private ValidPageMetrics validPageMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        validPageMetrics = new ValidPageMetrics();
        yaml = loadTestCasesYaml("ValidPageMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, validPageMetrics);
    }
}

