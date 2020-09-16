package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class DataSkewProcessFunction extends
    ProcessFunction<RawEvent, RawEvent> {

  private OutputTag outputTag;
  private Boolean isFilter;
  private Set<String> filterGuidSet;

  public DataSkewProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
    isFilter = FlinkEnvUtils.getBoolean(Property.IS_FILTER);
    filterGuidSet = FlinkEnvUtils.getSet(Property.FILTER_GUID_LIST);
  }

  @Override
  public void close() throws Exception {
    super.close();
  }

  @Override
  public void processElement(RawEvent rawEvent, Context context, Collector<RawEvent> out)
      throws Exception {

    if (isFilter) {
      Map<String, String> map = new HashMap<>();
      map.putAll(rawEvent.getSojA());
      map.putAll(rawEvent.getSojK());
      map.putAll(rawEvent.getSojC());
      if (map.containsKey("g")) {
        String guid = map.get("g");
        if (filterGuidSet.contains(guid)) {
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

