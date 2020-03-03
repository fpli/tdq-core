package com.ebay.sojourner.ubd.common.sharedlib.util;

/** Created by qingxu1 on 2017/7/5. */
public class SojGetBase64EncodedBitsSets {

  private static final int MAX_RESULT_SIZE = 4096;

  private static final int[] ascii_to_base64 = {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };

  public static String getBase64EncodedBitsSetValue(String sojflag) {
    int sojflag_len;
    int bit_pos = 0;
    int mybytepos;
    int return_val = 0;
    int result_len;
    int freespace;
    int written;
    String result = "";

    if (sojflag == null || sojflag.equals("")) {
      return null;
    }

    sojflag_len = sojflag.length();

    for (mybytepos = 0, bit_pos = 0; bit_pos < 4096; bit_pos++) {
      mybytepos = bit_pos / 6;

      if (mybytepos >= sojflag_len) {
        break;
      }

      char charAt = sojflag.charAt(mybytepos);

      System.out.println("charAt : " + charAt);
      System.out.println("ascii_to_base64[charAt] : " + ascii_to_base64[charAt]);

      if (ascii_to_base64[charAt] < 0) {
        break;
      }

      return_val = ((ascii_to_base64[charAt] >> (5 - (bit_pos % 6))) & 1);

      System.out.println("return_val : " + return_val);

      if (return_val == 1) {

        if (result == "") {
          result_len = 0;
        } else {
          result_len = result.length();
        }

        freespace = MAX_RESULT_SIZE - result_len;
        if (freespace <= 1) {
          break;
        }
        if (result_len == 0) {
          written = String.valueOf(bit_pos).length();
          if (String.valueOf(bit_pos).length() >= freespace) {
            result = result + String.valueOf(bit_pos).substring(0, freespace - 1);
          } else {
            result = result + bit_pos;
          }

          if (written >= freespace) {
            break;
          }

        } else {
          written = String.valueOf(bit_pos).length();
          if (String.valueOf(bit_pos).length() >= freespace) {
            result = result + "," + String.valueOf(bit_pos).substring(0, freespace - 1);
          } else {
            result = result + "," + bit_pos;
          }

          if (written >= freespace) {
            break;
          }
        }
      }
    }

    if (result == "") {
      return null;
    } else {
      return result;
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println(
        "value = "
            + getBase64EncodedBitsSetValue(
                "EGAEAAAAQAAEAwEACACUAAAAAAAAAIAAAAIAAQIAAAAAAAAAgAAAABIAEAg"
                    + "ESQAAAQAADAAAoAYABAAIAAAAAgAAgKBAAAAAAAAAAAQAAEWgQJBCo"
                    + "cAAEENCEAIAmAgKBAQEACgCBCAwkAIBAACARIIAgAAgQAAAAIAEAAAABBA"));
  }
}
