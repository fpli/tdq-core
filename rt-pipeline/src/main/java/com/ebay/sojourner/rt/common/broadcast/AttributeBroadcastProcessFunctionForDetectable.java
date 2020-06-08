package com.ebay.sojourner.rt.common.broadcast;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.TransformUtil;
import com.ebay.sojourner.flink.common.state.MapStateDesc;
import com.ebay.sojourner.flink.common.util.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.types.Either;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

@Slf4j
public class AttributeBroadcastProcessFunctionForDetectable
    extends BroadcastProcessFunction<
    Either<UbiEvent, UbiSession>, Tuple5<String, String, Boolean, Set<Integer>, Long>, UbiEvent> {

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
      Integer ipEnhance = TransformUtil.ipToInt(ubiEvent.getClientIP());
      if (attributeSignature
          .contains("ip" + ipEnhance + Constants.SEPARATOR)) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            attributeSignature.get("ip" + ipEnhance + Constants.SEPARATOR).entrySet()) {
          ubiEvent.getBotFlags().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      long[] agentEnhance = TransformUtil.md522Long(ubiEvent.getAgentInfo());
      if (attributeSignature
          .contains("agent" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1])) {
        for (Map.Entry<Integer, Long> agentBotFlagMap : attributeSignature
            .get("agent" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1]).entrySet()) {
          ubiEvent.getBotFlags().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      if (attributeSignature.contains(
          "agentIp" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1] + Constants.SEPARATOR
              + ipEnhance)) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap :
            attributeSignature
                .get("agentIp" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1]
                    + Constants.SEPARATOR + ipEnhance)
                .entrySet()) {
          ubiEvent.getBotFlags().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      long[] guidEnhance = TransformUtil.md522Long(ubiEvent.getGuid());
      if (attributeSignature
          .contains("guid" + guidEnhance[0] + Constants.SEPARATOR + guidEnhance[1])) {
        for (Map.Entry<Integer, Long> guidIpBotFlagMap :
            attributeSignature.get("guid" + guidEnhance[0] + Constants.SEPARATOR + guidEnhance[1])
                .entrySet()) {
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

      if (ubiEvent.getBotFlags().contains(223) && ubiEvent.getBotFlags().contains(224)) {
        ubiEvent.getBotFlags().add(211);
      }
      out.collect(ubiEvent);
    } else {
      UbiSession ubiSession = signatureDetectable.right();
      // ip
      Integer ipEnhance = TransformUtil.ipToInt(ubiSession.getClientIp());
      if (attributeSignature.contains("ip" + ipEnhance + Constants.SEPARATOR)) {
        for (Map.Entry<Integer, Long> ipBotFlagMap :
            attributeSignature.get("ip" + ipEnhance + Constants.SEPARATOR).entrySet()) {
          ubiSession.getBotFlagList().add(ipBotFlagMap.getKey());
        }
      }

      // agent
      long[] agentEnhance = TransformUtil.md522Long(ubiSession.getUserAgent());
      if (attributeSignature
          .contains("agent" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1])) {
        for (Map.Entry<Integer, Long> agentBotFlagMap : attributeSignature
            .get("agent" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1]).entrySet()) {
          ubiSession.getBotFlagList().add(agentBotFlagMap.getKey());
        }
      }

      // agentIp
      if (attributeSignature.contains(
          "agentIp" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1] + Constants.SEPARATOR
              + ipEnhance)) {
        for (Map.Entry<Integer, Long> agentIpBotFlagMap : attributeSignature
            .get("agentIp" + agentEnhance[0] + Constants.SEPARATOR + agentEnhance[1]
                + Constants.SEPARATOR + ipEnhance)
            .entrySet()) {
          ubiSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
        }
      }

      // guid
      long[] guidEnhance = TransformUtil.md522Long(ubiSession.getGuid());
      if (attributeSignature
          .contains("guid" + guidEnhance[0] + Constants.SEPARATOR + guidEnhance[1])) {
        for (Map.Entry<Integer, Long> guidBotFlagMap :
            attributeSignature.get("guid" + guidEnhance[0] + Constants.SEPARATOR + guidEnhance[1])
                .entrySet()) {
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

      if (ubiSession.getBotFlagList().contains(223) && ubiSession.getBotFlagList().contains(224)) {
        ubiSession.getBotFlagList().add(211);
      }
      context.output(outputTag, ubiSession);
    }
  }

  @Override
  public void processBroadcastElement(
      Tuple5<String, String, Boolean, Set<Integer>, Long> signatures,
      Context context,
      Collector<UbiEvent> out)
      throws Exception {

    BroadcastState<String, Map<Integer, Long>> signatureState =
        context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

    Set<Integer> botFlags = signatures.f3;
    String signatureId = signatures.f0;
    String signatureValue = signatures.f1;
    Long expirationTime = signatures.f4;
    Boolean isGeneration = signatures.f2;

    if (isGeneration) {
      for (int botFlag : botFlags) {
        if (signatureState.get(signatureId + signatureValue) != null) {
          if (signatureState.get(signatureId + signatureValue).containsKey(botFlag)) {
            if (expirationTime > signatureState
                .get(signatureId + signatureValue).get(botFlag)) {
              signatureState.get(signatureId + signatureValue)
                  .put(botFlag, expirationTime);
            }
          } else {
            signatureState.get(signatureId + signatureValue).put(botFlag, expirationTime);
          }
        } else {
          HashMap<Integer, Long> newBotFlagState = new HashMap<>();
          newBotFlagState.put(botFlag, expirationTime);
          signatureState.put(signatureId + signatureValue, newBotFlagState);
          if (signatureId.equals("ip")) {
            ipIncCounter.inc();
            ipCounter.inc();
          } else if (signatureId.equals("agent")) {
            agentIncCounter.inc();
            agentCounter.inc();
          } else if (signatureId.equals("agentIp")) {
            agentIpIncCounter.inc();
            agentIpCounter.inc();
          } else if (signatureId.equals("guid")) {
            guidIncCounter.inc();
            guidCounter.inc();
          }
        }
      }
    } else {
      Map<Integer, Long> botFlagState = signatureState
          .get(signatureId + signatureValue);
      if (botFlagState != null) {
        for (int botFlag : botFlags) {
          if (botFlagState.containsKey(botFlag)) {
            if (expirationTime > botFlagState.get(botFlag)) {
              botFlagState.remove(botFlag);
              if (botFlagState.size() == 0) {
                if (signatureId.equals("ip")) {
                  ipDecCounter.inc();
                  ipCounter.dec();
                } else if (signatureId.equals("agent")) {
                  agentDecCounter.inc();
                  agentCounter.dec();
                } else if (signatureId.equals("agentIp")) {
                  agentIpDecCounter.inc();
                  agentIpCounter.dec();
                } else if (signatureId.equals("guid")) {
                  guidDecCounter.inc();
                  guidCounter.dec();
                }
                botFlagState.remove(signatureId + signatureValue);
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
            .addGroup(Constants.SOJ_METRICS_GROUP)
            .counter("guid signature count");
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
