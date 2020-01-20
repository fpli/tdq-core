package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.BinCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class BinCntMetricsTest extends BaseMetricsTest {
    private BinCntMetrics binCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        binCntMetrics = new BinCntMetrics();
        yaml = loadTestCasesYaml("BinCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, binCntMetrics);
    }
}
