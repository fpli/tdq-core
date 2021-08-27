package com.ebay.tdq.planner.utils.udf.soj;


import java.io.Serializable;

public class GUID2IPHive implements Serializable {

  public String evaluate(String guid) {
    if (guid == null) {
      return null;
    }

    return GUID2IP.getIP(guid);
  }
}
