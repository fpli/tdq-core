package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import com.ebay.sojourner.rt.util.TransformUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class CrossSessionDQBroadcastProcessFunction extends BroadcastProcessFunction<
    IntermediateSession, BotSignature, IntermediateSession> {

  public static final String FIELD_DELIM = "\007";

  @Override
  public void processElement(IntermediateSession intermediateSession, ReadOnlyContext context,
      Collector<IntermediateSession> out) throws Exception {

    ReadOnlyBroadcastState<String, Map<String, Map<Integer, Long>>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    // ip
    String ip = TransformUtil.ipToInt(intermediateSession.getIp()) == null ? "0"
        : TransformUtil.ipToInt(intermediateSession.getIp()).toString();
    Map<String, Map<Integer, Long>> ipSignature = attributeSignature.get("ip");
    if (ipSignature.containsKey(ip)) {
      for (Map.Entry<Integer, Long> ipBotFlagMap :
          ipSignature.get(ip).entrySet()) {
        intermediateSession.getBotFlagList().add(ipBotFlagMap.getKey());
      }
    }

    // agent
    long[] long4AgentHash = TransformUtil
        .md522Long(TransformUtil.getMD5(intermediateSession.getUserAgent()));
    Map<String, Map<Integer, Long>> agentSignature = attributeSignature.get("agent");
    String agent = long4AgentHash[0] + FIELD_DELIM + long4AgentHash[1];
    if (agentSignature.containsKey(agent)) {
      for (Map.Entry<Integer, Long> agentBotFlagMap :
          agentSignature.get(agent).entrySet()) {
        intermediateSession.getBotFlagList().add(agentBotFlagMap.getKey());
      }
    }

    // agentIp
    Map<String, Map<Integer, Long>> agentIpSignature = attributeSignature.get("agentIp");
    String agentIp = long4AgentHash[0] + FIELD_DELIM + long4AgentHash[1] + FIELD_DELIM + (
        TransformUtil.ipToInt(intermediateSession.getIp()) == null ? "0"
            : TransformUtil.ipToInt(intermediateSession.getIp()).toString());
    if (agentIpSignature.containsKey(agentIp)) {
      for (Map.Entry<Integer, Long> agentIpBotFlagMap :
          agentIpSignature
              .get(agentIp)
              .entrySet()) {
        intermediateSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
      }
    }

    // guid
    Map<String, Map<Integer, Long>> guidSignature = attributeSignature.get("guid");
    long[] long4Cguid = TransformUtil.md522Long(intermediateSession.getGuid());
    String guid = long4Cguid[0] + FIELD_DELIM + long4Cguid[1];
    if (guidSignature.containsKey(guid)) {
      for (Map.Entry<Integer, Long> guidBotFlagMap :
          guidSignature.get(guid).entrySet()) {
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
  public void processBroadcastElement(BotSignature attributeSignature,
      Context context, Collector<IntermediateSession> out) throws Exception {
    BroadcastState<String, Map<String, Map<Integer, Long>>> attributeBroadcastStatus =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    List<Integer> botFlags = attributeSignature.getBotFlags();
    String signatureId = null;
    Long expirationTime = null;
    Boolean isGeneration = null;
    Map<String, Map<Integer, Long>> signature =
        attributeBroadcastStatus.get(attributeSignature.getType());
    expirationTime = attributeSignature.getExpirationTime();
    isGeneration = attributeSignature.getIsGeneration();
    if ("agent".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getUserAgent().getAgentHash1() + FIELD_DELIM + attributeSignature
              .getUserAgent().getAgentHash2();

    } else if ("agentIp".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getUserAgent().getAgentHash1() + FIELD_DELIM + attributeSignature
              .getUserAgent().getAgentHash2() + FIELD_DELIM + attributeSignature.getIp();

    } else if ("ip".equals(attributeSignature.getType())) {
      signatureId = attributeSignature.getIp().toString();
    } else if ("guid".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getGuid().getGuid1() + FIELD_DELIM + attributeSignature.getGuid()
              .getGuid2();
    }
    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (signature.get(signatureId) != null) {
          if (signature.get(signatureId).containsKey(botFlag)) {
            if (expirationTime > signature.get(signatureId).get(botFlag)) {
              signature.get(signatureId).put(botFlag, expirationTime);
            }
          } else {
            signature.get(signatureId).put(botFlag, expirationTime);
          }
        } else {
          HashMap<Integer, Long> newBotFlagStatus = new HashMap<>();
          newBotFlagStatus.put(botFlag, expirationTime);
          signature.put(signatureId, newBotFlagStatus);

        }
      }
    } else {
      Map<Integer, Long> signatureBotFlagStatus = signature.get(signatureId);
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
