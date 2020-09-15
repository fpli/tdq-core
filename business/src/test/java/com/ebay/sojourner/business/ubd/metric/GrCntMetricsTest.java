package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiSession;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

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
