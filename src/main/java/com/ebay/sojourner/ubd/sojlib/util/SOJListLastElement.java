package com.ebay.sojourner.ubd.sojlib.util;

import org.apache.commons.lang3.StringUtils;

public class SOJListLastElement {

    public static String getLastElement(String str_vec, String vec_delimit) {
        // to align with TD udf
        // the vector start from 1

        // checking NULL value for parameter:str_vec
        // checking NULL value for parameter:vec_delimit
        if (StringUtils.isBlank(str_vec) || StringUtils.isBlank(vec_delimit)) {
            return null;
        }

        String[] vect;

        vect = str_vec.split(vec_delimit, -1);
        if(!"".equals(vect[vect.length - 1])) {
            return vect[vect.length - 1];
        }
        else
        {
            return null;
        }
    }

}
