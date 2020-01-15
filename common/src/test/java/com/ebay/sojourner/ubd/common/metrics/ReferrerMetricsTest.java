package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.ReferrerMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class ReferrerMetricsTest extends BaseMetricsTest {
    private ReferrerMetrics referrerMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        referrerMetrics = new ReferrerMetrics();
        yaml = loadTestCasesYaml("ReferrerMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, referrerMetrics);
    }
}
