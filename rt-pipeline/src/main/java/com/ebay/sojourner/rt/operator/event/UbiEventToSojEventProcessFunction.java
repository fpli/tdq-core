package com.ebay.sojourner.rt.operator.event;

import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.util.SojUtils;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class UbiEventToSojEventProcessFunction extends ProcessFunction<UbiEvent, SojEvent> {

  private OutputTag outputTag;
  private List<Integer> intermediateBotFlagList = Arrays.asList(220, 221, 222, 223);

  public UbiEventToSojEventProcessFunction(OutputTag outputTag) {
    this.outputTag = outputTag;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    super.open(parameters);
  }

  @Override
  public void processElement(UbiEvent ubiEvent, Context context, Collector<SojEvent> out)
      throws Exception {
    SojEvent sojEvent = SojUtils.convertUbiEvent2SojEvent(ubiEvent);

    // split bot event and nonbot event
    if (sojEvent.getBotFlags().size() == 0 || CollectionUtils
        .subtract(sojEvent.getBotFlags(), intermediateBotFlagList).size() == 0) {
      out.collect(sojEvent);
    } else {
      context.output(outputTag, sojEvent);
    }
  }

  @Override
  public void close() throws Exception {
    super.close();
  }
}
