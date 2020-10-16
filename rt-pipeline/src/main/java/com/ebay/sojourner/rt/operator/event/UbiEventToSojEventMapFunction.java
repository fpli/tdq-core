package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojUtils;
import org.apache.flink.api.common.functions.RichMapFunction;

public class UbiEventToSojEventMapFunction extends RichMapFunction<UbiEvent, SojEvent> {

  @Override
  public SojEvent map(UbiEvent ubiEvent) throws Exception {
    return SojUtils.convertUbiEvent2SojEvent(ubiEvent);
  }
}
