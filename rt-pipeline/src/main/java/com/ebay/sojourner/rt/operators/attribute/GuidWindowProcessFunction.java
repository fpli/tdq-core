package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import com.ebay.sojourner.flink.common.util.Constants;
import com.ebay.sojourner.rt.common.util.SignatureUtils;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class GuidWindowProcessFunction extends ProcessWindowFunction<GuidAttributeAccumulator,
    Tuple5<String, String, Boolean, Set<Integer>, Long>,
    Tuple, TimeWindow> {

  private final String signatureId = "guid";

  @Override
  public void process(Tuple tuple,
      Context context,
      Iterable<GuidAttributeAccumulator> elements,
      Collector<Tuple5<String, String, Boolean, Set<Integer>, Long>> out) {

    GuidAttributeAccumulator guidAttributeAccumulator = elements.iterator().next();
    GuidAttribute guidAttribute = guidAttributeAccumulator.getGuidAttribute();
    Map<Integer, Integer> signatureStates = guidAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = guidAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      out.collect(
          new Tuple5<>(
              signatureId,
              guidAttribute.getGuid1() + Constants.SEPARATOR + guidAttribute.getGuid2(),
              false,
              botFlagList,
              context.window().maxTimestamp()));

    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && signatureStates.containsValue(1)
        && botFlagList != null
        && botFlagList.size() > 0) {

      Set<Integer> newGenerateSignatures = SignatureUtils.generateNewSignature(signatureStates);

      out.collect(
          new Tuple5<>(
              signatureId,
              guidAttribute.getGuid1() + Constants.SEPARATOR + guidAttribute.getGuid2(),
              true,
              newGenerateSignatures,
              context.window().maxTimestamp()));
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
