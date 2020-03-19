package com.ebay.sojourner.ubd.rt.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class FlinkTypeCheckUtil {

  public static void main(String[] args) {
    TypeInformation<UbiSession> t1 = TypeExtractor.createTypeInfo(UbiSession.class);
    System.out.println(t1);
  }
}
