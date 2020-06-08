package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.Guid;
import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class GuidWindowProcessFunction extends
    ProcessWindowFunction<GuidAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  private final String signatureId = "guid";

  @Override
  public void process(Tuple tuple, Context context, Iterable<GuidAttributeAccumulator> elements,
      Collector<BotSignature> out) throws Exception {

    GuidAttributeAccumulator guidAttributeAccumulator = elements.iterator().next();
    GuidAttribute guidAttribute = guidAttributeAccumulator.getGuidAttribute();
    Map<Integer, Integer> signatureStates = guidAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = guidAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      BotSignature botSignature = new BotSignature();
      botSignature.setType(signatureId);
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(guidAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setGuid(new Guid(guidAttribute.getGuid1(), guidAttribute.getGuid2()));
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
      botSignature.setGuid(new Guid(guidAttribute.getGuid1(), guidAttribute.getGuid2()));
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
