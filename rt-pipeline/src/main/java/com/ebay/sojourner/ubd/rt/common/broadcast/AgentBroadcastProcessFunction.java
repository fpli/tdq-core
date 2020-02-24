package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Set;

@Slf4j
public class AgentBroadcastProcessFunction extends BroadcastProcessFunction<UbiSession, Tuple2<String, Set<Integer>>, UbiSession> {

    @Override
    public void processElement(UbiSession ubiSession, ReadOnlyContext context, Collector<UbiSession> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.agentSignatureDesc);
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
    public void processBroadcastElement(Tuple2<String, Set<Integer>> attributeSignature, Context context, Collector<UbiSession> out) throws Exception {
        BroadcastState<String, Set<Integer>> agentBroadcastState = context.getBroadcastState(MapStateDesc.agentSignatureDesc);
        if (attributeSignature.f1 == null) {
            agentBroadcastState.remove(attributeSignature.f0);
        }
        agentBroadcastState.put(attributeSignature.f0, attributeSignature.f1);

    }
}
