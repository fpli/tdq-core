package com.ebay.sojourner.rt.operators.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;

public class SplitFunction implements OutputSelector<Tuple4<String, Boolean, Set<Integer>, Long>> {

  @Override
  public Iterable<String> select(Tuple4<String, Boolean, Set<Integer>, Long> value) {
    List<String> output = new ArrayList<>();
    if (value.f1) {
      output.add("generation");
    } else {
      output.add("expiration");
    }
    return output;
  }
}
