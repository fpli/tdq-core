package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceInfoMetricsTest {

  DeviceInfoMetrics deviceInfoMetrics;
  SessionAccumulator sessionAccumulator;
  UbiSession ubiSession;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    deviceInfoMetrics = new DeviceInfoMetrics();
    ubiEvent = new UbiEvent();
    ubiSession = new UbiSession();
    sessionAccumulator = new SessionAccumulator(ubiSession);
  }

  @Test
  void start() throws Exception {
    ubiSession.setBrowserFamily("abc");
    ubiSession.setBrowserVersion("1");
    ubiSession.setDeviceFamily("iPhone");
    ubiSession.setDeviceClass("xxx");
    ubiSession.setOsFamily("iOS");
    ubiSession.setOsVersion("14");
    deviceInfoMetrics.start(sessionAccumulator);
    assertThat(ubiSession.getBrowserFamily()).isNull();
    assertThat(ubiSession.getBrowserVersion()).isNull();
    assertThat(ubiSession.getDeviceFamily()).isNull();
    assertThat(ubiSession.getDeviceClass()).isNull();
    assertThat(ubiSession.getOsFamily()).isNull();
    assertThat(ubiSession.getOsVersion()).isNull();
  }

  @Test
  void feed_firstEvent() throws Exception {
    ubiEvent.setBrowserFamily("abc");
    ubiEvent.setBrowserVersion("1");
    ubiEvent.setDeviceFamily("iPhone");
    ubiEvent.setDeviceType("xxx");
    ubiEvent.setOsFamily("iOS");
    ubiEvent.setOsVersion("14");
    ubiSession.setAbsEventCnt(1);
    deviceInfoMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(ubiSession.getBrowserFamily()).isEqualTo("abc");
    assertThat(ubiSession.getBrowserVersion()).isEqualTo("1");
    assertThat(ubiSession.getDeviceFamily()).isEqualTo("iPhone");
    assertThat(ubiSession.getDeviceClass()).isEqualTo("xxx");
    assertThat(ubiSession.getOsFamily()).isEqualTo("iOS");
    assertThat(ubiSession.getOsVersion()).isEqualTo("14");
  }

  @Test
  void feed_followingEvent() throws Exception {
    ubiEvent.setBrowserFamily("abc");
    ubiEvent.setBrowserVersion("1");
    ubiEvent.setDeviceFamily("iPhone");
    ubiEvent.setDeviceType("xxx");
    ubiEvent.setOsFamily("iOS");
    ubiEvent.setOsVersion("14");
    ubiEvent.setPageId(123);
    ubiEvent.setIframe(false);
    ubiEvent.setRdt(false);
    ubiEvent.setEventTimestamp(1579505088L);

    ubiSession.setAbsEventCnt(4);
    ubiSession.setStartTimestampNOIFRAMERDT(1579505089L);
    deviceInfoMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(ubiSession.getBrowserFamily()).isEqualTo("abc");
    assertThat(ubiSession.getBrowserVersion()).isEqualTo("1");
    assertThat(ubiSession.getDeviceFamily()).isEqualTo("iPhone");
    assertThat(ubiSession.getDeviceClass()).isEqualTo("xxx");
    assertThat(ubiSession.getOsFamily()).isEqualTo("iOS");
    assertThat(ubiSession.getOsVersion()).isEqualTo("14");
  }

  @Test
  void feed_earlyEvent() throws Exception {
    ubiEvent.setBrowserFamily("abc");
    ubiEvent.setBrowserVersion("1");
    ubiEvent.setDeviceFamily("iPhone");
    ubiEvent.setDeviceType("xxx");
    ubiEvent.setOsFamily("iOS");
    ubiEvent.setOsVersion("14");
    ubiEvent.setPageId(123);
    ubiEvent.setIframe(true);
    ubiEvent.setRdt(false);
    ubiEvent.setEventTimestamp(1579505088L);

    ubiSession.setAbsEventCnt(4);
    ubiSession.setStartTimestampNOIFRAMERDT(1579505089L);
    ubiSession.setAbsStartTimestamp(1579505089L);
    deviceInfoMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(ubiSession.getBrowserFamily()).isEqualTo("abc");
    assertThat(ubiSession.getBrowserVersion()).isEqualTo("1");
    assertThat(ubiSession.getDeviceFamily()).isEqualTo("iPhone");
    assertThat(ubiSession.getDeviceClass()).isEqualTo("xxx");
    assertThat(ubiSession.getOsFamily()).isEqualTo("iOS");
    assertThat(ubiSession.getOsVersion()).isEqualTo("14");
  }
}