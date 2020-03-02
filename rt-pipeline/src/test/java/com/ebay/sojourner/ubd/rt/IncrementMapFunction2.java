package com.ebay.sojourner.ubd.rt;

import org.apache.flink.api.common.functions.MapFunction;

public class IncrementMapFunction2 implements MapFunction<Long, Long> {

  @Override
  public Long map(Long record) throws Exception {
    return record + 1;
  }
}
