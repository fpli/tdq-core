package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
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
public class AgentIpBroadcastProcessFunction extends BroadcastProcessFunction<UbiEvent, AgentIpSignature,UbiEvent> {

    @Override
    public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> agentIpBroadcastState = context.getBroadcastState(MapStateDesc.agentIpSignatureDesc);
        if (agentIpBroadcastState.contains(ubiEvent.getAgentInfo() + ubiEvent.getClientIP())) {
            ubiEvent.getBotFlags().addAll(agentIpBroadcastState.get(ubiEvent.getAgentInfo() + ubiEvent.getClientIP()));
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
    public void processBroadcastElement(AgentIpSignature agentIpSignature, Context context, Collector<UbiEvent> out) throws Exception {
        BroadcastState<String, Set<Integer>> agentIpBroadcastState = context.getBroadcastState(MapStateDesc.agentIpSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : agentIpSignature.getAgentIpBotSignature().entrySet()) {
            agentIpBroadcastState.put(entry.getKey(), entry.getValue());
        }
    }
}
