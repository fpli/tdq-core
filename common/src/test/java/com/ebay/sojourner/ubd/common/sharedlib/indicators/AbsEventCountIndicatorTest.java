package com.ebay.sojourner.ubd.common.sharedlib.indicators;

import com.ebay.sojourner.ubd.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbsEventCountIndicatorTest {

  AbsEventCountIndicator absEventCountIndicator;
  GuidAttributeAccumulator guidAttributeAccumulator;
  UbiSession ubiSession;

  @BeforeEach
  void setup() {
    absEventCountIndicator = new AbsEventCountIndicator();
    guidAttributeAccumulator = new GuidAttributeAccumulator();
    ubiSession = new UbiSession();
  }

  @Test
  void test_start() throws Exception {
    absEventCountIndicator.start(guidAttributeAccumulator);
    Assertions.assertThat(guidAttributeAccumulator.getGuidAttribute().getAbsEventCount())
        .isEqualTo(0);
  }

  @Test
  void test_feed() throws Exception {
    absEventCountIndicator.feed(ubiSession, guidAttributeAccumulator, true);
  }

  @Test
  void test_filter() throws Exception {
    absEventCountIndicator.filter(ubiSession, guidAttributeAccumulator);
  }
}
