package com.ebay.sojourner.business.ubd.metric;

import com.ebay.sojourner.common.model.Attributes;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class AttributeFlagMetricsTest {

  private SessionAccumulator sessionAccumulator;
  private UbiEvent ubiEvent;
  private UbiSession ubiSession;
  private Attributes attributes;
  private AttributeFlagMetrics attributeFlagMetrics;
  private byte[] attributeFlags = {
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
  };
  private Set<String> userIdSet = new HashSet<>();

  @BeforeEach
  public void setup() {
    attributeFlagMetrics = new AttributeFlagMetrics();
    sessionAccumulator = new SessionAccumulator();
    ubiEvent = new UbiEvent();
    ubiSession = new UbiSession();
    attributes = new Attributes();
  }

  @Test
  public void test1() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload(
        "ebay&rule=ebay&abvar=ebay&test=ebay&tui_abtest=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebayexpress.ebay.half.sofe");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[3]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[7]);
  }

  @Test
  public void test2() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload(
        "ebay&rule=ebay&abvar=ebay&test=ebay&tui_abtest=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half.ebay");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[2]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[7]);
  }

  @Test
  public void test3() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload(
        "ebay&rule=ebay&abvar=ebay&test=ebay&tui_abtest=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[7]);
  }

  @Test
  public void test4() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload(
        "ebay&rule=ebay&test=ebay&tui_abtest=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[6]);
  }

  @Test
  public void test5() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload(
        "ebay&rule=ebay&tui_abtest=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[8]);
  }

  @Test
  public void test6() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload("ebay&rule=ebay&epr=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[9]);
  }

  @Test
  public void test7() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload("ebay&rule=ebay&pgV=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[11]);
  }

  @Test
  public void test8() {
    ubiEvent.setSojDataDt(20200120L);
    ubiEvent.setApplicationPayload("ebay&rule=ebay&Motors20Group=ebay");
    ubiEvent.setWebServer("ebay.half");
    ubiEvent.setReferrer("ebaypages.ebay.com/expresshttp://www.express.ebay");
    ubiEvent.setPageId(2588);
    ubiEvent.setUserId("haxi");

    ubiSession.setAttributes(attributes);
    ubiSession.setUserIdSet(userIdSet);
    ubiSession.setAttributeFlags(attributeFlags);
    ubiSession.setSessionStartDt(20200121L);

    sessionAccumulator.setUbiSession(ubiSession);

    attributeFlagMetrics.feed(ubiEvent, sessionAccumulator);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[10]);

    Iterator<String> iterator = sessionAccumulator.getUbiSession().getUserIdSet().iterator();

    while (iterator.hasNext()) {
      Assertions.assertEquals(true, iterator.next().equals("haxi"));
    }

    Assertions.assertEquals(
        true, sessionAccumulator.getUbiSession().getUserIdSet().contains("haxi"));
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isCustRule);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isWeb_ee);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isSofe);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isHalf);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isEbxRef);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isAbvar);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTest);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isEpr);
    Assertions.assertEquals(false, sessionAccumulator.getUbiSession().getAttributes().isPgV);
    Assertions.assertEquals(true, sessionAccumulator.getUbiSession().getAttributes().isM2g);

    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[1]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[4]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[5]);
    Assertions.assertEquals(1, sessionAccumulator.getUbiSession().getAttributeFlags()[12]);
  }
}
