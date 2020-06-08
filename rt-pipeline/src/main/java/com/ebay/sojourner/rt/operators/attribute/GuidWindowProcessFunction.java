package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.Guid;
import com.ebay.sojourner.common.model.GuidAttribute;
import com.ebay.sojourner.common.model.GuidAttributeAccumulator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class GuidWindowProcessFunction
    extends ProcessWindowFunction<
    GuidAttributeAccumulator, BotSignature, Tuple, TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<GuidAttributeAccumulator> elements,
      Collector<BotSignature> out)
      throws Exception {

    GuidAttributeAccumulator guidAttributeAccumulator = elements.iterator().next();
    GuidAttribute guidAttribute = guidAttributeAccumulator.getGuidAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && guidAttribute.getBotFlagList() != null
        && guidAttribute.getBotFlagList().size() > 0) {
      BotSignature botSignature = new BotSignature();
      botSignature.setType("guid");
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(guidAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setGuid(new Guid(guidAttribute.getGuid1(),guidAttribute.getGuid2()));
      out.collect(
        botSignature);
    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && guidAttributeAccumulator.getBotFlagStatus().containsValue(1)
        && guidAttribute.getBotFlagList() != null
        && guidAttribute.getBotFlagList().size() > 0) {
      HashSet<Integer> generationBotFlag = new HashSet<>();
      for (Map.Entry<Integer, Integer> newBotFlagMap :
          guidAttributeAccumulator.getBotFlagStatus().entrySet()) {
        if (newBotFlagMap.getValue() == 1) {
          generationBotFlag.add(newBotFlagMap.getKey());
        }
      }
      BotSignature botSignature = new BotSignature();
      botSignature.setType("guid");
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(generationBotFlag));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setGuid(new Guid(guidAttribute.getGuid1(),guidAttribute.getGuid2()));
      out.collect(
          botSignature);
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
