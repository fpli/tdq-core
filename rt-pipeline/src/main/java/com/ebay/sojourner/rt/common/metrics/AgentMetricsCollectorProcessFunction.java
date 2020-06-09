package com.ebay.sojourner.rt.common.metrics;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class AgentMetricsCollectorProcessFunction extends
    ProcessFunction<BotSignature, BotSignature> {

  private List<String> signatureIdList;
  private Map<String, Counter> agentCounterNameMap = new ConcurrentHashMap<>();

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);

    signatureIdList = Arrays.asList("agent_g", "agent_e");

    for (String signatureId : signatureIdList) {
      Counter agentCounter =
          getRuntimeContext()
              .getMetricGroup()
              .addGroup(Constants.SOJ_METRICS_GROUP)
              .counter(signatureId);
      agentCounterNameMap.put(signatureId, agentCounter);
    }
  }

  @Override
  public void processElement(BotSignature value, Context ctx, Collector<BotSignature> out) {

    SignatureUtils
        .signatureMetricsCollection(agentCounterNameMap, value.getType(), value.getIsGeneration());
    out.collect(null);

  }
}
