package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
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

  private final String signatureId = "ip";

  @Override
  public void process(Tuple tuple, Context context, Iterable<IpAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();
    Map<Integer, Integer> signatureStates = ipAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = ipAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      BotSignature botSignature = new BotSignature();
      botSignature.setType(signatureId);
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(ipAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setIp(ipAttribute.getClientIp());
      out.collect(botSignature);

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateSignatures = SignatureUtils.generateNewSignature(signatureStates);

      BotSignature botSignature = new BotSignature();
      botSignature.setType(signatureId);
      botSignature.setIsGeneration(true);
      botSignature.setBotFlags(new ArrayList<>(newGenerateSignatures));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setIp(ipAttribute.getClientIp());
      out.collect(botSignature);

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
