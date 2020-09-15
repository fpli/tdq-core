package com.ebay.sojourner.rt.operator.attribute;

import com.ebay.sojourner.common.model.AgentAttribute;
import com.ebay.sojourner.common.model.AgentAttributeAccumulator;
import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.SignatureInfo;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

@Slf4j
public class AgentWindowProcessFunction extends
    ProcessWindowFunction<AgentAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private static final String signatureId = "agent";

  @Override
  public void process(Tuple tuple, Context context, Iterable<AgentAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    AgentAttributeAccumulator agentAttributeAccumulator = elements.iterator().next();
    AgentAttribute agentAttribute = agentAttributeAccumulator.getAgentAttribute();
    Map<Integer, SignatureInfo> signatureStates = agentAttributeAccumulator.getSignatureStatus();
    AgentHash agent = agentAttribute.getAgent();
    long windowEndTime = context.window().maxTimestamp();
    long timestamp = SojTimestamp.getSojTimestampToUnixTimestamp(agentAttribute.getTimestamp());

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && signatureStates.size() > 0) {
      sendSignatures(agent, timestamp, signatureStates, out, context);
      out.collect(new BotSignature(signatureId, agent, null, null,
          new ArrayList<>(signatureStates.keySet()),
          windowEndTime, false, 3, windowEndTime));
    } else if (context.currentWatermark() < context.window().maxTimestamp()) {
      sendSignatures(agent, timestamp, signatureStates, out, context);
    }
  }

  private void sendSignatures(AgentHash agent, long timestamp,
      Map<Integer, SignatureInfo> signatureStates, Collector<BotSignature> out, Context context) {

    for (Map.Entry<Integer, SignatureInfo> entry : signatureStates.entrySet()) {
      if (!entry.getValue().isSent()) {
        out.collect(new BotSignature(signatureId, agent, null, null,
            new ArrayList<>(Arrays.asList(entry.getKey())),
            context.window().maxTimestamp(), true, entry.getValue().getType(),
            timestamp));
      }
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
