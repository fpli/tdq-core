package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class LogdnCntMetricsTest extends BaseMetricsTest {

  private LogdnCntMetrics logdnCntMetrics;

  @BeforeEach
  public void setup() throws Exception {
    logdnCntMetrics = new LogdnCntMetrics();
    yaml = loadTestCasesYaml("LogdnCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, logdnCntMetrics);
  }

  @Test
  void test_start() throws Exception {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setSigninPageCnt(8);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    logdnCntMetrics.start(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getSigninPageCnt()).isEqualTo(0);
  }
}
