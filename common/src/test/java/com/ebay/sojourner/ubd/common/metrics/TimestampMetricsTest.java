package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.TimestampMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class TimestampMetricsTest extends BaseMetricsTest {
    private TimestampMetrics timestampMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        timestampMetrics = new TimestampMetrics();
        yaml = loadTestCasesYaml("TimestampMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, timestampMetrics);
    }
}
