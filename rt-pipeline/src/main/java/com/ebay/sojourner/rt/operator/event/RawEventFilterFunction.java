package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.RawEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.configuration.Configuration;

public class RawEventFilterFunction extends RichFilterFunction<RawEvent> {

  @Override
  public void open(Configuration conf) throws Exception {
    super.open(conf);
  }

  @Override
  public boolean filter(RawEvent rawEvent) throws Exception {
    Map<String, String> map = new HashMap<>();
    map.putAll(rawEvent.getSojA());
    map.putAll(rawEvent.getSojK());
    map.putAll(rawEvent.getSojC());

    if (map.containsKey("g")) {
      String g = map.get("g");
      return Math.abs(g.hashCode() % 20) == 0;
    }
    return false;
  }
}
