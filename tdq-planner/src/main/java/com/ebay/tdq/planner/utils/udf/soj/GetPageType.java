package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;

public class GetPageType implements Serializable {

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

  public static int soj_get_page_type(String itemId, String flags, int rdt, int id, Integer lkup, Integer fbp) {
    // -1 for integer is same as null
    if (lkup == null) {
      return fbp == null ? 0 : -1;
    }

    switch (lkup) {
      case 4:
        if (commonCheck(itemId, flags) == 1 && ExtractFlag.extractFlag(flags, 47) != 1) {
          if (id == 3897 && ExtractFlag.extractFlag(flags, 59) == 1 && rdt == 1) {
            return 4;
          }

          if (id != 3897 && ExtractFlag.extractFlag(flags, 3) == 1) {
            return 4;
          }
        }

        return 0;

      case 5:
        if (commonCheck(itemId, flags) == 1) {
          if ((id == 2499 || id == 2501) && ExtractFlag.extractFlag(flags, 19) == 1
              && ExtractFlag.extractFlag(flags, 23) != 1) {
            return 5;
          }
          if (!(id == 2499 || id == 2501) && (ExtractFlag.extractFlag(flags, 6) == 1
              || ExtractFlag.extractFlag(flags, 48) == 1) && ExtractFlag.extractFlag(flags, 47) != 1) {
            return 5;
          }
        }
        return 0;

      case 3:
        if (commonCheck(itemId, flags) == 1) {
          if (ExtractFlag.extractFlag(flags, 28) != 1 && ExtractFlag.extractFlag(flags, 36) != 1
              && ExtractFlag.extractFlag(flags, 41) != 1
              && ExtractFlag.extractFlag(flags, 42) != 1) {
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
        if ((id == 1637 || id == 1638) && (ExtractFlag.extractFlag(flags, 78) == 1
            || ExtractFlag.extractFlag(flags, 237) == 1 || ExtractFlag.extractFlag(flags, 13) == 1)) {
          return 0;
        } else if ((id == 3286 || id == 4506 || id == 3756 || id == 4737 || id == 4011 || id == 4978)
            && ExtractFlag.extractFlag(flags, 267) == 1) {
          return 0;
        } else {
          return lkup;
        }
    }
  }

  public Integer evaluate(String itemId, String flags, int rdt, int id, Integer lkup, Integer fbp) {
    if (itemId == null || flags == null || rdt < 0 || id < 0 || lkup == null || fbp == null) {
      return null; //check if this is okay
    }

    return soj_get_page_type(itemId, flags, rdt, id, lkup, fbp);
  }

}
