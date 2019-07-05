package com.ebay.sojourner.ubd.common.sharelib;

import com.ebay.sojourner.ubd.common.util.TypeTransUtil;
import org.apache.log4j.Logger;

public class VaildateResult {
    private static final Logger logger = Logger.getLogger(VaildateResult.class);

    public static Boolean vaildateString(Object expectResult,String str){

        if(TypeTransUtil.ObjectToString(expectResult).equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean vaildateInteger(Object expectResult,Integer integer){

        if(TypeTransUtil.ObjectToInteger(expectResult).equals(integer)){
            return true;
        }else{
            return false;
        }
    }
}
