package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class IpWindowProcessFunction
    extends ProcessWindowFunction<
    IpAttributeAccumulator, Tuple4<String, Boolean, Set<Integer>, Long>, Tuple, TimeWindow> {

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<IpAttributeAccumulator> elements,
      Collector<Tuple4<String, Boolean, Set<Integer>, Long>> out)
      throws Exception {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && ipAttribute.getBotFlagList() != null
        && ipAttribute.getBotFlagList().size() > 0) {
      out.collect(
          new Tuple4<>(
              "ip" + ipAttribute.getClientIp(),
              false,
              ipAttribute.getBotFlagList(),
              context.window().maxTimestamp()));
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
      out.collect(
          new Tuple4<>(
              "ip" + ipAttribute.getClientIp(),
              true,
              generationBotFlag,
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
