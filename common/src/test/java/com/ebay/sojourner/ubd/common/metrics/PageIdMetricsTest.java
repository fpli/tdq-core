package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.PageIdMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class PageIdMetricsTest extends BaseMetricsTest {
    private PageIdMetrics pageIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        pageIdMetrics = new PageIdMetrics();
        yaml = loadTestCasesYaml("PageIdMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, pageIdMetrics);
    }
}
