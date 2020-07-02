package com.ebay.sojourner.rt.operators.attribute;

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
      sendSignatures(agent, timestamp, signatureStates, out, context, agentAttribute);
      out.collect(new BotSignature(signatureId, agent, null, null,
          new ArrayList<>(signatureStates.keySet()),
          windowEndTime, false, 3, windowEndTime));
      StringBuffer sb = new StringBuffer();
      for (SignatureInfo signatureInfo : signatureStates.values()) {
        sb.append(signatureInfo.toString()).append(";");
      }
      //      if (agent.getAgentHash1() == 4780249662515408496L
      //          && agent.getAgentHash2() == 8995480885048214972L) {
      //        log.info(String.format("window:[%s,%s] , waterMark:%s , category :%s ,
      //        agentAttribute: "
      //                + "%s",
      //            context.window().getStart(), context.window().getEnd(), context
      //            .currentWatermark(),
      //            sb.toString(),
      //            agentAttribute.toString()));
      //      }
    } else if (context.currentWatermark() < context.window().maxTimestamp()) {

      sendSignatures(agent, timestamp, signatureStates, out, context, agentAttribute);
      //      Set<Integer> newGenerateStates = SignatureUtils.generateNewSignature(signatureStates);
      //
      //      out.collect(new BotSignature(signatureId, agent, null, null,
      //          new ArrayList<>(newGenerateStates),
      //          windowEndTime, true,));
    }
  }

  private void sendSignatures(AgentHash agent,
      long timestamp, Map<Integer, SignatureInfo> signatureStates,
      Collector<BotSignature> out, Context context, AgentAttribute agentAttribute) {
    for (Map.Entry<Integer, SignatureInfo> entry : signatureStates.entrySet()) {
      if (!entry.getValue().isSent()) {
        //        if (agent.getAgentHash1() == 4780249662515408496L
        //            && agent.getAgentHash2() == 8995480885048214972L) {
        //          log.info(String.format("window:[%s,%s] , waterMark:%s , category :%s ,
        //          agentAttribute: "
        //                  + "%s",
        //              context.window().getStart(), context.window().getEnd(), context
        //              .currentWatermark(),
        //              entry.getValue().toString(),
        //                agentAttribute.toString()));
        //        }
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
