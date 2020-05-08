package com.ebay.sojourner.ubd.common.sharedlib.util;

public class SOJListGetValueByIndex {

  private static boolean a;

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
    if (vec_idx < vect.length) {
      return vect[vec_idx];
    } else {
      return null;
    }
  }

  public static void main(String[] args) {
    String content = "/rover/1/710-154084-954045-4/2?gclid=EAIaIQobChMIwPu5t4qs3A"
        + "IVAQAAAB0BAAAAEAAYACAAEgJVzfD_BwE&geo_id=32251&MT_ID=584476&cmpgn=158341220"
        + "8&crlp=432947547545_&keyword=.22+exploding+pellets&rlsatarget=kwd-312361277726&abcI"
        + "d=1139966&loc=1007032&sitelnk=&mkrid=710-154084-954045-4&poi=&mkevt=1&adpos=&dev"
        + "ice=m&mkc"
        + "id=2&crdt=0&mpre=https%3A%2F%2Fwww.ebay.co.uk%2Fsch%2Fi.html%3F_nkw%3D.22+Explo"
        + "ding+Pellets";
    System.out.println(getValueByIndex(content, "/", 4));

    System.out.println(a);
  }
}
