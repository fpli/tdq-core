package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.BotSignature;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AgentWindowProcessFunction
    extends ProcessWindowFunction<
    AgentAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentAttributeAccumulator> elements,
      Collector<BotSignature> out)
      throws Exception {

    AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && agentAttribute.getBotFlagList() != null
        && agentAttribute.getBotFlagList().size() > 0) {
      BotSignature botSignature = new BotSignature();
      botSignature.setType("agent");
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(agentAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentAttribute.getAgent());
      out.collect(
          botSignature);
    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && agentAttributeAccumulator.getBotFlagStatus().containsValue(1)
        && agentAttribute.getBotFlagList() != null
        && agentAttribute.getBotFlagList().size() > 0) {
      HashSet<Integer> generationBotFlag = new HashSet<>();
      for (Map.Entry<Integer, Integer> newBotFlagMap :
          agentAttributeAccumulator.getBotFlagStatus().entrySet()) {
        if (newBotFlagMap.getValue() == 1) {
          generationBotFlag.add(newBotFlagMap.getKey());
        }
      }
      BotSignature botSignature = new BotSignature();
      botSignature.setType("agent");
      botSignature.setIsGeneration(true);
      botSignature.setBotFlags(new ArrayList<>(generationBotFlag));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentAttribute.getAgent());
      out.collect(
          botSignature);
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
