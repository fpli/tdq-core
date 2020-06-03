package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import com.ebay.sojourner.rt.util.TransformUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.types.Either;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

@Slf4j
public class AttributeBroadcastProcessFunctionForDetectable
    extends BroadcastProcessFunction<
    Either<UbiEvent, UbiSession>, Tuple4<String, Boolean, Set<Integer>, Long>, UbiEvent> {

  private OutputTag outputTag = null;
  private Counter guidCounter;
  private Counter ipCounter;
  private Counter agentCounter;
  private Counter agentIpCounter;
  private Counter guidIncCounter;
  private Counter guidDecCounter;
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
  public void processElement(
      Either<UbiEvent, UbiSession> signatureDetectable,
      ReadOnlyContext context,
      Collector<UbiEvent> out)
      throws Exception {
    ReadOnlyBroadcastState<String, Map<Integer, Long>> attributeSignature =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    if (signatureDetectable.isLeft()) {
      UbiEvent ubiEvent = signatureDetectable.left();
      // ip
      if (attributeSignature.contains("ip" + ubiEvent.getClientIP())) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            attributeSignature.get("ip" + ubiEvent.getClientIP()).entrySet()) {
          ubiEvent.getBotFlags().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      if (attributeSignature.contains("agent" + ubiEvent.getAgentInfo())) {
        for (Map.Entry<Integer, Long> agentBotFlagMap :
            attributeSignature.get("agent" + ubiEvent.getAgentInfo()).entrySet()) {
          ubiEvent.getBotFlags().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      if (attributeSignature.contains(
          "agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP())) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap :
            attributeSignature
                .get("agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP())
                .entrySet()) {
          ubiEvent.getBotFlags().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      Long[] guidEnhance = TransformUtil.stringToLong(ubiEvent.getGuid());
      if (attributeSignature.contains("guid" + guidEnhance[0] + guidEnhance[1])) {
        for (Map.Entry<Integer, Long> guidIpBotFlagMap :
            attributeSignature.get("guid" + guidEnhance[0] + guidEnhance[1]).entrySet()) {
          ubiEvent.getBotFlags().add(guidIpBotFlagMap.getKey());
        }
      }

      if ((ubiEvent.getBotFlags().contains(221) && ubiEvent.getBotFlags().contains(223))
          || (ubiEvent.getBotFlags().contains(220) && ubiEvent.getBotFlags().contains(222))) {
        ubiEvent.getBotFlags().add(202);
      }

      if ((ubiEvent.getBotFlags().contains(221) && ubiEvent.getBotFlags().contains(223))
          || (ubiEvent.getBotFlags().contains(220) && ubiEvent.getBotFlags().contains(222))) {
        ubiEvent.getBotFlags().add(210);
      }

      if (ubiEvent.getBotFlags().contains(223)&&ubiEvent.getBotFlags().contains(224)) {
        ubiEvent.getBotFlags().add(211);
      }
      out.collect(ubiEvent);
    } else {
      UbiSession ubiSession = signatureDetectable.right();
      // ip
      if (attributeSignature.contains("ip" + ubiSession.getClientIp())) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            attributeSignature.get("ip" + ubiSession.getClientIp()).entrySet()) {
          ubiSession.getBotFlagList().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      if (attributeSignature.contains("agent" + ubiSession.getUserAgent())) {
        for (Map.Entry<Integer, Long> agentBotFlagMap :
            attributeSignature.get("agent" + ubiSession.getUserAgent()).entrySet()) {
          ubiSession.getBotFlagList().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      if (attributeSignature.contains(
          "agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp())) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap :
            attributeSignature
                .get("agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp())
                .entrySet()) {
          ubiSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      Long[] guidEnhance = TransformUtil.stringToLong(ubiSession.getGuid());
      if (attributeSignature.contains("guid" + guidEnhance[0] + guidEnhance[1])) {
        for (Map.Entry<Integer, Long> guidBotFlagMap :
            attributeSignature.get("guid" + guidEnhance[0] + guidEnhance[1]).entrySet()) {
          ubiSession.getBotFlagList().add(guidBotFlagMap.getKey());
        }
      }

      if ((ubiSession.getBotFlagList().contains(221) && ubiSession.getBotFlagList().contains(223))
          || (ubiSession.getBotFlagList().contains(220)
          && ubiSession.getBotFlagList().contains(222))) {
        ubiSession.getBotFlagList().add(202);
      }

      if ((ubiSession.getBotFlagList().contains(221) && ubiSession.getBotFlagList().contains(223))
          || (ubiSession.getBotFlagList().contains(220)
          && ubiSession.getBotFlagList().contains(222))) {
        ubiSession.getBotFlagList().add(210);
      }

      if (ubiSession.getBotFlagList().contains(223)&&ubiSession.getBotFlagList().contains(224)) {
        ubiSession.getBotFlagList().add(211);
      }
      context.output(outputTag, ubiSession);
    }
  }

  @Override
  public void processBroadcastElement(
      Tuple4<String, Boolean, Set<Integer>, Long> attributeSignature,
      Context context,
      Collector<UbiEvent> out)
      throws Exception {
    BroadcastState<String, Map<Integer, Long>> attributeBroadcastStatus =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
    Set<Integer> botFlags = attributeSignature.f2;
    String signatureId = attributeSignature.f0;
    Long expirationTime = attributeSignature.f3;
    Boolean isGeneration = attributeSignature.f1;

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
          if (signatureId.contains("ip")) {
            ipIncCounter.inc();
            ipCounter.inc();
          } else if (signatureId.contains("agent")) {
            agentIncCounter.inc();
            agentCounter.inc();
          } else if (signatureId.contains("agentIp")) {
            agentIpIncCounter.inc();
            agentIpCounter.inc();
          } else if (signatureId.contains("guid")) {
            guidIncCounter.inc();
            guidCounter.inc();
          }
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
                if (signatureId.contains("ip")) {
                  ipDecCounter.inc();
                  ipCounter.dec();
                } else if (signatureId.contains("agent")) {
                  agentDecCounter.inc();
                  agentCounter.dec();
                } else if (signatureId.contains("agentIp")) {
                  agentIpDecCounter.inc();
                  agentIpCounter.dec();
                } else if (signatureId.contains("guid")) {
                  guidDecCounter.inc();
                  guidCounter.dec();
                }
                attributeBroadcastStatus.remove(signatureId);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    guidCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("guid signature count");
    ipCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("ip signature count");
    agentCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agent signature count");
    agentIpCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agentIp signature count");
    guidIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("guid signature inc count");
    guidDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("guid signature dec count");
    ipIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("ip signature inc count");
    ipDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("ip signature dec count");
    agentIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agent signature inc count");
    agentDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agent signature dec count");
    agentIpIncCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agentIp signature inc count");
    agentIpDecCounter =
        getRuntimeContext()
            .getMetricGroup()
            .addGroup("sojourner-ubd")
            .counter("agentIp signature dec count");
  }
}
