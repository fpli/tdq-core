package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.sharedlib.metrics.UserIdMetrics;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class UserIdMetricsTest extends BaseMetricsTest {
    private UserIdMetrics userIdMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        userIdMetrics = new UserIdMetrics();
        yaml = loadTestCasesYaml("UserIdMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> test_feed() throws Exception {
        return generateDynamicTests(yaml, userIdMetrics);
    }
}
