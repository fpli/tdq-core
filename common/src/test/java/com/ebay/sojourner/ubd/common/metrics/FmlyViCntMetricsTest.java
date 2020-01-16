package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.FmlyViCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class FmlyViCntMetricsTest extends BaseMetricsTest {
    private FmlyViCntMetrics fmlyViCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        fmlyViCntMetrics = new FmlyViCntMetrics();
        yaml = loadTestCasesYaml("FmlyViCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, fmlyViCntMetrics);
    }
}
