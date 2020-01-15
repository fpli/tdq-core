package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.SiteIdMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class SiteIdMetricsTest extends BaseMetricsTest {
    private SiteIdMetrics siteIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        siteIdMetrics = new SiteIdMetrics();
        yaml = loadTestCasesYaml("SiteIdMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, siteIdMetrics);
    }
}
