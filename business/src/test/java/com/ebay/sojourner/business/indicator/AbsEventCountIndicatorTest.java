package com.ebay.sojourner.business.indicator;

import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.common.model.SessionCore;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsEventCountIndicatorTest {

  AbsEventCountIndicator absEventCountIndicator;
  GuidAttributeAccumulator guidAttributeAccumulator;
  SessionCore intermediateSession;

  @BeforeEach
  void setup() {
    absEventCountIndicator = new AbsEventCountIndicator();
    guidAttributeAccumulator = new GuidAttributeAccumulator();
    intermediateSession = new SessionCore();
  }

  @Test
  void test_start() throws Exception {
    absEventCountIndicator.start(guidAttributeAccumulator);
    Assertions.assertThat(guidAttributeAccumulator.getGuidAttribute().getAbsEventCount())
        .isEqualTo(0);
  }

  @Test
  void test_feed() throws Exception {
    absEventCountIndicator.feed(intermediateSession, guidAttributeAccumulator);
  }

  @Test
  void test_filter() throws Exception {
    absEventCountIndicator.filter(intermediateSession, guidAttributeAccumulator);
  }
}
