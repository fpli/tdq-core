package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

/**
 * @author Daniel Zhang
 * <p>
 * This UDF takes input as string,charList,replaceChar and returns the string.
 */

public class SojReplaceRChar implements Serializable {

  public String evaluate(String str, String charList, String replaceChar) {
    // Checking the Given parameters are NULL or not
    if (str == null || charList == null || replaceChar == null) {
      return null;
    } else if (str.equals("")) {
      return "";
    } else {
      String firstChar = replaceChar.length() > 1 ?
          replaceChar.substring(0, 1) :
          replaceChar;
      String chars = charList.equals("") ? "." : charList;
      return str.replaceAll("[^" + chars + "]", firstChar);
    }

  }
}
