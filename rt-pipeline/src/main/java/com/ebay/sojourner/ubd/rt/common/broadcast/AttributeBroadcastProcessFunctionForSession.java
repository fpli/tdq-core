package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AttributeSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.Set;

public class AttributeBroadcastProcessFunctionForSession extends BroadcastProcessFunction<UbiSession, AttributeSignature, UbiSession> {
    @Override
    public void processElement(UbiSession ubiSession, ReadOnlyContext context, Collector<UbiSession> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

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
        out.collect(ubiSession);
    }

    @Override
    public void processBroadcastElement(AttributeSignature attributeSignature, Context context, Collector<UbiSession> out) throws Exception {
        BroadcastState<String, Set<Integer>> attributeSignatureState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : attributeSignature.getAttributeSignature().entrySet()) {
            attributeSignatureState.put(entry.getKey(), entry.getValue());
        }
    }
}
