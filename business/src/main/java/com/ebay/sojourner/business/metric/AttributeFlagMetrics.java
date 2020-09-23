package com.ebay.sojourner.business.metric;

import com.ebay.sojourner.common.model.Attributes;
import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.ByteArrayToNum;
import com.ebay.sojourner.common.util.SojTimestamp;
import org.apache.commons.lang3.StringUtils;

public class AttributeFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static final Integer EXCEPTION_NULL_INTEGER_VALUE = -99;
  private static final Long DEFAULT_DATE = 0L;

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    Long eventDate = event.getSojDataDt();
    if (eventDate == null) {
      eventDate = DEFAULT_DATE;
    } else {
      eventDate = SojTimestamp.castSojTimestampToDate(eventDate);
    }
    String applicationPayload = event.getApplicationPayload();
    String webServer = event.getWebServer();
    String referrer = event.getReferrer();
    int pageId = event.getPageId();
    String userId = event.getUserId();
    Long firstSessionStartDT = SojTimestamp.castSojTimestampToDate(
          sessionAccumulator.getUbiSession().getFirstSessionStartDt());
    // replace null with default value
    if (applicationPayload == null) {
      applicationPayload = "";
    }
    if (webServer == null) {
      webServer = "";
    }
    if (referrer == null) {
      referrer = "";
    }
    if (pageId == -1) {
      pageId = EXCEPTION_NULL_INTEGER_VALUE;
    }
    // update user list
    if (StringUtils.isNotBlank(userId)
        && sessionAccumulator.getUbiSession().getUserIdSet().size() < 3) {
      sessionAccumulator.getUbiSession().getUserIdSet().add(userId);
    }

    // attribute flag 10
    if (sessionAccumulator.getUbiSession().getAttributeFlags()[10] == 0
        && !eventDate.equals(firstSessionStartDT)) {
      sessionAccumulator.getUbiSession().getAttributeFlags()[10] = 1;
    }

    if (applicationPayload.contains("&rule=")) {
      sessionAccumulator.getUbiSession().getAttributes().isCustRule = true;
    }

    if (webServer.contains("express.ebay") || webServer.contains("ebayexpress")) {
      sessionAccumulator.getUbiSession().getAttributes().isWeb_ee = true;
    }

    if (webServer.contains("sofe") && referrer.contains("pages.ebay.com/express")) {
      sessionAccumulator.getUbiSession().getAttributes().isSofe = true;
    }

    if (webServer.contains("half.")) {
      sessionAccumulator.getUbiSession().getAttributes().isHalf = true;
    }

    if (pageId == 2588
        && (referrer.contains("http://www.express.ebay")
            || referrer.contains("http://www.ebayexpress"))) {
      sessionAccumulator.getUbiSession().getAttributes().isEbxRef = true;
    }

    if (applicationPayload.contains("&abvar=")
        && !applicationPayload.contains("&abvar=0&")
        && !applicationPayload.contains("&abvar=-1&")) {
      sessionAccumulator.getUbiSession().getAttributes().isAbvar = true;
    }

    if (applicationPayload.contains("&test=")) {
      sessionAccumulator.getUbiSession().getAttributes().isTest = true;
    }

    if (applicationPayload.contains("&tui_abtest=")) {
      sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest = true;
    }

    if (applicationPayload.contains("&epr=")) {
      sessionAccumulator.getUbiSession().getAttributes().isEpr = true;
    }

    if (applicationPayload.contains("&pgV=")) {
      sessionAccumulator.getUbiSession().getAttributes().isPgV = true;
    }

    if (applicationPayload.contains("&Motors20Group=")
        || applicationPayload.contains("&m2g=")) {
      sessionAccumulator.getUbiSession().getAttributes().isM2g = true;
    }
    flagProcess(sessionAccumulator.getUbiSession().getAttributes(), sessionAccumulator);
  }

  void flagProcess(Attributes attributes, SessionAccumulator sessionAccumulator) {
    // 13 Flags except 0 and 10
    byte[] attributeFlags = sessionAccumulator.getUbiSession().getAttributeFlags();
    if (attributes.isCustRule) {
      attributeFlags[1] = 1;
    }

    if (attributes.isWeb_ee || attributes.isSofe) {
      attributeFlags[3] = 1;
    } else if (attributes.isHalf) {
      attributeFlags[2] = 1;
    } else {
      attributeFlags[4] = 1;
    }

    if (attributes.isEbxRef) {
      attributeFlags[5] = 1;
    }

    if (attributes.isAbvar) {
      attributeFlags[7] = 1;
    } else if (attributes.isTest) {
      attributeFlags[6] = 1;
    } else if (attributes.isTuiAbtest) {
      attributeFlags[8] = 1;
    } else if (attributes.isEpr) {
      attributeFlags[9] = 1;
    } else if (attributes.isPgV) {
      attributeFlags[11] = 1;
    } else if (attributes.isM2g) {
      attributeFlags[12] = 1;
    }
  }

  public void endSessionProcess(SessionAccumulator sessionAccumulator) {
    if (sessionAccumulator.getUbiSession().getUserIdSet() != null
        && sessionAccumulator.getUbiSession().getUserIdSet().size() > 1) {
      sessionAccumulator.getUbiSession().getAttributeFlags()[0] = 1;
    }
  }

  @Override
  public void init() throws Exception {
  }

  @Override
  public void start(SessionAccumulator sessionAccumulator) {
    sessionAccumulator.getUbiSession().getAttributes().reset();
    sessionAccumulator.getUbiSession().getUserIdSet().clear();
    byte[] attributeFlags = sessionAccumulator.getUbiSession().getAttributeFlags();
    for (int i = attributeFlags.length - 1; i >= 0; i--) {
      attributeFlags[i] = 0;
    }
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) {
    this.endSessionProcess(sessionAccumulator);
    sessionAccumulator
        .getUbiSession()
        .setAttrFlags(
            ByteArrayToNum.getInt(sessionAccumulator.getUbiSession().getAttributeFlags()));
  }
}
