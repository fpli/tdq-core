package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
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

public class AgentWindowProcessFunction extends ProcessWindowFunction<AgentAttributeAccumulator,
    Tuple5<String, String, Boolean, Set<Integer>, Long>, Tuple, TimeWindow> {

  private final String signatureId = "agent";

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentAttributeAccumulator> elements,
      Collector<Tuple5<String, String, Boolean, Set<Integer>, Long>> out) {

    AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();
    Map<Integer, Integer> signatureStates = agentAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = agentAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      out.collect(
          new Tuple5<>(
              signatureId,
              agentAttribute.getAgent().getAgentHash1()
                  + Constants.SEPARATOR + agentAttribute.getAgent().getAgentHash2(),
              false,
              botFlagList,
              context.window().maxTimestamp()));

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateStates = SignatureUtils.generateNewSignature(signatureStates);

      out.collect(
          new Tuple5<>(
              signatureId,
              agentAttribute.getAgent().getAgentHash1()
                  + Constants.SEPARATOR + agentAttribute.getAgent().getAgentHash2(),
              true,
              newGenerateStates,
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
