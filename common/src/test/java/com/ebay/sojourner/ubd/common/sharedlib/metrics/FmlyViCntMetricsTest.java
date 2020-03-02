package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mock;

public class FmlyViCntMetricsTest extends BaseMetricsTest {

  @Mock LkpFetcher lkpFetcher;
  @Mock List<String> viPGT;
  private FmlyViCntMetrics fmlyViCntMetrics;

  @BeforeEach
  public void setup() throws Exception {
    initMocks(this);
    fmlyViCntMetrics = new FmlyViCntMetrics();
    setInternalState(fmlyViCntMetrics, lkpFetcher, viPGT);
    yaml = loadTestCasesYaml("FmlyViCntMetricsTest.yaml");
  }

  @TestFactory
  public Collection<DynamicTest> dynamicTests() throws Exception {
    return generateDynamicTests(yaml, fmlyViCntMetrics);
  }

  @Test
  void test_getImPGT_VI() throws Exception {
    Map<Integer, String[]> pageFmlyMaps = new HashMap<>();
    pageFmlyMaps.put(0, new String[] {"0", "VI"});

    when(lkpFetcher.getPageFmlyMaps()).thenReturn(pageFmlyMaps);
    when(viPGT.contains("abc")).thenReturn(true);

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setApplicationPayload("&pgt=abc");
    ubiEvent.setPageId(1521826);

    UbiSession ubiSession = new UbiSession();
    ubiSession.setFamilyViCnt(3);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);

    fmlyViCntMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getFamilyViCnt()).isEqualTo(4);
  }

  @Test
  void test_getImPGT_VI_2() throws Exception {
    Map<Integer, String[]> pageFmlyMaps = new HashMap<>();
    pageFmlyMaps.put(1521826, new String[] {"0", "VI"});

    when(lkpFetcher.getPageFmlyMaps()).thenReturn(pageFmlyMaps);

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setUrlQueryString("/itm/like?bla");
    ubiEvent.setPageId(2066804);

    UbiSession ubiSession = new UbiSession();
    ubiSession.setFamilyViCnt(3);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);

    fmlyViCntMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getFamilyViCnt()).isEqualTo(4);
  }

  @Test
  void test_getImPGT_GR() throws Exception {
    Map<Integer, String[]> pageFmlyMaps = new HashMap<>();
    pageFmlyMaps.put(1521826, new String[] {"0", "VI"});

    when(lkpFetcher.getPageFmlyMaps()).thenReturn(pageFmlyMaps);

    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setPartialValidPage(true);
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2066804);

    UbiSession ubiSession = new UbiSession();
    ubiSession.setFamilyViCnt(3);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);

    fmlyViCntMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getFamilyViCnt()).isEqualTo(3);
  }

  @Test
  void test_start() throws Exception {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setFamilyViCnt(100);

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    fmlyViCntMetrics.start(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getFamilyViCnt()).isEqualTo(0);
  }
}
