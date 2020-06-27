package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.SojTimestamp;
import com.ebay.sojourner.common.util.TransformUtil;
import com.ebay.sojourner.common.util.UbiSessionHelper;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class CrossSessionDQBroadcastProcessFunction extends
    BroadcastProcessFunction<IntermediateSession, BotSignature, IntermediateSession> {

  @Override
  public void processElement(IntermediateSession intermediateSession,
      ReadOnlyContext context, Collector<IntermediateSession> out) throws Exception {

    ReadOnlyBroadcastState<String, Map<String, Map<Integer, Long[]>>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    // ip
    String ip = TransformUtil.ipToInt(intermediateSession.getIp()) == null ? "0"
        : TransformUtil.ipToInt(intermediateSession.getIp()).toString();
    Map<String, Map<Integer, Long[]>> ipSignature = attributeSignature.get("ip");
    if (ipSignature != null && ipSignature.containsKey(ip)) {
      for (Map.Entry<Integer, Long[]> ipBotFlagMap :
          ipSignature.get(ip).entrySet()) {
        Long[] duration = ipBotFlagMap.getValue();

        if (SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
            > duration[0]
            &&
            SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
                < duration[1]) {
          intermediateSession.getBotFlagList().add(ipBotFlagMap.getKey());
        }
      }
    }

    // agent
    long[] long4AgentHash = TransformUtil
        .md522Long(TransformUtil.getMD5(intermediateSession.getUserAgent()));
    Map<String, Map<Integer, Long[]>> agentSignature = attributeSignature.get("agent");
    String agent = long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1];

    if (agentSignature != null && agentSignature.containsKey(agent)) {
      for (Map.Entry<Integer, Long[]> agentBotFlagMap :
          agentSignature.get(agent).entrySet()) {

        Long[] duration = agentBotFlagMap.getValue();
        if (SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
            > duration[0]
            &&
            SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
                < duration[1]) {
          intermediateSession.getBotFlagList().add(agentBotFlagMap.getKey());
        }
      }
    }

    // agentIp

    Map<String, Map<Integer, Long[]>> agentIpSignature = attributeSignature.get("agentIp");
    String agentIp =
        long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1] + Constants.FIELD_DELIM + (
            TransformUtil.ipToInt(intermediateSession.getIp()) == null ? "0"
                : TransformUtil.ipToInt(intermediateSession.getIp()).toString());
    if (agentIpSignature != null && agentIpSignature.containsKey(agentIp)) {
      for (Map.Entry<Integer, Long[]> agentIpBotFlagMap :
          agentIpSignature.get(agentIp).entrySet()) {

        Long[] duration = agentIpBotFlagMap.getValue();
        if (SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
            > duration[0]
            &&
            SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
                < duration[1]) {
          intermediateSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
        }
      }
    }

    // guid
    //    Map<String, Map<Integer, Long[]>> guidSignature = attributeSignature.get("guid");
    //    long[] long4Cguid = TransformUtil.md522Long(intermediateSession.getGuid());
    //    String guid = long4Cguid[0] + Constants.FIELD_DELIM + long4Cguid[1];
    //    if (guidSignature.containsKey(guid)) {
    //      for (Map.Entry<Integer, Long[]> guidBotFlagMap : guidSignature.get(guid).entrySet()) {
    //        intermediateSession.getBotFlagList().add(guidBotFlagMap.getKey());
    //      }
    //    }

    if ((UbiSessionHelper.isAgentDeclarative(intermediateSession.getUserAgent())
        && intermediateSession.getBotFlagList().contains(223))
        || (intermediateSession.getBotFlagList().contains(220)
        && intermediateSession.getBotFlagList().contains(222))) {
      intermediateSession.getBotFlagList().add(202);
    }

    if ((UbiSessionHelper.isAgentDeclarative(intermediateSession.getUserAgent())
        && intermediateSession.getBotFlagList().contains(223))
        || (intermediateSession.getBotFlagList().contains(220)
        && intermediateSession.getBotFlagList().contains(222))) {
      intermediateSession.getBotFlagList().add(210);
    }

    if (intermediateSession.getBotFlagList().contains(223)
        && intermediateSession.getBotFlagList().contains(224)) {
      intermediateSession.getBotFlagList().add(211);
    }
    out.collect(intermediateSession);

  }

  @Override
  public void processBroadcastElement(BotSignature attributeSignature, Context context,
      Collector<IntermediateSession> out) throws Exception {

    BroadcastState<String, Map<String, Map<Integer, Long[]>>> attributeBroadcastStatus =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    List<Integer> botFlags = attributeSignature.getBotFlags();
    String signatureId = null;
    Long expirationTime = null;
    Boolean isGeneration = null;
    int category = 0;
    Long generationTime = null;
    Map<String, Map<Integer, Long[]>> signature =
        attributeBroadcastStatus.get(attributeSignature.getType());
    expirationTime = attributeSignature.getExpirationTime();
    isGeneration = attributeSignature.getIsGeneration();
    category = attributeSignature.getCategory();
    generationTime = attributeSignature.getGenerationTime();

    if ("agent".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getUserAgent().getAgentHash1() + Constants.FIELD_DELIM
              + attributeSignature
              .getUserAgent().getAgentHash2();

    } else if ("agentIp".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getUserAgent().getAgentHash1() + Constants.FIELD_DELIM
              + attributeSignature
              .getUserAgent().getAgentHash2() + Constants.FIELD_DELIM + attributeSignature.getIp();

    } else if ("ip".equals(attributeSignature.getType())) {
      signatureId = attributeSignature.getIp().toString();
    }

    if (signature == null) {
      signature = new ConcurrentHashMap<>();
    }

    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (signature.get(signatureId) != null) {
          if (signature.get(signatureId).containsKey(botFlag) && category == 2) {
            //            if (expirationTime > signature.get(signatureId).get(botFlag)) {
            //              signature.get(signatureId).put(botFlag, expirationTime);
            //            }
            Long[] durationOld = signature.get(signatureId).get(botFlag);
            durationOld[1] = generationTime;
            signature.get(signatureId).put(botFlag, durationOld);
          } else if (!signature.get(signatureId).containsKey(botFlag) && category == 1) {
            Long[] duration = {generationTime, expirationTime};
            signature.get(signatureId).put(botFlag, duration);
          } else if (signature.get(signatureId).containsKey(botFlag) && category == 1) {
            Long[] durationOld = signature.get(signatureId).get(botFlag);
            if (durationOld[0] > generationTime) {
              durationOld[0] = generationTime;
            }
            if (durationOld[1] < expirationTime) {
              durationOld[1] = expirationTime;
            }
            signature.get(signatureId).put(botFlag, durationOld);
          }
        } else {
          Map<Integer, Long[]> newBotFlagStatus = new ConcurrentHashMap<>();
          Long[] duration = {generationTime, expirationTime};
          newBotFlagStatus.put(botFlag, duration);
          signature.put(signatureId, newBotFlagStatus);
        }
      }
    } else {
      Map<Integer, Long[]> signatureBotFlagStatus = signature.get(signatureId);
      if (signatureBotFlagStatus != null) {
        for (int botFlag : botFlags) {
          if (signatureBotFlagStatus.containsKey(botFlag)) {
            if (expirationTime >= signatureBotFlagStatus.get(botFlag)[1]) {
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