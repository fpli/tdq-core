package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.SessionForGuidEnhancement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsEventCountIndicatorTest {

  AbsEventCountIndicator absEventCountIndicator;
  GuidAttributeAccumulator guidAttributeAccumulator;
  SessionForGuidEnhancement sessionForGuidEnhancement;

  @BeforeEach
  void setup() {
    absEventCountIndicator = new AbsEventCountIndicator();
    guidAttributeAccumulator = new GuidAttributeAccumulator();
    sessionForGuidEnhancement = new SessionForGuidEnhancement();
  }

  @Test
  void test_start() throws Exception {
    absEventCountIndicator.start(guidAttributeAccumulator);
    Assertions.assertThat(guidAttributeAccumulator.getGuidAttribute().getAbsEventCount())
        .isEqualTo(0);
  }

  @Test
  void test_feed() throws Exception {
    absEventCountIndicator.feed(sessionForGuidEnhancement, guidAttributeAccumulator, true);
  }

  @Test
  void test_filter() throws Exception {
    absEventCountIndicator.filter(sessionForGuidEnhancement, guidAttributeAccumulator);
  }
}
