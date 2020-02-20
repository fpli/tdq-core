package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AttributeSignature;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.Set;

@Slf4j
public class AgentBroadcastProcessFunction extends BroadcastProcessFunction<UbiSession, AttributeSignature,UbiSession> {

    @Override
    public void processElement(UbiSession ubiSession, ReadOnlyContext context, Collector<UbiSession> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        if (attributeSignature.contains("agent" + ubiSession.getUserAgent())) {
            ubiSession.getBotFlagList().addAll(attributeSignature.get("agent" + ubiSession.getUserAgent()));
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
        BroadcastState<String, Set<Integer>> agentBroadcastState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : attributeSignature.getAttributeSignature().entrySet()) {
            agentBroadcastState.put(entry.getKey(), entry.getValue());
        }
    }
}
