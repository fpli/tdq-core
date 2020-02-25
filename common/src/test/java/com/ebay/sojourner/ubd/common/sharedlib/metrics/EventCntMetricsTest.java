package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class EventCntMetricsTest extends BaseMetricsTest {
    private EventCntMetrics eventCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        eventCntMetrics = new EventCntMetrics();
        yaml = loadTestCasesYaml("EventCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, eventCntMetrics);
    }
}
