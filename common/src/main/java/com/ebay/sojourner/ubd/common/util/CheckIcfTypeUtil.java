package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class CheckIcfTypeUtil {

    public static int getIcfRuleType(String applicationPayload,int checkLocation){
        if (checkLocation <= 0) {
            log.error("the parameter must great than 0");
        }

        if (StringUtils.isBlank(applicationPayload)) {
            return 0;
        }

        String hexString = SOJNVL.getTagValue(applicationPayload, "icf");

        if (StringUtils.isBlank(hexString)) {
            return 0;
        }

        if (!hexString.contains("1")) {
            return 0;
        }

        String binaryString = Converter.hexToBinary(hexString);
        String[] splited = binaryString.split("");

        if (checkLocation > splited.length) {
            log.error("the parameter must less than " + splited.length);
        }

        if (splited[splited.length - checkLocation].equals("1")) {
            return 1;
        } else {
            return 0;
        }
    }
}
