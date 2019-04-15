package com.ebay.sojourner.ubd.common.sojlib;


import org.apache.commons.lang3.StringUtils;

public class SOJReplaceChar {

    public static String replaceString(String str, String oldString, String newString) {
        if (StringUtils.isBlank(str)
                || StringUtils.isBlank(oldString)
                || StringUtils.isBlank(newString))
            return str;
        
        return str.replace(oldString, newString);
    }

    public static String replaceChar(String str, String oldString, Character newChar) {
        if (StringUtils.isBlank(str) 
                || StringUtils.isBlank(oldString)
                || newChar == null)
            return str;
        
        String newString = String.valueOf(newChar);
        
        return str.replace(oldString, newString);
    }
}
