package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.IpAttribute;
import com.ebay.sojourner.common.model.IpAttributeAccumulator;
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

public class IpWindowProcessFunction extends
    ProcessWindowFunction<IpAttributeAccumulator,
        Tuple5<String, String, Boolean, Set<Integer>, Long>, Tuple, TimeWindow> {

  private final String signatureId = "ip";

  @Override
  public void process(
      Tuple tuple,
      Context context,
      Iterable<IpAttributeAccumulator> elements,
      Collector<Tuple5<String, String, Boolean, Set<Integer>, Long>> out) {

    IpAttributeAccumulator ipAttributeAccumulator = elements.iterator().next();
    IpAttribute ipAttribute = ipAttributeAccumulator.getIpAttribute();
    Map<Integer, Integer> signatureStates = ipAttributeAccumulator.getSignatureStates();
    Set<Integer> botFlagList = ipAttribute.getBotFlagList();

    if (context.currentWatermark() >= context.window().maxTimestamp()
        && botFlagList != null
        && botFlagList.size() > 0) {

      out.collect(
          new Tuple5<>(
              signatureId,
              ipAttribute.getClientIp() + Constants.SEPARATOR,
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
              ipAttribute.getClientIp() + Constants.SEPARATOR,
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
