package com.ebay.sojourner.common.util;

public class SOJSetBit64 {

  static int OK = 0;
  static int FAIL = -1;
  static int NULLIND = -1;

  public static final byte[] intToByteArray(int value) {
    return new byte[]{
        (byte) (value), (byte) (value >>> 8), (byte) (value >>> 16), (byte) (value >>> 24)
    };
  }

  public static byte[] setBit64(
      String flags,
      int flags_len,
      int byte_offset,
      int bit0,
      int bit1,
      int bit2,
      int bit3,
      int bit4,
      int bit5,
      int bit6,
      int bit7,
      int bit8,
      int bit9,
      int bit10,
      int bit11,
      int bit12,
      int bit13,
      int bit14,
      int bit15,
      int bit16,
      int bit17,
      int bit18,
      int bit19,
      int bit20,
      int bit21,
      int bit22,
      int bit23,
      int bit24,
      int bit25,
      int bit26,
      int bit27,
      int bit28,
      int bit29,
      int bit30,
      int bit31,
      int bit32,
      int bit33,
      int bit34,
      int bit35,
      int bit36,
      int bit37,
      int bit38,
      int bit39,
      int bit40,
      int bit41,
      int bit42,
      int bit43,
      int bit44,
      int bit45,
      int bit46,
      int bit47,
      int bit48,
      int bit49,
      int bit50,
      int bit51,
      int bit52,
      int bit53,
      int bit54,
      int bit55,
      int bit56,
      int bit57,
      int bit58,
      int bit59,
      int bit60,
      int bit61,
      int bit62,
      int bit63) {

    if (flags == null) {
      flags = "\0\0\0\0\0\0\0\0";
    } else {
      int len = 8 - flags.length();
      if (len > 0) {
        for (int i = 0; i < len; i++) {
          flags = flags + '\0';
        }
      }
    }

    // Set 64 bits.
    long result32_1 = 0, result32_2 = 0;
    byte[] flags_out = new byte[256];

    if (byte_offset < 0) {
      // sprintf( error_message, "Byte offset must be >= 0, found %d",
      // *byte_offset );
      // sprintf( sqlstate, "U0001" );
      return flags_out;
    }

    if (flags_len < 0) {
      // sprintf( error_message, "Flags length must be >= 0, found %d",
      // *flags_len );
      // sprintf( sqlstate, "U0001" );
      return flags_out;
    }

    // Validate offset.
    if (byte_offset + 7 >= 256) {
      // sprintf( error_message,
      // "Expected byte offset <= 256 - 8, found %d", *byte_offset );
      // sprintf( sqlstate, "U0001" );
      return flags_out;
    }

    if (byte_offset % 8 != 0) {
      // sprintf( error_message,
      // "Expected byte offset to be multiple of 8, found %d",
      // *byte_offset );
      // sprintf( sqlstate, "U0001" );
      return flags_out;
    }

    System.arraycopy(flags.getBytes(), 0, flags_out, 0, flags_len);

    // Pad 0 bits to flag if result is larger than input.
    if (byte_offset >= flags_len) {
      int i = 0;
      for (i = flags_len; i < byte_offset; i++) {
        flags_out[i] = 0;
      }
    }

    if (byte_offset < flags_len) {
      result32_1 = (int) (flags.charAt(byte_offset));
      result32_2 = (int) (flags.charAt(byte_offset + 4));
    }

    result32_1 |=
        bit0 << 0
            | bit1 << 1
            | bit2 << 2
            | bit3 << 3
            | bit4 << 4
            | bit5 << 5
            | bit6 << 6
            | bit7 << 7
            | bit8 << 8
            | bit9 << 9
            | bit10 << 10
            | bit11 << 11
            | bit12 << 12
            | bit13 << 13
            | bit14 << 14
            | bit15 << 15
            | bit16 << (16 + 0)
            | bit17 << (16 + 1)
            | bit18 << (16 + 2)
            | bit19 << (16 + 3)
            | bit20 << (16 + 4)
            | bit21 << (16 + 5)
            | bit22 << (16 + 6)
            | bit23 << (16 + 7)
            | bit24 << (16 + 8)
            | bit25 << (16 + 9)
            | bit26 << (16 + 10)
            | bit27 << (16 + 11)
            | bit28 << (16 + 12)
            | bit29 << (16 + 13)
            | bit30 << (16 + 14)
            | bit31 << (16 + 15);

    // fix bit 48 issue
    result32_2 |=
        bit32 << 0
            | bit33 << 1
            | bit34 << 2
            | bit35 << 3
            | bit36 << 4
            | bit37 << 5
            | bit38 << 6
            | bit39 << 7
            | bit40 << 8
            | bit41 << 9
            | bit42 << 10
            | bit43 << 11
            | bit44 << 12
            | bit45 << 13
            | bit46 << 14
            | bit47 << 15
            | bit48 << (16 + 0)
            | bit49 << (16 + 1)
            | bit50 << (16 + 2)
            | bit51 << (16 + 3)
            | bit52 << (16 + 4)
            | bit53 << (16 + 5)
            | bit54 << (16 + 6)
            | bit55 << (16 + 7)
            | bit56 << (16 + 8)
            | bit57 << (16 + 9)
            | bit58 << (16 + 10)
            | bit59 << (16 + 11)
            | bit60 << (16 + 12)
            | bit61 << (16 + 13)
            | bit62 << (16 + 14)
            | bit63 << (16 + 15);

    byte[] b1, b2;
    b1 = intToByteArray((int) result32_1);
    b2 = intToByteArray((int) result32_2);
    System.arraycopy(b1, 0, flags_out, byte_offset, 4);
    System.arraycopy(b2, 0, flags_out, byte_offset + 4, 4);
    return flags_out;
  }
}
