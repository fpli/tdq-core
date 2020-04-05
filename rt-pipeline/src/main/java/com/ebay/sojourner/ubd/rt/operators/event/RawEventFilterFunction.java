package com.ebay.sojourner.ubd.rt.operators.event;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.configuration.Configuration;

public class RawEventFilterFunction extends RichFilterFunction<RawEvent> {

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
    //        getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
  }

  @Override
  public boolean filter(RawEvent rawEvent) throws Exception {
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());
    String applicationPayload = null;
    String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
    String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
    String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
    if (mARecString != null) {
      applicationPayload = mARecString;
    }
    if ((applicationPayload != null) && (mKRecString != null)) {
      applicationPayload = applicationPayload + "&" + mKRecString;
    }

    // else set C record
    if (applicationPayload == null) {
      applicationPayload = mCRecString;
    }
    if (map.containsKey("g")) {
      String g = map.get("g");
      return g.hashCode() % 10 == 0;
    }
    return false;
  }
}
