package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiEvent;

public class BotFlagsMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {

  public static final int BOT_FLAGS_DEFAULT = 0;

  @Override
  public void start(SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void feed(UbiEvent source, SessionAccumulator sessionAccumulator) throws Exception {
  }

  @Override
  public void end(SessionAccumulator sessionAccumulator) throws Exception {

    // Set BotFlags Default Value to avoid session container BOT flags is empty String
    sessionAccumulator.getUbiSession().setBotFlags(BOT_FLAGS_DEFAULT);
  }

  @Override
  public void init() throws Exception {
    // nothing to do
  }
}
