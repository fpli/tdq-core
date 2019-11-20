package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class IpBroadcastProcessFunction extends BroadcastProcessFunction<UbiEvent, IpSignature,UbiEvent> {
    Logger logger = Logger.getLogger(IpBroadcastProcessFunction.class);

    @Override
    public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> ipBroadcastState = context.getBroadcastState(MapStateDesc.ipSignatureDesc);
        if(ipBroadcastState.contains(ubiEvent.getClientIP())){
            ubiEvent.getBotFlags().addAll(ipBroadcastState.get(ubiEvent.getClientIP()));
        }
        out.collect(ubiEvent);
    }

    @Override
    public void processBroadcastElement(IpSignature ipSignature, Context context, Collector<UbiEvent> out) throws Exception {
        BroadcastState<String, Set<Integer>> ipBroadcastState = context.getBroadcastState(MapStateDesc.ipSignatureDesc);
        for (Map.Entry<String, Set<Integer>> entry : ipSignature.getIpBotSignature().entrySet()) {
            ipBroadcastState.put(entry.getKey(), entry.getValue());
        }
    }
}
