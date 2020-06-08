package com.ebay.sojourner.rt.common.metrics;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.flink.common.util.Constants;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class IpMetricsCollectorProcessFunction extends ProcessFunction<BotSignature, BotSignature> {

  private List<String> signatureIdList;
  private Map<String, Counter> ipCounterNameMap = new ConcurrentHashMap<>();

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    signatureIdList = Arrays.asList("ip_g", "ip_e");

    for (String signatureId : signatureIdList) {
      Counter ipCounter =
          getRuntimeContext()
              .getMetricGroup()
              .addGroup(Constants.SOJ_METRICS_GROUP)
              .counter(signatureId);
      ipCounterNameMap.put(signatureId, ipCounter);
    }
  }

  @Override
  public void processElement(BotSignature value, Context ctx, Collector<BotSignature> out) {

    SignatureUtils
        .signatureMetricsCollection(ipCounterNameMap, value.getType(), value.getIsGeneration());
    out.collect(null);

  }
}