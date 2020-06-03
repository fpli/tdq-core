package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.ubd.common.util.SOJNVL;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;

public class IcfParser implements FieldParser<RawEvent, UbiEvent> {

  @Override
  public void init() throws Exception {
  }

  @Override
  public void parse(RawEvent rawEvent, UbiEvent ubiEvent) throws Exception {

    if (StringUtils.isBlank(ubiEvent.getApplicationPayload())) {
      ubiEvent.setIcfBinary(0L);
    } else {
      String hexString = SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "icf");
      if (StringUtils.isBlank(hexString)) {
        ubiEvent.setIcfBinary(0L);
      } else {
        long icfDecNum = NumberUtils.hexToDec(hexString);
        ubiEvent.setIcfBinary(icfDecNum);
      }
    }
  }
}
