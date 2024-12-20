package com.ebay.tdq.planner.utils.udf.common;

import java.util.Arrays;

public final class ByteArray {

  public static final byte[] EMPTY_BYTE = new byte[0];

  /**
   * Writes the content of a byte array into a memory address, identified by an object and an
   * offset. The target memory address must already been allocated, and have enough space to
   * hold all the bytes in this string.
   */
  public static void writeToMemory(byte[] src, Object target, long targetOffset) {
    Platform.copyMemory(src, Platform.BYTE_ARRAY_OFFSET, target, targetOffset, src.length);
  }

  /**
   * Returns a 64-bit integer that can be used as the prefix used in sorting.
   */
  public static long getPrefix(byte[] bytes) {
    if (bytes == null) {
      return 0L;
    } else {
      final int minLen = Math.min(bytes.length, 8);
      long p = 0;
      for (int i = 0; i < minLen; ++i) {
        p |= ((long) Platform.getByte(bytes, Platform.BYTE_ARRAY_OFFSET + i) & 0xff)
            << (56 - 8 * i);
      }
      return p;
    }
  }

  public static byte[] subStringSQL(byte[] bytes, int pos, int len) {
    // This pos calculation is according to UTF8String#subStringSQL
    if (pos > bytes.length) {
      return EMPTY_BYTE;
    }
    int start = 0;
    int end;
    if (pos > 0) {
      start = pos - 1;
    } else if (pos < 0) {
      start = bytes.length + pos;
    }
    if ((bytes.length - start) < len) {
      end = bytes.length;
    } else {
      end = start + len;
    }
    start = Math.max(start, 0); // underflow
    if (start >= end) {
      return EMPTY_BYTE;
    }
    return Arrays.copyOfRange(bytes, start, end);
  }

  public static byte[] concat(byte[]... inputs) {
    // Compute the total length of the result
    int totalLength = 0;
    for (int i = 0; i < inputs.length; i++) {
      if (inputs[i] != null) {
        totalLength += inputs[i].length;
      } else {
        return null;
      }
    }

    // Allocate a new byte array, and copy the inputs one by one into it
    final byte[] result = new byte[totalLength];
    int offset = 0;
    for (int i = 0; i < inputs.length; i++) {
      int len = inputs[i].length;
      Platform.copyMemory(
          inputs[i], Platform.BYTE_ARRAY_OFFSET,
          result, Platform.BYTE_ARRAY_OFFSET + offset,
          len);
      offset += len;
    }
    return result;
  }

}
