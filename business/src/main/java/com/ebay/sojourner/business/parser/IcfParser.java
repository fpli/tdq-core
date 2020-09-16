package com.ebay.sojourner.business.parser;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.NumberUtils;
import com.ebay.sojourner.common.util.PropertyUtils;
import com.ebay.sojourner.common.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;

public class IcfParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void init() throws Exception {
  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {

    String applicationPayload = null;
    String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
    String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
    String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
    if (mARecString != null) {
      applicationPayload = mARecString;
    }
    if ((applicationPayload != null) && (mKRecString != null)) {
      applicationPayload = applicationPayload + "&" + mKRecString;
    }

    // else set C record
    if (applicationPayload == null) {
      applicationPayload = mCRecString;
    }

    if (StringUtils.isBlank(applicationPayload)) {
      ubiEvent.setIcfBinary(0L);
    } else {
      String hexString = SOJNVL.getTagValue(applicationPayload, "icf");
      if (StringUtils.isBlank(hexString)) {
        ubiEvent.setIcfBinary(0L);
      } else {
        long icfDecNum = NumberUtils.hexToDec(hexString);
        ubiEvent.setIcfBinary(icfDecNum);
      }
    }

     ubiEvent.setApplicationPayload(applicationPayload);
  }
}
