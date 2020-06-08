package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AgentWindowProcessFunction extends
    ProcessWindowFunction<AgentAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private final String signatureId = "agent";

  @Override
  public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();
    Map<Integer, Integer> signatureStates = agentAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = agentAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      BotSignature botSignature = new BotSignature();
      botSignature.setType(signatureId);
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(agentAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentAttribute.getAgent());
      out.collect(botSignature);

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateStates = SignatureUtils.generateNewSignature(signatureStates);

      BotSignature botSignature = new BotSignature();
      botSignature.setType(signatureId);
      botSignature.setIsGeneration(true);
      botSignature.setBotFlags(new ArrayList<>(newGenerateStates));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setUserAgent(agentAttribute.getAgent());
      out.collect(botSignature);
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
