package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.BitUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class SiteFlagMetricsTest extends BaseMetricsTest {

  SiteFlagMetrics siteFlagMetrics;

  @BeforeEach
  void setup() throws Exception {
    siteFlagMetrics = new SiteFlagMetrics();
  }

  @TestFactory
  Collection<DynamicTest> dynamicTests() throws Exception {
    List<DynamicTest> tests = Lists.newArrayList();

    UbiSession ubiSession = new UbiSession();
    SessionAccumulator sessionAccumulator = new SessionAccumulator();
    sessionAccumulator.setUbiSession(ubiSession);

    ArrayList<Integer> siteIds = Lists.newArrayList(
            998, 0, 1, 2, 3, 15, 16, 23, 37, 71, 77, 100, 101, 104, 123, 146, 186, 193,
            196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213,
            214, 215, 216, 217, 218, 219, 220, 221, 223, 224, 225);

    for (int i = 0; i < siteIds.size(); i++) {
      final int idx = i;
      UbiEvent ubiEvent = new UbiEvent();
      ubiEvent.setSiteId(siteIds.get(idx));
      DynamicTest dynamicTest =
          DynamicTest.dynamicTest(
              "test siteId " + siteIds.get(idx),
              () -> {
                long siteFlags = 0;
                siteFlags = BitUtils.setBit(siteFlags, idx);
                siteFlagMetrics.start(sessionAccumulator);
                siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
                Assertions.assertThat(sessionAccumulator.getUbiSession().getSiteFlags())
                    .isEqualTo(siteFlags);
              });
      tests.add(dynamicTest);
    }

    List<Integer> siteIdsForFlag0 = Lists.newArrayList(
        4, 5, 6, 7, 8, 20, 163, 500, 970, 971, 972, 973, 980, 981, 982, 983, 984, 985,
        986, 987, 989, 990, 991, 992, 993, 994, 995, 996, 997);

    for (int i = 0; i < siteIdsForFlag0.size(); i++) {
      final int idx = i;
      UbiEvent ubiEvent = new UbiEvent();
      ubiEvent.setSiteId(siteIdsForFlag0.get(idx));
      DynamicTest dynamicTest =
          DynamicTest.dynamicTest(
              "test siteId " + siteIdsForFlag0.get(idx),
              () -> {
                long siteFlags = 0;
                siteFlags = BitUtils.setBit(siteFlags, 0);
                siteFlagMetrics.start(sessionAccumulator);
                siteFlagMetrics.feed(ubiEvent, sessionAccumulator);
                Assertions.assertThat(sessionAccumulator.getUbiSession().getSiteFlags())
                          .isEqualTo(siteFlags);
              });
      tests.add(dynamicTest);
    }

    return tests;
  }
}
