package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentHash;
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

  private static final String signatureId = "agent";

  @Override
  public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();
    Map<Integer, Integer> signatureStates = agentAttributeAccumulator.getSignatureStatus();
    Set<Integer> botFlagList = agentAttribute.getBotFlagList();
    AgentHash agent = agentAttribute.getAgent();
    long windowEndTime = context.window().maxTimestamp();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      out.collect(new BotSignature(signatureId, agent, null, null,
          new ArrayList<>(botFlagList),
          windowEndTime, false));

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateStates = SignatureUtils.generateNewSignature(signatureStates);

      out.collect(new BotSignature(signatureId, agent, null, null,
          new ArrayList<>(newGenerateStates),
          windowEndTime, true));
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
