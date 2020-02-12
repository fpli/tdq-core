package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AttributeSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.Set;

@Slf4j
public class AttributeBroadcastProcessFunctionForEvent extends BroadcastProcessFunction<UbiEvent, AttributeSignature, UbiEvent> {
    @Override
    public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

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

        // guid
        if (attributeSignature.contains("guid" + ubiEvent.getGuid())) {
            ubiEvent.getBotFlags().addAll(attributeSignature.get("guid" + ubiEvent.getGuid()));
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
    }

    @Override
    public void processBroadcastElement(AttributeSignature attributeSignature, Context context, Collector<UbiEvent> out) throws Exception {
        BroadcastState<String, Set<Integer>> attributeSignatureState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : attributeSignature.getAttributeSignature().entrySet()) {
            attributeSignatureState.put(entry.getKey(), entry.getValue());
        }
    }
}
