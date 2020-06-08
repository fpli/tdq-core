package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class IpWindowProcessFunction
    extends ProcessWindowFunction<
    IpAttributeAccumulator, BotSignature, Tuple,
    TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<IpAttributeAccumulator> elements,
      Collector<BotSignature> out)
      throws Exception {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && ipAttribute.getBotFlagList() != null
        && ipAttribute.getBotFlagList().size() > 0) {
      BotSignature botSignature = new BotSignature();
      botSignature.setType("ip");
      botSignature.setIsGeneration(false);
      botSignature.setBotFlags(new ArrayList<>(ipAttribute.getBotFlagList()));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setIp(ipAttribute.getClientIp());
      out.collect(
          botSignature);
    } else if (context.currentWatermark() < context.window().maxTimestamp()
        && ipAttributeAccumulator.getBotFlagStatus().containsValue(1)
        && ipAttribute.getBotFlagList() != null
        && ipAttribute.getBotFlagList().size() > 0) {
      HashSet<Integer> generationBotFlag = new HashSet<>();
      for (Map.Entry<Integer, Integer> newBotFlagMap :
          ipAttributeAccumulator.getBotFlagStatus().entrySet()) {
        if (newBotFlagMap.getValue() == 1) {
          generationBotFlag.add(newBotFlagMap.getKey());
        }
      }
      BotSignature botSignature = new BotSignature();
      botSignature.setType("ip");
      botSignature.setIsGeneration(true);
      botSignature.setBotFlags(new ArrayList<>(generationBotFlag));
      botSignature.setExpirationTime(context.window().maxTimestamp());
      botSignature.setIp(ipAttribute.getClientIp());
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
