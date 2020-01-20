package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.LogdnCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class LogdnCntMetricsTest extends BaseMetricsTest {
    private LogdnCntMetrics logdnCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        logdnCntMetrics = new LogdnCntMetrics();
        yaml = loadTestCasesYaml("LogdnCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, logdnCntMetrics);
    }
}
