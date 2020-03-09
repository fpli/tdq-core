package com.ebay.sojourner.ubd.rt.common.broadcast;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

public class IpBroadcastProcessFunction
    extends BroadcastProcessFunction<UbiEvent, IpSignature, UbiEvent> implements Serializable {

  private static volatile Logger logger = Logger.getLogger(IpBroadcastProcessFunction.class);

  @Override
  public void processElement(UbiEvent ubiEvent, ReadOnlyContext context, Collector<UbiEvent> out)
      throws Exception {
    ReadOnlyBroadcastState<String, Set<Integer>> ipBroadcastState =
        context.getBroadcastState(MapStateDesc.ipSignatureDesc);
    //        System.out.println(ubiEvent);
    if (ipBroadcastState.contains(ubiEvent.getClientIP())) {
      ubiEvent.getBotFlags().addAll(ipBroadcastState.get(ubiEvent.getClientIP()));
      //            System.out.println(ubiEvent);
    }
    //         System.out.println("test========"+ipBroadcastState.contains("10.246.240.175"));
    for (Map.Entry<String, Set<Integer>> entry : ipBroadcastState.immutableEntries()) {
      //            System.out.println("test========"+entry);
      //
    }
    out.collect(ubiEvent);
  }

  @Override
  public void processBroadcastElement(
      IpSignature ipSignature, Context context, Collector<UbiEvent> out) throws Exception {
    BroadcastState<String, Set<Integer>> ipBroadcastState =
        context.getBroadcastState(MapStateDesc.ipSignatureDesc);
    for (Map.Entry<String, Set<Integer>> entry : ipSignature.getIpBotSignature().entrySet()) {
      ipBroadcastState.put(entry.getKey(), entry.getValue());
      //            System.out.println(entry);
    }
  }
}
