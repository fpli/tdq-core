package com.ebay.tdq.planner.utils.udf.soj;

import static java.lang.Character.isDigit;

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
  public boolean evaluate(String input, int digitLimit) {

    // TD return false in case of null input.
    if (input == null) return false;

    input = input.trim();

    int length = input.length();

    if (length == 0) return false;

    int dpCnt = 0; // decimal points counter
    int chCnt = 0; // char pointer counter

    // one leading +|- sign is ok
    if (input.charAt(chCnt) == '+' || input.charAt(chCnt) == '-') {
      chCnt++;
    }

    while (true) {
      if (chCnt == length) {
        break;
      }
      if (input.charAt(chCnt) == '.') {
        dpCnt++; // count the number of decimalpoints in the input
      }
      else if (!isDigit(input.charAt(chCnt))) {
        return false;
      }
      chCnt++;
    }
    return chCnt <= digitLimit && dpCnt <= 1;
  }

  // Overloads the above method with a defalut digitLimit == 18, which is verified in TD by Daniel.
  public boolean evaluate(String input) {
    return evaluate(input, 18);
  }

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
