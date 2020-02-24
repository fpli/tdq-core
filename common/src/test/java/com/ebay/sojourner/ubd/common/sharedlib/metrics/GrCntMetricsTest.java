package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;

public class GrCntMetricsTest extends BaseMetricsTest {

    private GrCntMetrics grCntMetrics;

    @BeforeEach
    public void setup() throws Exception {
        grCntMetrics = new GrCntMetrics();
        yaml = loadTestCasesYaml("GrCntMetricsTest.yaml");
    }

    @TestFactory
    public Collection<DynamicTest> dynamicTests() throws Exception {
        return generateDynamicTests(yaml, grCntMetrics);
    }

    @Test
    void test_start() throws Exception {
        UbiSession ubiSession = new UbiSession();
        ubiSession.setGrCnt(8);

        SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
        grCntMetrics.start(sessionAccumulator);

        Assertions.assertThat(sessionAccumulator.getUbiSession().getGrCnt()).isEqualTo(0);
    }
}
