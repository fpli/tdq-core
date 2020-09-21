package com.ebay.sojourner.common.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BitUtils {

  public static long setBit(long source, int pos) {
    Preconditions.checkArgument(pos >= 0 && pos < 64);
    return source | (long) 1 << pos;
  }

  public static int setBit(int source, int pos) {
    Preconditions.checkArgument(pos >= 0 && pos < 64);
    return source | 1 << pos;
  }

  public static boolean isBitSet(int source, int pos) {
    Preconditions.checkArgument(pos >= 0 && pos < 64);
    return (source & 1 << pos) != 0;
  }

  public static boolean isBitSet(long source, int pos) {
    Preconditions.checkArgument(pos >= 0 && pos < 64);
    return (source & (long) 1 << pos) != 0;
  }
}
