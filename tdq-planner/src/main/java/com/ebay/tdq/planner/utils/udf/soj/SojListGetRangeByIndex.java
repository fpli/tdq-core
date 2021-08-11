package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SojListGetRangeByIndex implements Serializable {

  private static List<String> stringList = Arrays
      .asList("$", "(", ")", "*", "+", ".", "[", "]", "?", "\\", "^", "{", "}", "|");

  public String evaluate(String list, String delimiter, int start_index, int num_values) {

    if (list == null || delimiter == null) {
      return null;
    }

    return getRangeByIdx(list, delimiter, start_index, num_values);
  }

  private String getRangeByIdx(String list, String delimiter, int start_index, int num_values) {
    // checking NULL value for parameter:str_vec
    if (list == null || list.length() == 0) {
      return null;
    }

    // checking NULL value for parameter:vec_delimit
    if (delimiter == null || delimiter.length() == 0) {
      return null;
    }
    String deli = delimiter;
    if (stringList.contains(delimiter)) {
      deli = "\\" + deli;
    }
    String[] list_split = list.split(deli, -1);
    String[] result = new String[Math.max(start_index - 1 + num_values, list_split.length)];
    int index = 0;
    int count = 0;
    boolean isFound = false;
    for (int i = 0; i < list_split.length; i++) {
      if (i >= start_index - 1) {
        count++;
        result[index] = list_split[i];
        index++;
      }
      if (count >= num_values) {
        isFound = true;
        break;
      }

    }
    if (count > 0) {
      isFound = true;
    }
    if (count == 0) {
      return null;
    } else {
      if ("".equals(StringUtils.join(result, delimiter, 0, Math.min(count, result.length)))) {
        return null;
      } else {
        return StringUtils.join(result, delimiter, 0, Math.min(count, result.length));
      }
    }


  }
}