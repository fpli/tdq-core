package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class AgentIpBroadcastProcessFunction extends BroadcastProcessFunction<UbiEvent, AgentIpSignature,UbiEvent> {
    private static volatile Logger logger = Logger.getLogger(AgentIpBroadcastProcessFunction.class);

    @Override
    public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> agentIpBroadcastState = context.getBroadcastState(MapStateDesc.agentIpSignatureDesc);
        if(agentIpBroadcastState.contains(ubiEvent.getAgentInfo()+ubiEvent.getClientIP())){
            ubiEvent.getBotFlags().addAll(agentIpBroadcastState.get(ubiEvent.getAgentInfo()+ubiEvent.getClientIP()));
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
