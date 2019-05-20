package com.ebay.sojourner.ubd.sharedlib.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SOJExtractNVP {

    public static String getTagValue(String value, String key, String keyDelimiter, String valueDelimiter) {
        String K_DELIMITER[] = { keyDelimiter };
        String V_DELIMITER = valueDelimiter;
        String BLANK_STRING = "";
        
        if (StringUtils.isBlank(value) ||
                StringUtils.isBlank(key)) {
            return null;
        }
        
        //add a key delimiter if value is not leading by it
        if (! value.startsWith(keyDelimiter)) {
            value = keyDelimiter + value;
        }
        
        Pattern p1, p2;
        Matcher m1, m2;
        String kvSet = "", keySet = "";
        StringBuilder sb_key = new StringBuilder();
        StringBuilder sb_kv = new StringBuilder();
        
        // set search keys
        for (int i = 0; i < K_DELIMITER.length; i++) {
            sb_key.append(K_DELIMITER[i]).append(key).append(V_DELIMITER).append("|");
            sb_kv.append(K_DELIMITER[i]).append("|");
        }

        keySet = sb_key.substring(0,sb_key.length() - 1);
        kvSet = sb_kv.substring(0, sb_kv.length() - 1);

        p1 = Pattern.compile(keySet);
        p2 = Pattern.compile(kvSet);
        m1 = p1.matcher(value);
        m2 = p2.matcher(value);

        if (m1.find()) {
            int startpos;
            int endpos;
            startpos = m1.start();
            int tmppos = m1.end();
            if (m2.find(tmppos)) {
                endpos = m2.start();
            }else{
                //if not next delimiter, get the max position
                endpos = value.length();
            }
                if (endpos < 0)
                    endpos = value.length() - 1;
                String kvPair[] = value.substring(startpos, endpos).split(V_DELIMITER,2);
                return BLANK_STRING.equals(kvPair[1]) ? null : kvPair[1];
            
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("params = " + getTagValue("h8=d3&h4=d9&", "h4", "&", "="));
    }
}
