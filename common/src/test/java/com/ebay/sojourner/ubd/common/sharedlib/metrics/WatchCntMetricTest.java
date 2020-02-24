package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class WatchCntMetricTest extends BaseMetricsTest {
    private WatchCntMetric watchCntMetric;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        watchCntMetric = new WatchCntMetric();
        yaml = loadTestCasesYaml("WatchCntMetricTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, watchCntMetric);
    }
}
