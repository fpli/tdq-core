package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.common.util.TransformUtil;
import com.ebay.sojourner.common.util.UbiSessionHelper;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.types.Either;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

@Slf4j
public class AttributeBroadcastProcessFunctionForDetectable extends
    BroadcastProcessFunction<Either<UbiEvent, UbiSession>, BotSignature, UbiEvent> {

  private OutputTag outputTag = null;
  // private Counter guidCounter;
  private Counter ipCounter;
  private Counter agentCounter;
  private Counter agentIpCounter;
  // private Counter guidIncCounter;
  // private Counter guidDecCounter;
  private Counter ipIncCounter;
  private Counter ipDecCounter;
  private Counter agentIncCounter;
  private Counter agentDecCounter;
  private Counter agentIpIncCounter;
  private Counter agentIpDecCounter;

  public AttributeBroadcastProcessFunctionForDetectable(OutputTag sessionOutputTag) {
    outputTag = sessionOutputTag;
  }

  @Override
  public void processElement(Either<UbiEvent, UbiSession> signatureDetectable,
      ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {

    ReadOnlyBroadcastState<String, Map<String, Map<Integer, Long>>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    if (signatureDetectable.isLeft()) {
      UbiEvent ubiEvent = signatureDetectable.left();

      // ip
      String ip = TransformUtil.ipToInt(ubiEvent.getClientIP()) == null ? "0"
          : TransformUtil.ipToInt(ubiEvent.getClientIP()).toString();
      Map<String, Map<Integer, Long>> ipSignature = attributeSignature.get("ip");
      if (ipSignature != null && ipSignature.size() > 0 && ipSignature.containsKey(ip)) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            ipSignature.get(ip).entrySet()) {
          ubiEvent.getBotFlags().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      long[] long4AgentHash = TransformUtil
          .md522Long(com.ebay.sojourner.common.util.TransformUtil.getMD5(ubiEvent.getAgentInfo()));
      Map<String, Map<Integer, Long>> agentSignature = attributeSignature.get("agent");
      String agent = long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1];
      if (agentSignature != null && agentSignature.size() > 0
          && agentSignature.containsKey(agent)) {
        for (Map.Entry<Integer, Long> agentBotFlagMap :
            agentSignature.get(agent).entrySet()) {
          ubiEvent.getBotFlags().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      String agentIp =
          long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1] + Constants.FIELD_DELIM + (
              TransformUtil.ipToInt(ubiEvent.getClientIP()) == null ? "0"
                  : TransformUtil.ipToInt(ubiEvent.getClientIP()).toString());
      Map<String, Map<Integer, Long>> agentIpSignature = attributeSignature.get("agentIp");
      if (agentIpSignature != null && agentIpSignature.size() > 0
          && agentIpSignature.containsKey(agentIp)) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap :
            agentIpSignature.get(agentIp).entrySet()) {
          ubiEvent.getBotFlags().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      /*
      long[] long4Cguid = TransformUtil.md522Long(ubiEvent.getGuid());
      String guid = long4Cguid[0] + Constants.FIELD_DELIM + long4Cguid[1];
      Map<String, Map<Integer, Long>> guidSignature = attributeSignature.get("guid");
      if (guidSignature != null && guidSignature.size() > 0
          && guidSignature.containsKey(guid)) {
        for (Map.Entry<Integer, Long> guidBotFlagMap :
            guidSignature.get(guid).entrySet()) {
          ubiEvent.getBotFlags().add(guidBotFlagMap.getKey());
        }
      }
      */

      if ((UbiSessionHelper.isAgentDeclarative(ubiEvent.getAgentInfo()) && ubiEvent.getBotFlags()
          .contains(223))
          || (ubiEvent.getBotFlags().contains(220) && ubiEvent.getBotFlags().contains(222))) {
        ubiEvent.getBotFlags().add(202);
      }

      if ((UbiSessionHelper.isAgentDeclarative(ubiEvent.getAgentInfo()) && ubiEvent.getBotFlags()
          .contains(223))
          || (ubiEvent.getBotFlags().contains(220) && ubiEvent.getBotFlags().contains(222))) {
        ubiEvent.getBotFlags().add(210);
      }

      if (ubiEvent.getBotFlags().contains(223) && ubiEvent.getBotFlags().contains(224)) {
        ubiEvent.getBotFlags().add(211);
      }
      out.collect(ubiEvent);

    } else {

      UbiSession ubiSession = signatureDetectable.right();

      // ip
      String ip = TransformUtil.ipToInt(ubiSession.getIp()) == null ? "0"
          : TransformUtil.ipToInt(ubiSession.getIp()).toString();
      Map<String, Map<Integer, Long>> ipSignature = attributeSignature.get("ip");
      if (ipSignature != null && ipSignature.size() > 0 && ipSignature.containsKey(ip)) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            ipSignature.get(ip).entrySet()) {
          ubiSession.getBotFlagList().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      long[] long4AgentHash = TransformUtil
          .md522Long(
              com.ebay.sojourner.common.util.TransformUtil.getMD5(ubiSession.getAgentInfo()));
      Map<String, Map<Integer, Long>> agentSignature = attributeSignature.get("agent");
      String agent = long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1];
      if (agentSignature != null && agentSignature.size() > 0
          && agentSignature.containsKey(agent)) {
        for (Map.Entry<Integer, Long> agentBotFlagMap :
            agentSignature.get(agent).entrySet()) {
          ubiSession.getBotFlagList().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      String agentIp =
          long4AgentHash[0] + Constants.FIELD_DELIM + long4AgentHash[1] + Constants.FIELD_DELIM + (
              TransformUtil.ipToInt(ubiSession.getIp()) == null ? "0"
                  : TransformUtil.ipToInt(ubiSession.getIp()).toString());
      Map<String, Map<Integer, Long>> agentIpSignature = attributeSignature.get("agentIp");
      if (agentIpSignature != null && agentIpSignature.size() > 0
          && agentIpSignature.containsKey(agentIp)) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap :
            agentIpSignature.get(agentIp).entrySet()) {
          ubiSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      /*
      long[] long4Cguid = TransformUtil.md522Long(ubiSession.getGuid());
      String guid = long4Cguid[0] + Constants.FIELD_DELIM + long4Cguid[1];
      Map<String, Map<Integer, Long>> guidSignature = attributeSignature.get("guid");
      if (guidSignature != null && guidSignature.size() > 0
          && guidSignature.containsKey(guid)) {
        for (Map.Entry<Integer, Long> guidBotFlagMap :
            guidSignature.get(guid).entrySet()) {
          ubiSession.getBotFlagList().add(guidBotFlagMap.getKey());
        }
      }
      */

      if ((UbiSessionHelper.isAgentDeclarative(ubiSession.getUserAgent())
          && ubiSession.getBotFlagList().contains(223))
          || (ubiSession.getBotFlagList().contains(220)
          && ubiSession.getBotFlagList().contains(222))) {
        ubiSession.getBotFlagList().add(202);
      }

      if ((UbiSessionHelper.isAgentDeclarative(ubiSession.getUserAgent())
          && ubiSession.getBotFlagList().contains(223))
          || (ubiSession.getBotFlagList().contains(220)
          && ubiSession.getBotFlagList().contains(222))) {
        ubiSession.getBotFlagList().add(210);
      }

      if (ubiSession.getBotFlagList().contains(223) && ubiSession.getBotFlagList().contains(224)) {
        ubiSession.getBotFlagList().add(211);
      }
      context.output(outputTag, ubiSession);

    }
  }

  @Override
  public void processBroadcastElement(BotSignature attributeSignature, Context context,
      Collector<UbiEvent> out) throws Exception {

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
    /*
    else if ("guid".equals(attributeSignature.getType())) {
      signatureId =
          attributeSignature.getGuid().getGuid1() + Constants.FIELD_DELIM + attributeSignature
              .getGuid()
              .getGuid2();
    }
    */

    if (signature != null && signature.size() > 0) {
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
            if (signatureId.contains("ip")) {
              ipIncCounter.inc();
              ipCounter.inc();
            } else if (signatureId.contains("agent")) {
              agentIncCounter.inc();
              agentCounter.inc();
            } else if (signatureId.contains("agentIp")) {
              agentIpIncCounter.inc();
              agentIpCounter.inc();
            }
            /*else if (signatureId.contains("guid")) {
              guidIncCounter.inc();
              guidCounter.inc();
            }
            */
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
                  if (signatureId.contains("ip")) {
                    ipDecCounter.inc();
                    ipCounter.dec();
                  } else if (signatureId.contains("agent")) {
                    agentDecCounter.inc();
                    agentCounter.dec();
                  } else if (signatureId.contains("agentIp")) {
                    agentIpDecCounter.inc();
                    agentIpCounter.dec();
                  }
                  /*
                  else if (signatureId.contains("guid")) {
                    guidDecCounter.inc();
                    guidCounter.dec();
                  }
                  */
                  attributeBroadcastStatus.remove(signatureId);
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    /*
    guidCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("guid signature count");
            */
    ipCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("ip signature count");
    agentCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agent signature count");
    agentIpCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agentIp signature count");
    /*
    guidIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("guid signature inc count");
    guidDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("guid signature dec count");
            */
    ipIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("ip signature inc count");
    ipDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("ip signature dec count");
    agentIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agent signature inc count");
    agentDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agent signature dec count");
    agentIpIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agentIp signature inc count");
    agentIpDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("agentIp signature dec count");
  }
}