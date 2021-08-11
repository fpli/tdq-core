package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

/**
 * @author naravipati
 * <p>
 * This UDF takes input as string,delimiter,element and returns the position of the first occurrence of the string if
 * the given string is available in list.
 */

public class SojValueInList implements Serializable {

  public String evaluate(String str, String delimit, String element) {
    int index = 0;
    String result = null;
    // Checking Given parameters are NULL or not
    if (str == null || delimit == null || element == null) {
      return null;
    } else {
      for (String retval : str.split(delimit)) {
        index++;
        if (retval.equals(element)) {
          result = Integer.toString(index);
          break;
        }
      }
      return result;
    }
  }

}
