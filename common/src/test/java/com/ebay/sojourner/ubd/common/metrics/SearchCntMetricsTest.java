package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.SearchCntMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class SearchCntMetricsTest extends BaseMetricsTest {
    private SearchCntMetrics searchCntMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        searchCntMetrics = new SearchCntMetrics();
        yaml = loadTestCasesYaml("SearchCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, searchCntMetrics);
    }
}
