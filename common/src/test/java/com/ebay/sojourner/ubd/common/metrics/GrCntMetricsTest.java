package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.GrCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class GrCntMetricsTest extends BaseMetricsTest {
    private GrCntMetrics grCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        grCntMetrics = new GrCntMetrics();
        yaml = loadTestCasesYaml("GrCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, grCntMetrics);
    }
}
