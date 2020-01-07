package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.EventCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

public class EventCntMetricsTest extends BaseMetricsTest{
    private EventCntMetrics eventCntMetrics;
    private Pair<JsonNode, List<MetricsTestCase>> pair;

    @BeforeEach
    public void setup() throws Exception {
        eventCntMetrics = new EventCntMetrics();
        pair = loadTestCases("EventCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(pair.getRight(), pair.getLeft(), eventCntMetrics);
    }
}
