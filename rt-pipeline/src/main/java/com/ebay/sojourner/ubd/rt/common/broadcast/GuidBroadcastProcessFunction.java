package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Set;

public class GuidBroadcastProcessFunction extends BroadcastProcessFunction<UbiSession, Tuple2<String, Set<Integer>>, UbiSession> {
    @Override
    public void processElement(UbiSession ubiSession, ReadOnlyContext context, Collector<UbiSession> out) throws Exception {
        ReadOnlyBroadcastState<String, Set<Integer>> attributeSignature = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);

        if (attributeSignature.contains("guid" + ubiSession.getGuid())) {
            ubiSession.getBotFlagList().addAll(attributeSignature.get("guid" + ubiSession.getGuid()));
        }
        out.collect(ubiSession);
    }

    @Override
    public void processBroadcastElement(Tuple2<String, Set<Integer>> attributeSignature, Context context, Collector<UbiSession> out) throws Exception {
        BroadcastState<String, Set<Integer>> guidBroadcastState = context.getBroadcastState(MapStateDesc.attributeSignatureDesc);
        if (attributeSignature.f1 == null) {
            guidBroadcastState.remove(attributeSignature.f0);
        }
        guidBroadcastState.put(attributeSignature.f0, attributeSignature.f1);

    }
}
