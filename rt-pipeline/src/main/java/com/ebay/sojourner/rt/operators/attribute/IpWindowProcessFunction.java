package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.common.model.SignatureInfo;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class IpWindowProcessFunction extends
    ProcessWindowFunction<IpAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private static final String signatureId = "ip";

  @Override
  public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();
    Map<Integer, SignatureInfo> signatureStates = ipAttributeAccumulator.getSignatureStatus();
    Set<Integer> botFlagList = ipAttribute.getBotFlagList();
    Integer clientIp = ipAttribute.getClientIp();
    long windowEndTime = context.window().maxTimestamp();

    if (context.currentWatermark() >= context.window().maxTimestamp()) {
      sendSignatures(clientIp, signatureStates, out, context);
      out.collect(new BotSignature(signatureId, null, clientIp, null,
          new ArrayList<>(signatureStates.keySet()),
          windowEndTime, false, 3, windowEndTime));

    } else if (context.currentWatermark() < context.window().maxTimestamp()) {
      sendSignatures(clientIp, signatureStates, out, context);
      //      Set<Integer> newGenerateSignatures = SignatureUtils.generateNewSignature
      //      (signatureStates);
      //
      //      out.collect(new BotSignature(signatureId, null, clientIp, null,
      //          new ArrayList<>(newGenerateSignatures),
      //          windowEndTime, true));

    }
  }

  private void sendSignatures(Integer clientIp, Map<Integer, SignatureInfo> signatureStates,
      Collector<BotSignature> out, Context context) {
    for (Map.Entry<Integer, SignatureInfo> entry : signatureStates.entrySet()) {
      if (!entry.getValue().isSent()) {
        out.collect(new BotSignature(signatureId, null, clientIp, null,
            new ArrayList<>(entry.getKey()),
            context.window().maxTimestamp(), true, entry.getValue().getType(),
            context.currentWatermark()));
      }
    }
  }

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
  }

  @Override
  public void clear(Context context) throws Exception {
    super.clear(context);
  }
}
