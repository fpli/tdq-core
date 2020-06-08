package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.flink.common.util.Constants;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AgentIpSignatureWindowProcessFunction extends
    ProcessWindowFunction<AgentIpAttributeAccumulator,
        Tuple5<String, String, Boolean, Set<Integer>, Long>, Tuple, TimeWindow> {

  private final String signatureId = "agentIp";

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentIpAttributeAccumulator> elements,
      Collector<Tuple5<String, String, Boolean, Set<Integer>, Long>> out) {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
    AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();
    Map<Integer, Integer> signatureStates = agentIpAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = agentIpAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      out.collect(
          new Tuple5<>(
              signatureId,
              agentIpAttribute.getAgent().getAgentHash1()
                  + Constants.SEPARATOR + agentIpAttribute.getAgent().getAgentHash2()
                  + Constants.SEPARATOR + agentIpAttribute.getClientIp(),
              false,
              botFlagList,
              context.window().maxTimestamp()));

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateSignatures = SignatureUtils.generateNewSignature(signatureStates);

      out.collect(
          new Tuple5<>(
              signatureId,
              agentIpAttribute.getAgent().getAgentHash1()
                  + Constants.SEPARATOR + agentIpAttribute.getAgent().getAgentHash2()
                  + Constants.SEPARATOR + agentIpAttribute.getClientIp(),
              true,
              newGenerateSignatures,
              context.window().maxTimestamp()));
    }
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);

  }

  @Override
  public void clear(Context context) throws Exception {
    super.clear(context);
  }
}
