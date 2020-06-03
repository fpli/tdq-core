package com.ebay.sojourner.business.ubd.util;

import java.util.HashMap;

public class TypeTransUtil {

  public static HashMap<String, Object> ObjectToHashMap(Object o) {
    return (HashMap<String, Object>) o;
  }

  public static HashMap<String, String> ObjectToHashMap1(Object o) {
    return (HashMap<String, String>) o;
  }

  public static String ObjectToString(Object o) {
    return String.valueOf(o);
  }

  public static Integer ObjectToInteger(Object o) {
    String str = ObjectToString(o);
    return Integer.valueOf(str);
  }

  public static Long ObjectToLong(Object o) {
    String str = ObjectToString(o);
    return Long.valueOf(str);
  }

  public static String LongToString(Long lon) {
    return String.valueOf(lon);
  }

  public static String IntegerToString(Integer integer) {
    return String.valueOf(integer);
  }
}
