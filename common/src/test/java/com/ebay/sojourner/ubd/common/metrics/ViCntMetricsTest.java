package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.ViCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class ViCntMetricsTest extends BaseMetricsTest {
    private ViCntMetrics viCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        viCntMetrics = new ViCntMetrics();
        yaml = loadTestCasesYaml("ViCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, viCntMetrics);
    }
}
