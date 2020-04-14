package com.ebay.sojourner.ubd.rt.util;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.common.sharedlib.metrics.IntermediateMetrics;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class FlinkTypeCheckUtil {

  public static void main(String[] args) {
    TypeInformation<UbiSession> t1 = TypeExtractor.createTypeInfo(UbiSession.class);
    TypeInformation<IntermediateMetrics> intermediateMetricsTypeInformation = TypeExtractor
        .createTypeInfo(IntermediateMetrics.class);
    System.out.println(intermediateMetricsTypeInformation);
  }
}
