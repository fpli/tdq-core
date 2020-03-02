package com.ebay.sojourner.ubd.common.sharedlib.util;

public class SOJListGetValueByIndex {

  public static String getValueByIndex(String str_vec, String vec_delimit, int vec_idx) {
    // to align with TD udf
    // the vector start from 1

    // checking NULL value for parameter:str_vec
    if (str_vec == null || str_vec.length() == 0) {
      return null;
    }

    // checking NULL value for parameter:vec_delimit
    if (vec_delimit == null || vec_delimit.length() == 0) {
      return null;
    }

    // checking NULL value for parameter:vec_idx
    vec_idx = vec_idx - 1;
    if (vec_idx < 0) {
      return null;
    }

    String[] vect;

    vect = str_vec.split(vec_delimit, -1);
    if (vec_idx < vect.length) return vect[vec_idx];
    else return null;
  }
}
