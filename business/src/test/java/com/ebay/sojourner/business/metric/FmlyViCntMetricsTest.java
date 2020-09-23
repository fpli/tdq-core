package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.LkpManager;
import com.ebay.sojourner.common.util.PropertyUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropertyUtils.class, LkpManager.class })
public class FmlyViCntMetricsTest {

  FmlyViCntMetrics fmlyViCntMetrics;

  SessionAccumulator sessionAccumulator;
  UbiEvent event;

  @Before
  public void setup() throws Exception {
    fmlyViCntMetrics = new FmlyViCntMetrics();
    sessionAccumulator = new SessionAccumulator();
    event = new UbiEvent();
    mockStatic(LkpManager.class);
    mockStatic(PropertyUtils.class);
  }

  @Test
  public void test_init() throws Exception {
    List<String> strings = Lists.newArrayList("abc");
    when(PropertyUtils.parseProperty(any(), any())).thenReturn(strings);
    fmlyViCntMetrics.init();

    List<String> viPGT = Whitebox.getInternalState(fmlyViCntMetrics, "viPGT");
    assertThat(viPGT).contains("abc");
  }

  @Test
  public void test_start() throws Exception {
    sessionAccumulator.getUbiSession().setFamilyViCnt(100);
    fmlyViCntMetrics.start(sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(0);
  }


  @Test
  public void test_feed_pageFmly_VI() throws Exception {
    // prepare data
    Map<Integer, String[]> map = new HashMap<>();
    map.put(1, new String[]{"abc", "VI"});

    event.setPartialValidPage(true);
    event.setRdt(false);
    event.setIframe(false);
    event.setPageId(1);

    sessionAccumulator.getUbiSession().setFamilyViCnt(100);

    LkpManager mockLkpMgr = mock(LkpManager.class);
    when(LkpManager.getInstance()).thenReturn(mockLkpMgr);
    when(mockLkpMgr.getPageFmlyMaps()).thenReturn(map);

    // invoke
    fmlyViCntMetrics.feed(event, sessionAccumulator);

    // assertions
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(101);
  }

  @Test
  public void test_feed_pageFmly_VIGR() throws Exception {
    // prepare data
    Map<Integer, String[]> map = new HashMap<>();
    map.put(1, new String[]{"abc", "GR/VI"});

    event.setPartialValidPage(true);
    event.setRdt(false);
    event.setIframe(false);
    event.setPageId(1);

    sessionAccumulator.getUbiSession().setFamilyViCnt(100);

    LkpManager mockLkpMgr = mock(LkpManager.class);
    when(LkpManager.getInstance()).thenReturn(mockLkpMgr);
    when(mockLkpMgr.getPageFmlyMaps()).thenReturn(map);

    // invoke
    fmlyViCntMetrics.feed(event, sessionAccumulator);

    // assertions
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(101);
  }


  @Test
  public void test_feed_getImPGT_VI_page1521826() throws Exception {
    // prepare data
    Map<Integer, String[]> map = new HashMap<>();
    map.put(1, new String[]{"abc", "GR/VI"});

    event.setPartialValidPage(true);
    event.setRdt(false);
    event.setIframe(false);
    event.setPageId(1521826);
    event.setApplicationPayload("pgt=abc");

    sessionAccumulator.getUbiSession().setFamilyViCnt(100);

    Whitebox.setInternalState(fmlyViCntMetrics, "viPGT", Lists.newArrayList("abc"));

    LkpManager mockLkpMgr = mock(LkpManager.class);
    when(LkpManager.getInstance()).thenReturn(mockLkpMgr);
    when(mockLkpMgr.getPageFmlyMaps()).thenReturn(map);

    // invoke
    fmlyViCntMetrics.feed(event, sessionAccumulator);

    // assertions
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(101);
  }


  @Test
  public void test_feed_getImPGT_VI_page2066804() throws Exception {
    // prepare data
    Map<Integer, String[]> map = new HashMap<>();
    map.put(1, new String[]{"abc", "GR/VI"});

    event.setPartialValidPage(true);
    event.setRdt(false);
    event.setIframe(false);
    event.setPageId(2066804);
    event.setUrlQueryString("/itm/like/bla...");

    sessionAccumulator.getUbiSession().setFamilyViCnt(100);

    LkpManager mockLkpMgr = mock(LkpManager.class);
    when(LkpManager.getInstance()).thenReturn(mockLkpMgr);
    when(mockLkpMgr.getPageFmlyMaps()).thenReturn(map);

    // invoke
    fmlyViCntMetrics.feed(event, sessionAccumulator);

    // assertions
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(101);
  }


  @Test
  public void test_feed_getImPGT_GR_page2066804() throws Exception {
    // prepare data
    Map<Integer, String[]> map = new HashMap<>();
    map.put(1, new String[]{"abc", "GR/VI"});

    event.setPartialValidPage(true);
    event.setRdt(false);
    event.setIframe(false);
    event.setPageId(2066804);

    sessionAccumulator.getUbiSession().setFamilyViCnt(100);

    LkpManager mockLkpMgr = mock(LkpManager.class);
    when(LkpManager.getInstance()).thenReturn(mockLkpMgr);
    when(mockLkpMgr.getPageFmlyMaps()).thenReturn(map);

    // invoke
    fmlyViCntMetrics.feed(event, sessionAccumulator);

    // assertions
    assertThat(sessionAccumulator.getUbiSession()
                                 .getFamilyViCnt()).isEqualTo(100);
  }
}
