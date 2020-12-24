package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.UbiSession;
import org.apache.flink.api.common.functions.RichFilterFunction;

public class OpenSessionFilterFunction extends RichFilterFunction<UbiSession> {

  @Override
  public boolean filter(UbiSession ubiSession) throws Exception {
    return !ubiSession.isOpenEmit();
  }
}
