package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.SojTimestamp;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SojBoundedOutOfOrderlessTimestampExtractor<T> extends
    BoundedOutOfOrdernessTimestampExtractor<T> {

  private static final long serialVersionUID = -1902049986991898214L;

  public SojBoundedOutOfOrderlessTimestampExtractor(
      Time maxOutOfOrderless) {
    super(maxOutOfOrderless);
  }

  @Override
  public long extractTimestamp(T t) {
    if (t instanceof RawEvent) {
      RawEvent rawEvent = (RawEvent) t;
      return rawEvent.getRheosHeader().getEventCreateTimestamp();
    } else if (t instanceof SojSession) {
      SojSession sojSession = (SojSession) t;
      return SojTimestamp.getSojTimestampToUnixTimestamp(sojSession.getAbsEndTimestamp());
    } else if (t instanceof SojEvent) {
      SojEvent sojEvent = (SojEvent) t;
      return sojEvent.getGenerateTime();
    } else if (t instanceof JetStreamOutputEvent) {
      JetStreamOutputEvent jetStreamOutputEvent = (JetStreamOutputEvent) t;
      return jetStreamOutputEvent.getEventCreateTimestamp();
    } else if (t instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) t;
      return SojTimestamp.getSojTimestampToUnixTimestamp(intermediateSession.getAbsEndTimestamp());
    } else if (t instanceof JetStreamOutputSession) {
      JetStreamOutputSession jetStreamOutputSession = (JetStreamOutputSession) t;
      return jetStreamOutputSession.getEventCreateTimestamp();
    } else if (t instanceof BotSignature) {
      BotSignature botSignature = (BotSignature) t;
      return botSignature.getExpirationTime();
    } else {
      return 0;
    }
  }
}
