package com.ebay.sojourner.ubd.common.sharedlib.util;

public class SOJGetPageType {

  private static int commonCheck(String itemId, String flags) {
    if (itemIdCheck(itemId) == 1 && flags != null) {
      return 1;
    }
    return 0;
  }

  private static int itemIdCheck(String itemId) {
    if (itemId != null && itemId.length() < 20) {
      try {
        Long.parseLong(itemId);
      } catch (Exception e) {
        return 0;
      }
      return 1;
    }
    return 0;
  }

  public static int soj_get_page_type(
      String itemId, String flags, boolean rdt, int id, Integer lkup, Integer fbp) {
    // -1 for integer is same as null
    if (lkup == null) {
      return fbp == null ? 0 : -1;
    }

    switch (lkup) {
      case 4:
        if (commonCheck(itemId, flags) == 1 && SOJExtractFlag.extractFlag(flags, 47) != 1) {
          if (id == 3897 && SOJExtractFlag.extractFlag(flags, 59) == 1 && rdt) {
            return 4;
          }

          if (id != 3897 && SOJExtractFlag.extractFlag(flags, 3) == 1) {
            return 4;
          }
        }
        return 0;

      case 5:
        if (commonCheck(itemId, flags) == 1) {
          if ((id == 2499 || id == 2501)
              && SOJExtractFlag.extractFlag(flags, 19) == 1
              && SOJExtractFlag.extractFlag(flags, 23) != 1) {
            return 5;
          }
          if (!(id == 2499 || id == 2501)
              && (SOJExtractFlag.extractFlag(flags, 6) == 1
              || SOJExtractFlag.extractFlag(flags, 48) == 1)
              && SOJExtractFlag.extractFlag(flags, 47) != 1) {
            return 5;
          }
        }
        return 0;

      case 3:
        if (commonCheck(itemId, flags) == 1) {
          if (SOJExtractFlag.extractFlag(flags, 28) != 1
              && SOJExtractFlag.extractFlag(flags, 36) != 1
              && SOJExtractFlag.extractFlag(flags, 41) != 1
              && SOJExtractFlag.extractFlag(flags, 42) != 1) {
            return 3;
          }
        }
        return 0;

      case 6:
        if (itemIdCheck(itemId) == 1) {
          return 6;
        } else {
          return 0;
        }

      default:
        if ((id == 1637 || id == 1638)
            && (SOJExtractFlag.extractFlag(flags, 78) == 1
            || SOJExtractFlag.extractFlag(flags, 237) == 1
            || SOJExtractFlag.extractFlag(flags, 13) == 1)) {
          return 0;
        } else if ((id == 3286
            || id == 4506
            || id == 3756
            || id == 4737
            || id == 4011
            || id == 4978)
            && SOJExtractFlag.extractFlag(flags, 267) == 1) {
          return 0;
        } else {
          return lkup;
        }
    }
  }
}
