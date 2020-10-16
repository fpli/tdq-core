package com.ebay.sojourner.rt.operator.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SignatureInfo;
import com.ebay.sojourner.common.util.SojTimestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

@Slf4j
public class IpWindowProcessFunction extends
    ProcessWindowFunction<IpAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private static final String signatureId = "ip";

  @Override
  public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();
    Map<Integer, SignatureInfo> signatureStates = ipAttributeAccumulator.getSignatureStatus();
    Integer clientIp = ipAttribute.getClientIp();
    long windowEndTime = context.window().maxTimestamp();
    long timestamp = SojTimestamp.getSojTimestampToUnixTimestamp(ipAttribute.getTimestamp());

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && signatureStates.size() > 0) {
      sendSignatures(clientIp, timestamp, signatureStates, out, context);
      out.collect(new BotSignature(signatureId, null, clientIp, null,
          new ArrayList<>(signatureStates.keySet()),
          windowEndTime, false, 3, windowEndTime));
    } else if (context.currentWatermark() < context.window().maxTimestamp()) {
      sendSignatures(clientIp, timestamp, signatureStates, out, context);
    }
  }

  private void sendSignatures(Integer clientIp, long timestamp,
      Map<Integer, SignatureInfo> signatureStates, Collector<BotSignature> out, Context context) {

    for (Map.Entry<Integer, SignatureInfo> entry : signatureStates.entrySet()) {
      if (!entry.getValue().isSent()) {
        out.collect(new BotSignature(signatureId, null, clientIp, null,
            new ArrayList<>(Arrays.asList(entry.getKey())),
            context.window().maxTimestamp(), true, entry.getValue().getType(),
            timestamp));
      }
    }
  }
}
