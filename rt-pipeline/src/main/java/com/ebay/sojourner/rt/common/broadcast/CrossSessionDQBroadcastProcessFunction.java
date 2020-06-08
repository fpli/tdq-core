package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.util.TransformUtil;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class CrossSessionDQBroadcastProcessFunction extends BroadcastProcessFunction<
    IntermediateSession, Tuple5<String, String, Boolean, Set<Integer>, Long>, IntermediateSession> {

  @Override
  public void processElement(IntermediateSession intermediateSession, ReadOnlyContext context,
      Collector<IntermediateSession> out) throws Exception {

    ReadOnlyBroadcastState<String, Map<Integer, Long>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    // ip
    Integer ipEnhance = TransformUtil.ipToInt(intermediateSession.getClientIp());
    if (attributeSignature.contains("ip" + ipEnhance + "/007")) {
      for (Map.Entry<Integer, Long> ipBotFlagMap :
          attributeSignature.get("ip" + ipEnhance + "/007").entrySet()) {
        intermediateSession.getBotFlagList().add(ipBotFlagMap.getKey());
      }
    }

    // agent
    long[] agentEnhance = TransformUtil.md522Long(intermediateSession.getUserAgent());
    if (attributeSignature.contains("agent" + agentEnhance[0] + "/007" + agentEnhance[1])) {
      for (Map.Entry<Integer, Long> agentBotFlagMap : attributeSignature
          .get("agent" + agentEnhance[0] + "/007" + agentEnhance[1]).entrySet()) {
        intermediateSession.getBotFlagList().add(agentBotFlagMap.getKey());
      }
    }

    // agentIp
    if (attributeSignature.contains(
        "agentIp" + agentEnhance[0] + "/007" + agentEnhance[1] + "/007" + ipEnhance)) {
      for (Map.Entry<Integer, Long> agentIpBotFlagMap : attributeSignature
          .get("agentIp" + agentEnhance[0] + "/007" + agentEnhance[1] + "/007" + ipEnhance)
          .entrySet()) {
        intermediateSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
      }
    }

    // guid
    long[] guidEnhance = TransformUtil.md522Long(intermediateSession.getGuid());
    if (attributeSignature.contains("guid" + guidEnhance[0] + "/007" + guidEnhance[1])) {
      for (Map.Entry<Integer, Long> guidBotFlagMap :
          attributeSignature.get("guid" + guidEnhance[0] + "/007" + guidEnhance[1]).entrySet()) {
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
  public void processBroadcastElement(
      Tuple5<String, String, Boolean, Set<Integer>, Long> attributeSignature,
      Context context,
      Collector<IntermediateSession> out)
      throws Exception {

    BroadcastState<String, Map<Integer, Long>> attributeBroadcastStatus =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    Set<Integer> botFlags = attributeSignature.f3;
    String signatureId = attributeSignature.f0;
    String signatureValue = attributeSignature.f1;
    Long expirationTime = attributeSignature.f4;
    Boolean isGeneration = attributeSignature.f2;

    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (attributeBroadcastStatus.get(signatureId + signatureValue) != null) {
          if (attributeBroadcastStatus.get(signatureId + signatureValue).containsKey(botFlag)) {
            if (expirationTime > attributeBroadcastStatus
                .get(signatureId + signatureValue).get(botFlag)) {
              attributeBroadcastStatus.get(signatureId + signatureValue)
                  .put(botFlag, expirationTime);
            }
          } else {
            attributeBroadcastStatus.get(signatureId + signatureValue).put(botFlag, expirationTime);
          }
        } else {
          HashMap<Integer, Long> newBotFlagStatus = new HashMap<>();
          newBotFlagStatus.put(botFlag, expirationTime);
          attributeBroadcastStatus.put(signatureId + signatureValue, newBotFlagStatus);
        }
      }
    } else {
      Map<Integer, Long> signatureBotFlagStatus = attributeBroadcastStatus
          .get(signatureId + signatureValue);
      if (signatureBotFlagStatus != null) {
        for (int botFlag : botFlags) {
          if (signatureBotFlagStatus.containsKey(botFlag)) {
            if (expirationTime > signatureBotFlagStatus.get(botFlag)) {
              signatureBotFlagStatus.remove(botFlag);
              if (signatureBotFlagStatus.size() == 0) {
                attributeBroadcastStatus.remove(signatureId + signatureValue);
              }
            }
          }
        }
      }
    }
  }
}
