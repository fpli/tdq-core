package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BidBinConfirmFlagMetricsTest {

  BidBinConfirmFlagMetrics bidBinConfirmFlagMetrics;

  @BeforeEach
  void setUp() {
    bidBinConfirmFlagMetrics = new BidBinConfirmFlagMetrics();
  }

  @Test
  void test_end() {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setBidCoreCnt(1);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    bidBinConfirmFlagMetrics.end(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getBidBinConfirmFlag()).isTrue();
  }
}
