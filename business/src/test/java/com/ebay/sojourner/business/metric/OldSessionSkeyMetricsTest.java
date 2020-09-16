package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiSession;
import com.google.common.collect.Sets;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class OldSessionSkeyMetricsTest extends BaseMetricsTest {

  private OldSessionSkeyMetrics oldSessionSkeyMetrics;

  @BeforeEach
  public void setup() throws Exception {
    oldSessionSkeyMetrics = new OldSessionSkeyMetrics();
    yaml = loadTestCasesYaml("OldSessionSkeyMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, oldSessionSkeyMetrics);
  }

  @Test
  void test_start() {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setOldSessionSkey("bla");
    ubiSession.setOldSessionSkeySet(Sets.newHashSet(1L));

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    oldSessionSkeyMetrics.start(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getOldSessionSkey()).isNull();
    Assertions.assertThat(sessionAccumulator.getUbiSession().getOldSessionSkeySet()).isEmpty();
  }

  @Test
  void test_end() {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setOldSessionSkeySet(Sets.newHashSet(1L, 3L));

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    oldSessionSkeyMetrics.end(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getOldSessionSkey()).isEqualTo("1,3");
  }
}
