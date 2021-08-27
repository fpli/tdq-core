package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 * @author naravipati
 * <p>
 * This UDF takes input as string and delimiter and returns the last element.
 */

public class SojListLastElement implements Serializable {

  public String evaluate(String str, String delimit) {

    String result;

    // Checking Given parameters are NULL or not

    if (str == null || delimit == null) {

      return null;
    } else {

      result = str.substring(str.lastIndexOf(delimit) + 1).trim();

      if (StringUtils.isBlank(result)) {

        return null;
      }

      return result;
    }

  }

}
