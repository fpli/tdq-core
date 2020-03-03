package com.ebay.sojourner.ubd.rt.operators.event;

import com.alibaba.fastjson.JSON;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

public class RawEvent2JSONMapFunction extends RichMapFunction<RawEvent, String> {

  @Override
  public void open(Configuration conf) throws Exception {}

  @Override
  public String map(RawEvent rawEvent) throws Exception {

    return JSON.toJSONString(rawEvent);
  }
}
