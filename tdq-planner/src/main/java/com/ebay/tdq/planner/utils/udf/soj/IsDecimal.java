package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * Created by szang on 7/31/17.
 * <p>
 * This UDF exams if the input String can be interpreted as a valid decimal number.
 * <p>
 * The source code reference: https://github.corp.ebay.com/APD/DINT-UDF/blob/master/udf/syslib/udf_IsDecimal2.c
 * <p>
 * The initial request on JIRA ticket:
 * <p>
 * Hi Team, The below UDF function is not supported in Spark. Please let me know the equivalent.
 * syslib.udf_isdecimal(encrypted_user_id)=1
 * <p>
 * On a side note, the source code doesn't deal with the following corner case: input: "0000.1"
 */


public class IsDecimal implements Serializable {

  public int evaluate(String instr, int p, int s) {
    if (instr == null || p < 0 || s < 0) {
      return 0;
    }
    if (!NumberUtils.isNumber(instr) || instr.contains("e") || instr.contains("E")) {
      return 0;
    }
    try {
      BigDecimal decimal = new BigDecimal(instr);
      int scale = decimal.scale();
      int precision = decimal.precision();
      if ((p - s) >= (precision - scale)) {
        return 1;
      } else {
        return 0;
      }

    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
