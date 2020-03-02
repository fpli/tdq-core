package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RdtParser implements FieldParser<RawEvent, UbiEvent> {

  public static final String RDT = "rdt";

  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());
    String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
    String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
    String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
    String applicationPayload = null;
    if (mARecString != null) {
      applicationPayload = mARecString;
    }
    if ((applicationPayload != null) && (mKRecString != null)) {
      applicationPayload = applicationPayload + "&" + mKRecString;
    }

    // else set C record
    if (applicationPayload == null) applicationPayload = mCRecString;

    String payload = applicationPayload;
    try {
      if (StringUtils.isNotBlank(SOJNVL.getTagValue(payload, RDT))) {
        if ("0".equals(SOJNVL.getTagValue(payload, RDT))) {
          ubiEvent.setRdt(false);
        } else {
          ubiEvent.setRdt(true);
        }
      } else {
        ubiEvent.setRdt(false);
      }
    } catch (Exception e) {
      log.error("Parsing rdt failed, format wrong...", e);
      ubiEvent.setRdt(false);
    }
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
