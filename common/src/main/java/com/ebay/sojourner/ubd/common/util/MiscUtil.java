package com.ebay.sojourner.ubd.common.util;

/** @author weifang. */
public class MiscUtil {
  public static final boolean objEquals(Object obj1, Object obj2) {
    if (obj1 == null) {
      return obj2 == null;
    } else {
      return obj1.equals(obj2);
    }
  }

  public static final int objCompareTo(Comparable obj1, Comparable obj2) {
    if (obj1 == null) {
      if (obj2 == null) {
        return 0;
      } else {
        return -1;
      }
    } else {
      return obj1.compareTo(obj2);
    }
  }
}
