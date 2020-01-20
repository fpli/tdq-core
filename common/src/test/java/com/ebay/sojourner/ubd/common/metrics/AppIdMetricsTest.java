package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.AppIdMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class AppIdMetricsTest extends BaseMetricsTest {
    private AppIdMetrics appIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        appIdMetrics = new AppIdMetrics();
        yaml = loadTestCasesYaml("AppIdMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, appIdMetrics);
    }
}
