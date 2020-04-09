package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TimestampMetricsTest2 {

  private List<UbiEvent> ubiEventList;
  private TimestampMetrics timestampMetrics;
  private UbiSession ubiSession;
  private SessionAccumulator sessionAccumulator;

  @Before
  public void setUp() throws Exception {
    ubiEventList = new ArrayList<>();
    UbiEvent ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2507874);
    ubiEvent.setEventTimestamp(3795064810698000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(3795064810731000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(3795064810731000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(3795064810731000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(3795064810744000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2367355);
    ubiEvent.setEventTimestamp(3795064810731000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(3795064810721000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2317508);
    ubiEvent.setEventTimestamp(3795064810830000L);
    ubiEventList.add(ubiEvent);
    ubiEvent = new UbiEvent();
    ubiEvent.setGuid("4affcde01710ac734f70f1afff5eb980");
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setPageId(2047675);
    ubiEvent.setEventTimestamp(3795064810583000L);
    ubiEventList.add(ubiEvent);
    timestampMetrics = new TimestampMetrics();
    ubiSession = new UbiSession();
    ubiSession.setStartTimestamp(3795064810583000L);
    ubiSession.setEndTimestamp(3795064810583000L);
    ubiSession.setAbsStartTimestamp(3795064810583000L);
    ubiSession.setAbsEndTimestamp(3795064810830000L);
    ubiSession.setSessionSkey(37950648105830L);
    sessionAccumulator = new SessionAccumulator();

  }

  @Test
  public void test1() throws Exception {
    timestampMetrics.init();
    timestampMetrics.start(sessionAccumulator);
    for (UbiEvent ubiEvent : ubiEventList) {
      timestampMetrics.feed(ubiEvent, sessionAccumulator);
    }
    Assert.assertEquals(ubiSession.getAbsStartTimestamp(),
        sessionAccumulator.getUbiSession().getAbsStartTimestamp());
    Assert.assertEquals(ubiSession.getAbsEndTimestamp(),
        sessionAccumulator.getUbiSession().getAbsEndTimestamp());
    Assert.assertEquals(ubiSession.getStartTimestamp(),
        sessionAccumulator.getUbiSession().getStartTimestamp());
    Assert.assertEquals(ubiSession.getEndTimestamp(),
        sessionAccumulator.getUbiSession().getEndTimestamp());
    Assert.assertEquals(ubiSession.getSessionSkey(),
        sessionAccumulator.getUbiSession().getSessionSkey());
  }

}
