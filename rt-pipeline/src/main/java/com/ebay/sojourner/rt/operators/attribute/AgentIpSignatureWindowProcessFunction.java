package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.AgentHash;
import com.ebay.sojourner.common.model.AgentIpAttribute;
import com.ebay.sojourner.common.model.AgentIpAttributeAccumulator;
import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.SignatureInfo;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

@Slf4j
public class AgentIpSignatureWindowProcessFunction extends
    ProcessWindowFunction<AgentIpAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private static final String signatureId = "agentIp";

  @Override
  public void process(Tuple tuple, Context context, Iterable<AgentIpAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    AgentIpAttributeAccumulator agentIpAttributeAccumulator = elements.iterator().next();
    AgentIpAttribute agentIpAttribute = agentIpAttributeAccumulator.getAgentIpAttribute();
    Map<Integer, SignatureInfo> signatureStates = agentIpAttributeAccumulator.getSignatureStatus();
    Set<Integer> botFlagList = agentIpAttribute.getBotFlagList();
    AgentHash agent = agentIpAttribute.getAgent();
    Integer clientIp = agentIpAttribute.getClientIp();
    long windowEndTime = context.window().maxTimestamp();
    long timestamp = SojTimestamp.getSojTimestampToUnixTimestamp(agentIpAttribute.getTimestamp());
    if (context.currentWatermark() >= context.window().maxTimestamp()&&signatureStates.size()>0) {
      sendSignatures(agent, clientIp, timestamp,signatureStates, out, context);
      out.collect(new BotSignature(signatureId, agent, clientIp, null,
          new ArrayList<>(signatureStates.keySet()),
          windowEndTime, false, 3, windowEndTime));
    //     log.info("AgentIPAttribute: "+agentIpAttribute.toString());
    } else if (context.currentWatermark() < context.window().maxTimestamp()
    ) {
      sendSignatures(agent, clientIp,timestamp, signatureStates, out, context);
      //      Set<Integer> newGenerateSignatures = SignatureUtils.generateNewSignature
      //      (signatureStates);
      //
      //      out.collect(new BotSignature(signatureId, agent, clientIp, null,
      //          new ArrayList<>(newGenerateSignatures),
      //          windowEndTime, true));

    }
  }

  private void sendSignatures(AgentHash agent,
      Integer clientIp,long timestamp, Map<Integer, SignatureInfo> signatureStates,
      Collector<BotSignature> out, Context context) {
    for (Map.Entry<Integer, SignatureInfo> entry : signatureStates.entrySet()) {
      if (!entry.getValue().isSent()) {
        out.collect(new BotSignature(signatureId, agent, clientIp, null,
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
