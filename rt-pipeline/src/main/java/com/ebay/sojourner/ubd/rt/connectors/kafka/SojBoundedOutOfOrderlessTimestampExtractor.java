package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.IntermediateSession;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiSessionForDQ;
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
      return sojSession.getAbsStartTimestamp();
    } else if (t instanceof SojEvent) {
      SojEvent sojEvent = (SojEvent) t;
      return sojEvent.getGenerateTime();
    } else if (t instanceof UbiSessionForDQ) {
      UbiSessionForDQ ubiSessionForDQ = (UbiSessionForDQ) t;
      return ubiSessionForDQ.getAbsStartTimestamp();
    } else if (t instanceof IntermediateSession) {
      IntermediateSession intermediateSession = (IntermediateSession) t;
      return intermediateSession.getAbsStartTimestamp();
    } else {
      return 0;
    }
  }
}
