package com.ebay.sojourner.rt.operators.attribute;

import com.ebay.sojourner.common.model.BotSignature;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;

public class SplitFunction implements OutputSelector<BotSignature> {

  @Override
  public Iterable<String> select(BotSignature botSignature) {
    List<String> output = new ArrayList<>();
    if (botSignature.getIsGeneration()) {
      output.add("generation");
    } else {
      output.add("expiration");
    }
    return output;
  }
}
