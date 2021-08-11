package com.ebay.tdq.planner.utils.udf.soj;

import com.ebay.sojourner.common.util.IsValidPrivateIPv4;
import java.io.Serializable;


public class IsValidPrivateIPv4Hive implements Serializable {

  public boolean evaluate(String guid) {
    if (guid == null) {
      return false;
    }

    return IsValidPrivateIPv4.isValidIP(guid);
  }
}
