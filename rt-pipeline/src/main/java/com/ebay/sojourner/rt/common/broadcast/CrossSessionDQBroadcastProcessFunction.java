package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.rt.common.state.MapStateDesc;
import com.ebay.sojourner.rt.util.TransformUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class CrossSessionDQBroadcastProcessFunction extends BroadcastProcessFunction<
    IntermediateSession, Tuple4<String, Boolean, Set<Integer>, Long>, IntermediateSession> {

  @Override
  public void processElement(IntermediateSession intermediateSession, ReadOnlyContext context,
      Collector<IntermediateSession> out) throws Exception {

    ReadOnlyBroadcastState<String, Map<Integer, Long>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    // ip
    if (attributeSignature.contains("ip" + intermediateSession.getClientIp())) {
      for (Map.Entry<Integer, Long> ipBotFlagMap :
          attributeSignature.get("ip" + intermediateSession.getClientIp()).entrySet()) {
        intermediateSession.getBotFlagList().add(ipBotFlagMap.getKey());
      }
    }

    // agent
    if (attributeSignature.contains("agent" + intermediateSession.getUserAgent())) {
      for (Map.Entry<Integer, Long> agentBotFlagMap :
          attributeSignature.get("agent" + intermediateSession.getUserAgent()).entrySet()) {
        intermediateSession.getBotFlagList().add(agentBotFlagMap.getKey());
      }
    }

    // agentIp
    if (attributeSignature.contains(
        "agentIp" + intermediateSession.getUserAgent() + intermediateSession.getClientIp())) {
      for (Map.Entry<Integer, Long> agentIpBotFlagMap :
          attributeSignature
              .get("agentIp" + intermediateSession.getUserAgent() + intermediateSession
                  .getClientIp())
              .entrySet()) {
        intermediateSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
      }
    }

    // guid
    Long[] guidEnhance = TransformUtil.stringToLong(intermediateSession.getGuid());
    if (attributeSignature.contains("guid" + guidEnhance[0] + guidEnhance[1])) {
      for (Map.Entry<Integer, Long> guidBotFlagMap :
          attributeSignature.get("guid" + guidEnhance[0] + guidEnhance[1]).entrySet()) {
        intermediateSession.getBotFlagList().add(guidBotFlagMap.getKey());
      }
    }

    if ((intermediateSession.getBotFlagList().contains(221) && intermediateSession.getBotFlagList()
        .contains(223))
        || (intermediateSession.getBotFlagList().contains(220)
        && intermediateSession.getBotFlagList().contains(222))) {
      intermediateSession.getBotFlagList().add(202);
    }

    if ((intermediateSession.getBotFlagList().contains(221) && intermediateSession.getBotFlagList()
        .contains(223))
        || (intermediateSession.getBotFlagList().contains(220)
        && intermediateSession.getBotFlagList().contains(222))) {
      intermediateSession.getBotFlagList().add(210);
    }

    if (intermediateSession.getBotFlagList().contains(223)) {
      intermediateSession.getBotFlagList().add(211);
    }
    out.collect(intermediateSession);
  }

  @Override
  public void processBroadcastElement(Tuple4<String, Boolean, Set<Integer>, Long> value,
      Context context, Collector<IntermediateSession> out) throws Exception {
    BroadcastState<String, Map<Integer, Long>> attributeBroadcastStatus =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
    Set<Integer> botFlags = value.f2;
    String signatureId = value.f0;
    Long expirationTime = value.f3;
    Boolean isGeneration = value.f1;

    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (attributeBroadcastStatus.get(signatureId) != null) {
          if (attributeBroadcastStatus.get(signatureId).containsKey(botFlag)) {
            if (expirationTime > attributeBroadcastStatus.get(signatureId).get(botFlag)) {
              attributeBroadcastStatus.get(signatureId).put(botFlag, expirationTime);
            }
          } else {
            attributeBroadcastStatus.get(signatureId).put(botFlag, expirationTime);
          }
        } else {
          HashMap<Integer, Long> newBotFlagStatus = new HashMap<>();
          newBotFlagStatus.put(botFlag, expirationTime);
          attributeBroadcastStatus.put(signatureId, newBotFlagStatus);
        }
      }
    } else {
      Map<Integer, Long> signatureBotFlagStatus = attributeBroadcastStatus.get(signatureId);
      if (signatureBotFlagStatus != null) {
        for (int botFlag : botFlags) {
          if (signatureBotFlagStatus.containsKey(botFlag)) {
            if (expirationTime > signatureBotFlagStatus.get(botFlag)) {
              signatureBotFlagStatus.remove(botFlag);
              if (signatureBotFlagStatus.size() == 0) {
                attributeBroadcastStatus.remove(signatureId);
              }
            }
          }
        }
      }
    }
  }
}
