package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.Gr1CntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class Gr1CntMetricsTest extends BaseMetricsTest {
    private Gr1CntMetrics gr1CntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        gr1CntMetrics = new Gr1CntMetrics();
        yaml = loadTestCasesYaml("Gr1CntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, gr1CntMetrics);
    }
}
