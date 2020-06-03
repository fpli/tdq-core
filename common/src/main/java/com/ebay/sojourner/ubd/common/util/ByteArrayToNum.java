package com.ebay.sojourner.ubd.common.util;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class ByteArrayToNum {

  public static final int ARRAY_LENGTH = 64;

  public static int getInt(byte[] b64) {
    int value = 0;
    if (b64 != null && b64.length == ARRAY_LENGTH) {
      for (int i = 0; i < ARRAY_LENGTH; i++) {
        value |= b64[i] << i;
      }
    }
    return value;
  }

  public static long getLong(byte[] b64) {
    long value = 0L;
    if (b64 != null && b64.length == ARRAY_LENGTH) {
      for (int i = 0; i < ARRAY_LENGTH; i++) {
        value |= ((long) b64[i]) << i;
      }
    }
    return value;
  }

  public static long getLong2(AtomicIntegerArray ai) {
    long value = 0L;
    if (ai != null && ai.length() == ARRAY_LENGTH) {
      for (int i = 0; i < ARRAY_LENGTH; i++) {
        value |= ((long) ai.get(i)) << i;
      }
    }
    return value;
  }
}
