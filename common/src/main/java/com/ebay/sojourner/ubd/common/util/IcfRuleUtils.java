package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;

public class IcfRuleUtils {

    public static int getIcfRuleType(String applicationPayload){
        int icfType = 0;

        if (StringUtils.isBlank(applicationPayload)) {
            return icfType;
        }

        String hexString = SOJNVL.getTagValue(applicationPayload, "icf");

        if (StringUtils.isBlank(hexString)) {
            return icfType;
        }

        if (!hexString.contains("1")) {
            return icfType;
        }

        String binaryString = Converter.hexToBinary(hexString);
        String[] splited = binaryString.split("");

        for (int i=0;i<splited.length;i++) {
            if ((splited[i].equals("1")) && ((splited.length - i) == 1)) {
                icfType = BotRules.ICF_RULE1;
                break;
            } else if ((splited[i].equals("1")) && ((splited.length - i) == 2)) {
                icfType = BotRules.ICF_RULE2;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 3)) {
                icfType = BotRules.ICF_RULE3;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 4)) {
                icfType = BotRules.ICF_RULE4;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 5)) {
                icfType = BotRules.ICF_RULE5;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 6)) {
                icfType = BotRules.ICF_RULE6;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 7)) {
                icfType = BotRules.ICF_RULE7;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 10)) {
                icfType = BotRules.ICF_RULE10;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 11)) {
                icfType = BotRules.ICF_RULE11;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 12)) {
                icfType = BotRules.ICF_RULE12;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 13)) {
                icfType = BotRules.ICF_RULE13;
                break;
            }else if ((splited[i].equals("1")) && ((splited.length - i) == 56)) {
                icfType = BotRules.ICF_RULE56;
                break;
            }
        }

        return icfType;
    }
}
