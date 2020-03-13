package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.Attributes;
import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.ByteArrayToNum;
import org.apache.commons.lang3.StringUtils;

public class AttributeFlagMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  private static final Integer EXCEPTION_NULL_INTEGER_VALUE = -99;
  private static final Long DEFAULTDATE = 0L;
  private static final Integer NUMBER_ATTRIBUTE_FLAG = 13;

  @Override
  public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
    /*
     * is_abvar Application_Payload.contain('&abvar=' ) &&
     * !Application_Payload.contain('&abvar=0&' ) && !Application_Payload.contain('&abvar=-1&')
     * is_test Application_Payload.contain('&test=') is_tui_abtesto
     * Application_Payload.contain('&tui_abtest=' ) is_epr Application_Payload.contain('&epr=')
     * is_pgV Application_Payload.contain('&pgV=') is_m2g
     * Application_Payload.contain('&Motors20Group='|'&m2g=' )
     */
    Long eventDate = event.getSojDataDt();
    if (eventDate != null && sessionAccumulator.getUbiSession().isFirstSessionStartDt()) {
      sessionAccumulator.getUbiSession().setSessionStartDt(eventDate);

      sessionAccumulator.getUbiSession().setIsFirstSessionStartDt(false);
    }
    if (eventDate == null) {
      eventDate = DEFAULTDATE;
    }
    String applicationPayload = event.getApplicationPayload();
    String webServer = event.getWebServer();
    String referrer = event.getReferrer();
    Integer pageId = event.getPageId();
    String userId = event.getUserId();

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
    if (pageId == null) {
      pageId = EXCEPTION_NULL_INTEGER_VALUE;
    }
    // update user list
    if (StringUtils.isNotBlank(userId)
        && sessionAccumulator.getUbiSession().getUserIdSet().size() < 3) {
      sessionAccumulator.getUbiSession().getUserIdSet().add(userId);
    }

    // attribute flag 10
    if (sessionAccumulator.getUbiSession().getAttributeFlags()[10] == 0
        && !eventDate.equals(sessionAccumulator.getUbiSession().getSessionStartDt())) {
      sessionAccumulator.getUbiSession().getAttributeFlags()[10] = 1;
    }

    if (applicationPayload.indexOf("&rule=") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isCustRule = true;
    }

    if (webServer.indexOf("express.ebay") >= 0 || webServer.indexOf("ebayexpress") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isWeb_ee = true;
    }

    if (webServer.indexOf("sofe") >= 0 && referrer.indexOf("pages.ebay.com/express") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isSofe = true;
    }

    if (webServer.indexOf("half.") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isHalf = true;
    }

    if (pageId == 2588
        && (referrer.indexOf("http://www.express.ebay") >= 0
        || referrer.indexOf("http://www.ebayexpress") >= 0)) {
      sessionAccumulator.getUbiSession().getAttributes().isEbxRef = true;
    }

    if (applicationPayload.indexOf("&abvar=") >= 0
        && applicationPayload.indexOf("&abvar=0&") <= 0
        && applicationPayload.indexOf("&abvar=-1&") <= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isAbvar = true;
    }

    if (applicationPayload.indexOf("&test=") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isTest = true;
    }

    if (applicationPayload.indexOf("&tui_abtest=") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isTuiAbtest = true;
    }

    if (applicationPayload.indexOf("&epr=") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isEpr = true;
    }

    if (applicationPayload.indexOf("&pgV=") >= 0) {
      sessionAccumulator.getUbiSession().getAttributes().isPgV = true;
    }

    if (applicationPayload.indexOf("&Motors20Group=") >= 0
        || applicationPayload.indexOf("&m2g=") >= 0) {
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
    sessionAccumulator.getUbiSession().setSessionStartDt(DEFAULTDATE);
    sessionAccumulator.getUbiSession().setIsFirstSessionStartDt(true);
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
