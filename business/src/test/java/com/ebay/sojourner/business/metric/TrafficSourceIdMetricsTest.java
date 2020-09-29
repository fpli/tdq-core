package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.IntermediateMetrics;
import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

class TrafficSourceIdMetricsTest {

  TrafficSourceIdMetrics trafficSourceIdMetrics;
  SessionAccumulator sessionAccumulator;
  UbiSession mockSession = mock(UbiSession.class);
  IntermediateMetrics mockIntermediateMetrics = mock(IntermediateMetrics.class);
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    trafficSourceIdMetrics = new TrafficSourceIdMetrics();
    sessionAccumulator = new SessionAccumulator(mockSession);
    when(mockSession.getIntermediateMetrics()).thenReturn(mockIntermediateMetrics);
    ubiEvent = new UbiEvent();
  }

  @Test
  void end() {
  }

  @Test
  void feed() throws Exception {
    doNothing().when(mockIntermediateMetrics).feed(ubiEvent, sessionAccumulator);
    trafficSourceIdMetrics.feed(ubiEvent, sessionAccumulator);
    verify(mockIntermediateMetrics, times(1)).feed(ubiEvent, sessionAccumulator);
  }

  @Test
  void init() throws Exception {
    trafficSourceIdMetrics.init();
    Set<Integer> landPageSet1 = Whitebox.getInternalState(trafficSourceIdMetrics, "landPageSet1");
    Set<Integer> landPageSet2 = Whitebox.getInternalState(trafficSourceIdMetrics, "landPageSet2");
    Set<Integer> swdSet = Whitebox.getInternalState(trafficSourceIdMetrics, "swdSet");
    Set<Long> rotSet = Whitebox.getInternalState(trafficSourceIdMetrics, "rotSet");
    Set<Integer> socialAgentId22 = Whitebox.getInternalState(trafficSourceIdMetrics, "socialAgentId22");
    Set<Integer> socialAgentId23 = Whitebox.getInternalState(trafficSourceIdMetrics, "socialAgentId23");
    assertThat(landPageSet1).isNotEmpty();
    assertThat(landPageSet2).isNotEmpty();
    assertThat(swdSet).isNotEmpty();
    assertThat(rotSet).isNotEmpty();
    assertThat(socialAgentId22).isNotEmpty();
    assertThat(socialAgentId23).isNotEmpty();
  }

  @Test
  void start() throws Exception {
    doNothing().when(mockSession).initIntermediateMetrics();
    trafficSourceIdMetrics.start(sessionAccumulator);
    verify(mockSession, times(1)).initIntermediateMetrics();
  }

  @Test
  void end_traffic_sourceId_23() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getScEventE()).thenReturn("11030");
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(23);
  }

  @Test
  void end_traffic_sourceId_19() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getChannel()).thenReturn(8);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(19);
  }

  @Test
  void end_traffic_sourceId_4() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getChannel()).thenReturn(7);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(4);
  }

  @Test
  void end_traffic_sourceId_4_ScEventE_notBlank() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getScEventE()).thenReturn("ebay-test");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(4);
  }

  @Test
  void end_traffic_sourceId_6() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(1);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("half...");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_7() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(8);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("half...");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_9() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(8);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("half...");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_6_firstCobrand() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("kleinanzeigen");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_7_firstCobrand() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("kleinanzeigen");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_9_firstCobrand() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("kleinanzeigen");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_28() throws Exception {
    Whitebox.setInternalState(trafficSourceIdMetrics, "rotSet", Sets.newHashSet(72412831558541L));
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("14");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");
    when(mockIntermediateMetrics.getRotId()).thenReturn(72412831558541L);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(28);
  }

  @Test
  void end_traffic_sourceId_29() throws Exception {
    Whitebox.setInternalState(trafficSourceIdMetrics, "rotSet", Sets.newHashSet(72412831558541L));
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("14");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("ebay");
    when(mockIntermediateMetrics.getRotId()).thenReturn(72412831558541L);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(29);
  }

  @Test
  void end_traffic_sourceId_6_keyword_ebay() throws Exception {
    Whitebox.setInternalState(trafficSourceIdMetrics, "rotSet", Sets.newHashSet(72412831558541L));
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("ebay");
    when(mockIntermediateMetrics.getRotId()).thenReturn(72412831558541L);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_7_keyword_ebay() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("ebay");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_9_keyword_ebay() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("ebay");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_18() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(18);
  }

  @Test
  void end_traffic_sourceId_10() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(10);
  }

  @Test
  void end_traffic_sourceId_12() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("6");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(12);
  }

  @Test
  void end_traffic_sourceId_11() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("1");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(11);
  }

  @Test
  void end_traffic_sourceId_30() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("32");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(30);
  }

  @Test
  void end_traffic_sourceId_13() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("9");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(13);
  }

  @Test
  void end_traffic_sourceId_14() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("15");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(14);
  }

  @Test
  void end_traffic_sourceId_15() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("16");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(15);
  }

  @Test
  void end_traffic_sourceId_16() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("17");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(16);
  }

  @Test
  void end_traffic_sourceId_17() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("23");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(17);
  }

  @Test
  void end_traffic_sourceId_20() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(20);
  }


  @Test
  void end_traffic_sourceId_25() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("27");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(25);
  }

  @Test
  void end_traffic_sourceId_26() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("97");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(26);
  }

  @Test
  void end_traffic_sourceId_31() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getMpxChannelId()).thenReturn("33");
    when(mockIntermediateMetrics.getActualKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(31);
  }

  @Test
  void end_traffic_sourceId_11_ImgMpxChannelId() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("1");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(11);
  }

  @Test
  void end_traffic_sourceId_30_ImgMpxChannelId() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("32");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(30);
  }

  @Test
  void end_traffic_sourceId_12_ImgMpxChannelId() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getEventTS()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getRoverEntryTs()).thenReturn(3542227200000000L);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("6");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(12);
  }

  @Test
  void end_traffic_sourceId_6_RefKeyword() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(1);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("half");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_6_RefKeyword_kijiji() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("kijiji");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_6_RefKeyword_ebay() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("ebay");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(6);
  }

  @Test
  void end_traffic_sourceId_7_RefKeyword() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(1);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("half");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_7_RefKeyword_kijiji() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("kijiji");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_7_RefKeyword_ebay() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("ebay");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(7);
  }

  @Test
  void end_traffic_sourceId_9_RefKeyword() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(1);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("half");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_9_RefKeyword_kijiji() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(5);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("kijiji");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_9_RefKeyword_ebay() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockSession.getFirstCobrand()).thenReturn(2);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("ebay");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(9);
  }

  @Test
  void end_traffic_sourceId_18_ImgMpxChannelId25() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("25");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(18);
  }

  @Test
  void end_traffic_sourceId_10_ImgMpxChannelId2() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("2");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(10);
  }

  @Test
  void end_traffic_sourceId_13_ImgMpxChannelId9() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("9");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(13);
  }

  @Test
  void end_traffic_sourceId_14_ImgMpxChannelId15() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("15");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(14);
  }

  @Test
  void end_traffic_sourceId_15_ImgMpxChannelId16() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("16");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(15);
  }


  @Test
  void end_traffic_sourceId_16_ImgMpxChannelId17() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("17");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(16);
  }


  @Test
  void end_traffic_sourceId_17_ImgMpxChannelId23() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("23");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(17);
  }

  @Test
  void end_traffic_sourceId_20_ImgMpxChannelId26() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("26");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(20);
  }

  @Test
  void end_traffic_sourceId_25_ImgMpxChannelId27() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("27");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(25);
  }

  @Test
  void end_traffic_sourceId_23_ImgMpxChannelId13() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getImgMpxChannelId()).thenReturn("13");
    when(mockIntermediateMetrics.getRefKeyword()).thenReturn("xxx");
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("xxx");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(23);
  }

  @Test
  void end_traffic_sourceId_21_RefDomain() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("itemlistings.ebay.");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(21);
  }

  @Test
  void end_traffic_sourceId_21_RefDomain_regex() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("popular");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(21);
  }

  @Test
  void end_traffic_sourceId_24_RefDomain() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn(".craigslist.");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(24);
  }

  @Test
  void end_traffic_sourceId_8_RefDomain() throws Exception {
    Whitebox.setInternalState(trafficSourceIdMetrics, "landPageSet1", Sets.newHashSet(123));
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("toolbar");
    when(mockIntermediateMetrics.getLandPageID()).thenReturn(123);

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(8);
  }

  @Test
  void end_traffic_sourceId_21_RefDomain_regex_toolbar() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("toolbar");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(21);
  }

  @Test
  void end_traffic_sourceId_22_RefDomain_regex() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("t.co");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(22);
  }

  @Test
  void end_traffic_sourceId_5_RefDomain() throws Exception {
    doNothing().when(mockIntermediateMetrics).end(sessionAccumulator);
    when(mockIntermediateMetrics.getRefDomain()).thenReturn("email");

    trafficSourceIdMetrics.end(sessionAccumulator);
    verify(mockSession, times(1)).setTrafficSrcId(5);
  }

}
