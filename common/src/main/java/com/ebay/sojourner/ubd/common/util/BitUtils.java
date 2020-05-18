package com.ebay.sojourner.ubd.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitUtils {

  public static int checkBit(long l, int index) {
    if (index <= 0) {
      log.error("please input a valid parameter");
    }
    if (l == 0) {
      return 0;
    } else {
      return (l & (1 << (index - 1))) > 0 ? 1 : 0;
    }
  }

  public static long setBit(long source, int pos) {
    return source |= (long) 1 << pos;
  }

  public static boolean isBitSet(long source, int pos) {
    return (source & (long) 1 << pos) != 0;
  }

  public static void main(String[] args) {

    System.out.println(setBit(0,4));
  }
}
