package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.SingleClickFlagMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class SingleClickFlagMetricsTest extends BaseMetricsTest {
    private SingleClickFlagMetrics singleClickFlagMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        singleClickFlagMetrics = new SingleClickFlagMetrics();
        yaml = loadTestCasesYaml("SingleClickFlagMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, singleClickFlagMetrics);
    }
}