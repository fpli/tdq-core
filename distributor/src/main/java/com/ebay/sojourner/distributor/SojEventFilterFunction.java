package com.ebay.sojourner.distributor;

import com.ebay.sojourner.common.model.RawSojEventWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.configuration.Configuration;

@Slf4j
public class SojEventFilterFunction extends RichFilterFunction<RawSojEventWrapper> {

  @Override
  public boolean filter(RawSojEventWrapper rawSojEventWrapper) throws Exception {
    int pageId = rawSojEventWrapper.getPageId();
    return SojEventDispatcher.mappings.containsKey(pageId);
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    log.warn("open...");
    super.open(parameters);
  }

  @Override
  public void close() throws Exception {
    log.warn("close...");
    SojEventDispatcher.close();
    super.close();
  }
}
