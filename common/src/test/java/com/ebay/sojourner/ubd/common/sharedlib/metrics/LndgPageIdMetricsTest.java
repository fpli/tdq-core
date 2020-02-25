package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class LndgPageIdMetricsTest extends BaseMetricsTest {
    private LndgPageIdMetrics lndgPageIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        lndgPageIdMetrics = new LndgPageIdMetrics();
        yaml = loadTestCasesYaml("LndgPageIdMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, lndgPageIdMetrics);
    }
}
