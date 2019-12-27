package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.AgentIpSignature;
import com.ebay.sojourner.ubd.common.model.AgentSignature;
import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

@Slf4j
public class AttributeBroadcastProcessFunction extends BroadcastProcessFunction<UbiEvent, Map<String, Object>, UbiEvent> {
    @Override
    public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, Object> attributeBroadcastState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        AgentIpSignature agentAndIpSignature = (AgentIpSignature) attributeBroadcastState.get("agentAndIp");
        AgentSignature agentSignature = (AgentSignature) attributeBroadcastState.get("agentSignature");
        IpSignature ipSignature = (IpSignature) attributeBroadcastState.get("ipSignature");

        if (ipSignature != null && agentSignature != null) {
            if ((ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()).contains(223) && agentSignature.getAgentBotSignature().get(ubiEvent.getAgentInfo()).contains(221))
                    || (ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()).contains(222) && agentSignature.getAgentBotSignature().get(ubiEvent.getAgentInfo()).contains(220))) {
                ubiEvent.getBotFlags().add(202);
            }

            if ((agentSignature.getAgentBotSignature().get(ubiEvent.getAgentInfo()).contains(220) && agentSignature.getAgentBotSignature().get(ubiEvent.getAgentInfo()).contains(221))
                    || (ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()).contains(222) && ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()).contains(223))) {
                ubiEvent.getBotFlags().add(210);
            }

            if (agentSignature.getAgentBotSignature() != null && agentSignature.getAgentBotSignature().size() > 0) {
                ubiEvent.getBotFlags().addAll(agentSignature.getAgentBotSignature().get(ubiEvent.getAgentInfo()));
            }
        }

        if (ipSignature != null) {
            if (ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()).contains(223)) {
                ubiEvent.getBotFlags().add(211);
            }
            if (ipSignature.getIpBotSignature() != null && ipSignature.getIpBotSignature().size() > 0) {
                ubiEvent.getBotFlags().addAll(ipSignature.getIpBotSignature().get(ubiEvent.getClientIP()));
            }
        }

        if (agentAndIpSignature != null) {
            if (agentAndIpSignature.getAgentIpBotSignature() != null && agentAndIpSignature.getAgentIpBotSignature().size() > 0) {
                ubiEvent.getBotFlags().addAll(agentAndIpSignature.getAgentIpBotSignature().get(ubiEvent.getAgentInfo() + ubiEvent.getClientIP()));
            }
        }
    }

    @Override
    public void processBroadcastElement(Map<String, Object> map, Context context, Collector<UbiEvent> out) throws Exception {
        BroadcastState<String, Object> attributeBroadcastState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        attributeBroadcastState.putAll(map);
    }
}
