package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.CobrandMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class CobrandMetricsTest extends BaseMetricsTest {
    private CobrandMetrics cobrandMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        cobrandMetrics = new CobrandMetrics();
        yaml = loadTestCasesYaml("CobrandMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, cobrandMetrics);
    }
}
