package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class SessionDwellMetricsTest extends BaseMetricsTest {

    private SessionDwellMetrics sessionDwellMetrics;
    private JsonNode yaml;

    @BeforeEach
    public void setup() throws Exception {
        sessionDwellMetrics = new SessionDwellMetrics();
        yaml = loadTestCasesYaml("SessionDwellMetricsTest.yaml");
    }


    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, sessionDwellMetrics);
    }

}
