package com.ebay.sojourner.business.metric;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributeFlagMetricsTest {

  SessionAccumulator sessionAccumulator;
  UbiEvent ubiEvent;
  AttributeFlagMetrics attributeFlagMetrics;
  byte expectedFlag = new Integer(1).byteValue();
  byte defaultFlag = new Integer(0).byteValue();
  Long dt = 1600761563000000L; // 2020-09-22

  @BeforeEach
  void setup() {
    attributeFlagMetrics = new AttributeFlagMetrics();
    sessionAccumulator = new SessionAccumulator();
    sessionAccumulator.getUbiSession().setFirstSessionStartDt(dt);
    ubiEvent = new UbiEvent();
  }

  @Test
  void test_feed_userIdSetLt3() {
    ubiEvent.setUserId("test-user-id");
    Set<String> userIdSet = new HashSet<>();
    sessionAccumulator.getUbiSession().setUserIdSet(userIdSet);
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getUserIdSet().size()).isEqualTo(1);
    assertThat(sessionAccumulator.getUbiSession().getUserIdSet().contains("test-user-id")).isTrue();
  }

  @Test
  void test_feed_userIdSetGt3() {
    ubiEvent.setUserId("test-user-id");
    Set<String> userIdSet = new HashSet<>();
    userIdSet.add("a");
    userIdSet.add("b");
    userIdSet.add("c");
    sessionAccumulator.getUbiSession().setUserIdSet(userIdSet);
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getUserIdSet().size()).isEqualTo(3);
    assertThat(sessionAccumulator.getUbiSession().getUserIdSet().contains("test-user-id")).isFalse();
  }

  @Test
  void test_feed_attributeFlags10() {
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributeFlags()[10]).isEqualTo(expectedFlag);
  }

  @Test
  void test_feed_attributeFlags10_notSet() {
    ubiEvent.setSojDataDt(dt);
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributeFlags()[10]).isEqualTo(defaultFlag);
  }

  @Test
  void test_feed_applicationPayload_contains_rule() {
    ubiEvent.setApplicationPayload("abc&rule=123");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isCustRule).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_express_ebay() {
    ubiEvent.setWebServer("express.ebay");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isWeb_ee).isTrue();
  }

  @Test
  void test_feed_WebServer_contains_ebayexpress() {
    ubiEvent.setWebServer("ebayexpress");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isWeb_ee).isTrue();
  }

  @Test
  void test_feed_WebServer_contains_sofe_and_referrer_contains_express() {
    ubiEvent.setWebServer("sofe");
    ubiEvent.setReferrer("pages.ebay.com/express");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isSofe).isTrue();
  }

  @Test
  void test_feed_WebServer_contains_sofe_and_referrer_not_contains_express() {
    ubiEvent.setWebServer("sofe");
    ubiEvent.setReferrer("bla");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isSofe).isFalse();
  }

  @Test
  void test_feed_WebServer_contains_half() {
    ubiEvent.setWebServer("half.");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isHalf).isTrue();
  }

  @Test
  void test_feed_pageId2588_and_referrer_contains_express() {
    ubiEvent.setPageId(2588);
    ubiEvent.setReferrer("http://www.express.ebay");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEbxRef).isTrue();
  }

  @Test
  void test_feed_pageId2588_and_referrer_contains_ebayexpress() {
    ubiEvent.setPageId(2588);
    ubiEvent.setReferrer("http://www.ebayexpress");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEbxRef).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_abvar() {
    ubiEvent.setApplicationPayload("&abvar=1");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isAbvar).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_abvarEq0() {
    ubiEvent.setApplicationPayload("&abvar=0&");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isAbvar).isFalse();
  }

  @Test
  void test_feed_applicationPayload_contains_test() {
    ubiEvent.setApplicationPayload("&test=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTest).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_tui_abtest() {
    ubiEvent.setApplicationPayload("&tui_abtest=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_epr() {
    ubiEvent.setApplicationPayload("&epr=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEpr).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_pgV() {
    ubiEvent.setApplicationPayload("&pgV=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isPgV).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_Motors20Group() {
    ubiEvent.setApplicationPayload("&Motors20Group=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isM2g).isTrue();
  }

  @Test
  void test_feed_applicationPayload_contains_m2g() {
    ubiEvent.setApplicationPayload("&m2g=");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isM2g).isTrue();
  }

  @Test
  void test_feed_not_contains_str() {
    ubiEvent.setWebServer("");
    ubiEvent.setApplicationPayload("");
    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isCustRule).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isWeb_ee).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isSofe).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isHalf).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEbxRef).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isAbvar).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTest).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEpr).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isPgV).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isM2g).isFalse();
  }

  @Test
  void test_start() {
    attributeFlagMetrics.start(sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getUserIdSet().size()).isEqualTo(0);
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isCustRule).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isWeb_ee).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isSofe).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isHalf).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEbxRef).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isAbvar).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTest).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isEpr).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isPgV).isFalse();
    assertThat(sessionAccumulator.getUbiSession().getAttributes().isM2g).isFalse();
    for (byte attributeFlag : sessionAccumulator.getUbiSession()
                                                .getAttributeFlags()) {
      assertThat(attributeFlag).isEqualTo(defaultFlag);
    }
  }


  @Test
  void test_end_userIdSet_Lt1() {
    attributeFlagMetrics.end(sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributeFlags()[0]).isEqualTo(defaultFlag);
  }

  @Test
  void test_end_userIdSet_Gt1() {
    sessionAccumulator.getUbiSession().getUserIdSet().add("a");
    sessionAccumulator.getUbiSession().getUserIdSet().add("b");
    attributeFlagMetrics.end(sessionAccumulator);
    assertThat(sessionAccumulator.getUbiSession().getAttributeFlags()[0]).isEqualTo(expectedFlag);
  }
}
