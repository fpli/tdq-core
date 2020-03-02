package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

public class IpBroadcastProcessFunctionForSession
    extends BroadcastProcessFunction<UbiSession, IpSignature, UbiSession> implements Serializable {
  private static volatile Logger logger =
      Logger.getLogger(IpBroadcastProcessFunctionForSession.class);

  @Override
  public void processElement(
      UbiSession ubiSession, ReadOnlyContext context, Collector<UbiSession> out) throws Exception {
    ReadOnlyBroadcastState<String, Set<Integer>> ipBroadcastState =
        context.getBroadcastState(MapStateDesc.ipSignatureDesc);
    if (ipBroadcastState.contains(ubiSession.getClientIp())) {
      ubiSession.getBotFlagList().addAll(ipBroadcastState.get(ubiSession.getClientIp()));
      System.out.println(ubiSession);
    }
    //         System.out.println("test========"+ipBroadcastState.contains("10.246.240.175"));
    for (Map.Entry<String, Set<Integer>> entry : ipBroadcastState.immutableEntries()) {
      System.out.println("test========" + entry);
      //
    }
    out.collect(ubiSession);
  }

  @Override
  public void processBroadcastElement(
      IpSignature ipSignature, Context context, Collector<UbiSession> out) throws Exception {
    BroadcastState<String, Set<Integer>> ipBroadcastState =
        context.getBroadcastState(MapStateDesc.ipSignatureDesc);
    for (Map.Entry<String, Set<Integer>> entry : ipSignature.getIpBotSignature().entrySet()) {
      ipBroadcastState.put(entry.getKey(), entry.getValue());
      //            System.out.println(entry);
    }
  }
}
