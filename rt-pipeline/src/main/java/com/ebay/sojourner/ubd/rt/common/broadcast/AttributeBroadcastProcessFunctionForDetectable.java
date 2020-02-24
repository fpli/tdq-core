package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.types.Either;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AttributeBroadcastProcessFunctionForDetectable extends BroadcastProcessFunction<Either<UbiEvent, UbiSession>, Tuple3<String, Set<Integer>, Long>, UbiEvent> {
    private OutputTag outputTag = null;

    public  AttributeBroadcastProcessFunctionForDetectable(OutputTag sessionOutputTag) {
        outputTag = sessionOutputTag;
    }

    @Override
    public void processElement(Either<UbiEvent, UbiSession> signatureDetectable, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, HashMap<Integer, Long>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

        if (signatureDetectable.isLeft()) {
            UbiEvent ubiEvent = signatureDetectable.left();
            // ip
            if (attributeSignature.contains("ip" + ubiEvent.getClientIP())) {
                for (Map.Entry<Integer, Long> ipBotFlagMap : attributeSignature.get("ip" + ubiEvent.getClientIP()).entrySet()) {
                    ubiEvent.getBotFlags().add(ipBotFlagMap.getKey());
                }
            }

            // agent
            if (attributeSignature.contains("agent" + ubiEvent.getAgentInfo())) {
                for (Map.Entry<Integer, Long> agentBotFlagMap : attributeSignature.get("agent" + ubiEvent.getAgentInfo()).entrySet()) {
                    ubiEvent.getBotFlags().add(agentBotFlagMap.getKey());
                }
            }

            // agentIp
            if (attributeSignature.contains("agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP())) {
                for (Map.Entry<Integer, Long> agentIpBotFlagMap : attributeSignature.get("agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP()).entrySet()) {
                    ubiEvent.getBotFlags().add(agentIpBotFlagMap.getKey());
                }
            }

            // guid
            if (attributeSignature.contains("guid" + ubiEvent.getGuid())) {
                for (Map.Entry<Integer, Long> guidIpBotFlagMap : attributeSignature.get("guid" + ubiEvent.getGuid()).entrySet()) {
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

            if (ubiEvent.getBotFlags().contains(223)) {
                ubiEvent.getBotFlags().add(211);
            }
            out.collect(ubiEvent);
        } else {
            UbiSession ubiSession = signatureDetectable.right();
            // ip
            if (attributeSignature.contains("ip" + ubiSession.getClientIp())) {
                for (Map.Entry<Integer, Long> ipBotFlagMap : attributeSignature.get("ip" + ubiSession.getClientIp()).entrySet()) {
                    ubiSession.getBotFlagList().add(ipBotFlagMap.getKey());
                }
            }

            // agent
            if (attributeSignature.contains("agent" + ubiSession.getUserAgent())) {
                for (Map.Entry<Integer, Long> agentBotFlagMap : attributeSignature.get("agent" + ubiSession.getUserAgent()).entrySet()) {
                    ubiSession.getBotFlagList().add(agentBotFlagMap.getKey());
                }
            }

            // agentIp
            if (attributeSignature.contains("agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp())) {
                for (Map.Entry<Integer, Long> agentIpBotFlagMap : attributeSignature.get("agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp()).entrySet()) {
                    ubiSession.getBotFlagList().add(agentIpBotFlagMap.getKey());
                }
            }

            // guid
            if (attributeSignature.contains("guid" + ubiSession.getGuid())) {
                for (Map.Entry<Integer, Long> guidBotFlagMap : attributeSignature.get("guid" + ubiSession.getGuid()).entrySet()) {
                    ubiSession.getBotFlagList().add(guidBotFlagMap.getKey());
                }
            }

            if ((ubiSession.getBotFlagList().contains(221) && ubiSession.getBotFlagList().contains(223))
                    || (ubiSession.getBotFlagList().contains(220) && ubiSession.getBotFlagList().contains(222))) {
                ubiSession.getBotFlagList().add(202);
            }

            if ((ubiSession.getBotFlagList().contains(221) && ubiSession.getBotFlagList().contains(223))
                    || (ubiSession.getBotFlagList().contains(220) && ubiSession.getBotFlagList().contains(222))) {
                ubiSession.getBotFlagList().add(210);
            }

            if (ubiSession.getBotFlagList().contains(223)) {
                ubiSession.getBotFlagList().add(211);
            }
            context.output(outputTag, ubiSession);
        }
    }

    @Override
    public void processBroadcastElement(Tuple3<String, Set<Integer>, Long> attributeSignature, Context context, Collector<UbiEvent> out) throws Exception {
        BroadcastState<String, HashMap<Integer, Long>> attributeBroadcastStatus = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

        if (!attributeBroadcastStatus.contains(attributeSignature.f0)) {
            Iterator<Integer> botFlags = attributeSignature.f1.iterator();
            while (botFlags.hasNext()) {
                HashMap<Integer, Long> botFlagStatus = new HashMap<>();
                botFlagStatus.put(botFlags.next(), attributeSignature.f2);
                attributeBroadcastStatus.put(attributeSignature.f0, botFlagStatus);
            }
        } else {
            HashMap<Integer, Long> botFlagStatusMap = attributeBroadcastStatus.get(attributeSignature.f0);
            for (Map.Entry<Integer, Long> botFlagStatus : botFlagStatusMap.entrySet()) {
                if (!attributeSignature.f1.contains(botFlagStatus.getKey())) {
                    HashMap<Integer, Long> botFlag = new HashMap<>();
                    botFlag.put(botFlagStatus.getKey(), attributeSignature.f2);
                    attributeBroadcastStatus.put(attributeSignature.f0, botFlag);
                } else {
                    if (attributeSignature.f1 == null) {
                        if (attributeSignature.f2 <= botFlagStatus.getValue()) {
                            botFlagStatusMap.remove(botFlagStatus.getKey());
                            if (botFlagStatusMap.size() > 0) {
                                attributeBroadcastStatus.put(attributeSignature.f0,botFlagStatusMap);
                            } else {
                                attributeBroadcastStatus.remove(attributeSignature.f0);
                            }

                        }
                    } else {
                        HashMap<Integer, Long> botFlag = new HashMap<>();
                        botFlag.put(botFlagStatus.getKey(), attributeSignature.f2);
                        attributeBroadcastStatus.put(attributeSignature.f0, botFlag);
                    }
                }
            }
        }
    }
}
