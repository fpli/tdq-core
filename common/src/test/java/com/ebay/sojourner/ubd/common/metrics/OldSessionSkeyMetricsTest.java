package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.OldSessionSkeyMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class OldSessionSkeyMetricsTest extends BaseMetricsTest {
    private OldSessionSkeyMetrics oldSessionSkeyMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        oldSessionSkeyMetrics = new OldSessionSkeyMetrics();
        yaml = loadTestCasesYaml("OldSessionSkeyMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, oldSessionSkeyMetrics);
    }
}
