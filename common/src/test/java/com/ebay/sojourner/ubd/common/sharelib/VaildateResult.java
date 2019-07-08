package com.ebay.sojourner.ubd.common.sharelib;

import com.ebay.sojourner.ubd.common.util.TypeTransUtil;

public class VaildateResult {

    public static Boolean validateString(Object expectResult,String str){

        if(TypeTransUtil.ObjectToString(expectResult).equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static Boolean validateInteger(Object expectResult,Integer integer){

        if(TypeTransUtil.ObjectToInteger(expectResult).equals(integer)){
            return true;
        }else{
            return false;
        }
    }
}
