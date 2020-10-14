package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.RawEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class DataSkewProcessFunction extends
    ProcessFunction<RawEvent, RawEvent> {

  private OutputTag outputTag;
  private Boolean isFilter;
  private Set<String> filterPageIdSet;

  public DataSkewProcessFunction(OutputTag outputTag, Boolean isFilter, Set<String> pageIdList) {
    this.outputTag = outputTag;
    this.isFilter = isFilter;
    this.filterPageIdSet = pageIdList;
  }

  @Override
  public void processElement(RawEvent rawEvent, Context context, Collector<RawEvent> out)
      throws Exception {

    if (isFilter) {
      Map<String, String> map = new HashMap<>();
      map.putAll(rawEvent.getSojA());
      map.putAll(rawEvent.getSojK());
      map.putAll(rawEvent.getSojC());
      if (map.containsKey("p")) {
        String guid = map.get("p");
        if (filterPageIdSet.contains(guid)) {
          context.output(outputTag, rawEvent);
        } else {
          out.collect(rawEvent);
        }
      }
    } else {
      out.collect(rawEvent);
    }
  }
}

