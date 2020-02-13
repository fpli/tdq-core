package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AttributeSignature;
import com.ebay.sojourner.ubd.common.model.SignatureDetectable;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.types.Either;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Map;
import java.util.Set;

@Slf4j
public class AttributeBroadcastProcessFunctionForDetectable extends BroadcastProcessFunction<Either<UbiEvent,UbiSession>, AttributeSignature, UbiEvent> {
    private OutputTag outputTag = null;

    public AttributeBroadcastProcessFunctionForDetectable( OutputTag sessionOutputTag ) {
        outputTag=sessionOutputTag;
    }

    @Override
    public void processElement( Either<UbiEvent,UbiSession> signatureDetectable, ReadOnlyContext context, Collector<UbiEvent> out ) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

        if (signatureDetectable.isLeft()) {
            UbiEvent ubiEvent = signatureDetectable.left();
            // ip
            if (attributeSignature.contains("ip" + ubiEvent.getClientIP())) {
                ubiEvent.getBotFlags().addAll(attributeSignature.get("ip" + ubiEvent.getClientIP()));
            }

            // agent
            if (attributeSignature.contains("agent" + ubiEvent.getAgentInfo())) {
                ubiEvent.getBotFlags().addAll(attributeSignature.get("agent" + ubiEvent.getAgentInfo()));
            }

            // agentIp
            if (attributeSignature.contains("agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP())) {
                ubiEvent.getBotFlags().addAll(attributeSignature.get("agentIp" + ubiEvent.getAgentInfo() + ubiEvent.getClientIP()));
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
            UbiSession ubiSession =  signatureDetectable.right();
            // ip
            if (attributeSignature.contains("ip" + ubiSession.getClientIp())) {
                ubiSession.getBotFlagList().addAll(attributeSignature.get("ip" + ubiSession.getClientIp()));
            }

            // agent
            if (attributeSignature.contains("agent" + ubiSession.getUserAgent())) {
                ubiSession.getBotFlagList().addAll(attributeSignature.get("agent" + ubiSession.getUserAgent()));
            }

            // agentIp
            if (attributeSignature.contains("agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp())) {
                ubiSession.getBotFlagList().addAll(attributeSignature.get("agentIp" + ubiSession.getUserAgent() + ubiSession.getClientIp()));
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
            context.output(outputTag,ubiSession);
//            out.collect(outputTag,ubiSession);
        }

    }

    @Override
    public void processBroadcastElement( AttributeSignature attributeSignature, Context context, Collector<UbiEvent> out ) throws Exception {
        BroadcastState<String, Set<Integer>> attributeSignatureState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : attributeSignature.getAttributeSignature().entrySet()) {
            attributeSignatureState.put(entry.getKey(), entry.getValue());
        }
    }
}
