package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import com.ebay.sojourner.ubd.common.util.Converter;
import org.apache.commons.lang3.StringUtils;

public class IcfTypeParser {

    public static void parse(UbiEvent ubiEvent) throws Exception {

        if (StringUtils.isBlank(ubiEvent.getApplicationPayload())) {
            ubiEvent.setIcfBinary(0L);
        } else {
            String hexString = SOJNVL.getTagValue(ubiEvent.getApplicationPayload(), "icf");
            if (StringUtils.isBlank(hexString)) {
                ubiEvent.setIcfBinary(0L);
            } else {
                long icfDecNum = Converter.hexToDec(hexString);
                ubiEvent.setIcfBinary(icfDecNum);
            }
        }
    }
}
