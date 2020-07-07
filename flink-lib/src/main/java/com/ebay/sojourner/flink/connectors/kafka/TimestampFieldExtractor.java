package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.model.PulsarEvent;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.SojTimestamp;

public class TimestampFieldExtractor {

  public static <T> long getField(T t) {

    if (t instanceof RawEvent) {
      RawEvent rawEvent = (RawEvent) t;
      return rawEvent.getRheosHeader().getEventCreateTimestamp();
    } else if (t instanceof SojSession) {
      SojSession sojSession = (SojSession) t;
      return SojTimestamp.getSojTimestampToUnixTimestamp(sojSession.getAbsStartTimestamp());
    } else if (t instanceof SojEvent) {
      SojEvent sojEvent = (SojEvent) t;
      return sojEvent.getGenerateTime();
    } else if (t instanceof JetStreamOutputEvent) {
      JetStreamOutputEvent jetStreamOutputEvent = (JetStreamOutputEvent) t;
      return jetStreamOutputEvent.getEventCreateTimestamp();
    } else if (t instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) t;
      return SojTimestamp
          .getSojTimestampToUnixTimestamp(intermediateSession.getAbsStartTimestamp());
    } else if (t instanceof JetStreamOutputSession) {
      JetStreamOutputSession jetStreamOutputSession = (JetStreamOutputSession) t;
      return jetStreamOutputSession.getEventCreateTimestamp();
    } else if (t instanceof BotSignature) {
      BotSignature botSignature = (BotSignature) t;
      return botSignature.getExpirationTime();
    } else if (t instanceof PulsarEvent) {
      PulsarEvent pulsarEvent = (PulsarEvent) t;
      return pulsarEvent.getEventCreateTimestamp();
    }
    else {
      throw new IllegalStateException("Cannot extract timestamp filed for generate watermark");
    }
  }
}
