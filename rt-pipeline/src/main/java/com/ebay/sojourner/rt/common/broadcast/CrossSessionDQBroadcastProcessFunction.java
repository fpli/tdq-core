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
    // System.out.println("intermediateSession ip is:" + ip);
    /*
     System.out.println(
        "intermediateSession absStartTimestamp is:" + SojTimestamp
            .getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp()));
            */
    Map<String, Map<Integer, Long[]>> ipSignature = attributeSignature.get("ip");
    if (ipSignature != null && ipSignature.size() > 0 && ipSignature.containsKey(ip)) {
      // System.out.println("join success");
      // System.out.println("ipSignature keys is:" + ipSignature.keySet().toString());
      for (Map.Entry<Integer, Long[]> ipBotFlagMap :
          ipSignature.get(ip).entrySet()) {
        Long[] duration = ipBotFlagMap.getValue();
        /*
        System.out
            .println("ip duration0 is:" + duration[0] + "," + "duration1 is:" + duration[1]);
            */
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
    /*
    System.out.println("intermediateSession agent0 is:" + long4AgentHash[0] + "," + "agent1 is:"
        + long4AgentHash[1]);
        */
    Map<String, Map<Integer, Long[]>> agentSignature = attributeSignature.get("agent");
    String agent = long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1];
    if (agentSignature != null && agentSignature.size() > 0
        && agentSignature.containsKey(agent)) {
      // System.out.println("join success");
       System.out.println("agentSignature keys is:" + agentSignature.keySet().toString());
      for (Map.Entry<Integer, Long[]> agentBotFlagMap :
          agentSignature.get(agent).entrySet()) {
        Long[] duration = agentBotFlagMap.getValue();
        /*
        System.out
            .println("agent duration0 is:" + duration[0] + "," + "duration1 is:" + duration[1]);
            */
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
    String agentIp =
        long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1] + Constants.FIELD_DELIM + (
            TransformUtil.ipToInt(intermediateSession.getIp()) == null ? "0"
                : TransformUtil.ipToInt(intermediateSession.getIp()).toString());
    // System.out.println("intermediateSession agentIp is:" + agentIp);
    Map<String, Map<Integer, Long[]>> agentIpSignature = attributeSignature.get("agentIp");
    if (agentIpSignature != null && agentIpSignature.size() > 0
        && agentIpSignature.containsKey(agentIp)) {
      // System.out.println("join success");
      // System.out.println("agentIpSignature keys is:" + agentIpSignature.keySet().toString());
      for (Map.Entry<Integer, Long[]> agentIpBotFlagMap :
          agentIpSignature.get(agentIp).entrySet()) {
        Long[] duration = agentIpBotFlagMap.getValue();
        /*
        System.out
            .println("agent ip duration0 is:" + duration[0] + "," + "duration1 is:" + duration[1]);
            */
        if (SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
            > duration[0]
            &&
            SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp())
                < duration[1]) {
          intermediateSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
        }
      }
    }

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
      attributeBroadcastStatus.put(attributeSignature.getType(), signature);
    }

    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (signature.get(signatureId) != null) {
          if (signature.get(signatureId).containsKey(botFlag) && category == 2) {
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