package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.BotSignature;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AgentIpSignatureWindowProcessFunction
    extends ProcessWindowFunction<
    AgentIpAttributeAccumulator,
    BotSignature,
    Tuple,
    TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<AgentIpAttributeAccumulator> elements,
      Collector<BotSignature> out)
      throws Exception {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
    AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && agentIpAttribute.getBotFlagList() != null
        && agentIpAttribute.getBotFlagList().size() > 0) {
      BotSignature botSignature = new BotSignature();
      botSignature.setType("agentIp");
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(agentIpAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentIpAttribute.getAgent());
      botSignature.setIp(agentIpAttribute.getClientIp());
      out.collect(
          botSignature);
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
      BotSignature botSignature = new BotSignature();
      botSignature.setType("agentIp");
      botSignature.setIsGeneration(true);
      botSignature.setBotFlags(new ArrayList<>(generationBotFlag));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentIpAttribute.getAgent());
      botSignature.setIp(agentIpAttribute.getClientIp());
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
