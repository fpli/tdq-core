package com.ebay.sojourner.ubd.rt.operators.attribute;

import com.ebay.sojourner.ubd.common.model.AgentIpAttribute;
import com.ebay.sojourner.ubd.common.model.AgentIpAttributeAccumulator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AgentIpSignatureWindowProcessFunction
    extends ProcessWindowFunction<
    AgentIpAttributeAccumulator,
    Tuple4<String, Boolean, Set<Integer>, Long>,
    Tuple,
    TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentIpAttributeAccumulator> elements,
      Collector<Tuple4<String, Boolean, Set<Integer>, Long>> out)
      throws Exception {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
    AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && agentIpAttribute.getBotFlagList() != null
        && agentIpAttribute.getBotFlagList().size() > 0) {
      out.collect(
          new Tuple4<>(
              "agentIp" + agentIpAttribute.getAgent() + agentIpAttribute.getClientIp(),
              false,
              agentIpAttribute.getBotFlagList(),
              context.window().maxTimestamp()));
    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && agentIpAttributeAccumulator.getBotFlagStatus().containsValue(1)
        && agentIpAttribute.getBotFlagList() != null
        && agentIpAttribute.getBotFlagList().size() > 0) {
      HashSet<Integer> generationBotFlag = new HashSet<>();
      for (Map.Entry<Integer, Integer> newBotFlagMap :
          agentIpAttributeAccumulator.getBotFlagStatus().entrySet()) {
        if (newBotFlagMap.getValue() == 1) {
          generationBotFlag.add(newBotFlagMap.getKey());
        }
      }
      out.collect(
          new Tuple4<>(
              "agentIp" + agentIpAttribute.getAgent() + agentIpAttribute.getClientIp(),
              true,
              generationBotFlag,
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
