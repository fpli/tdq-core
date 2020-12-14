package com.ebay.sojourner.distributor.broadcast;

import com.ebay.sojourner.common.model.PageIdTopicMapping;
import com.ebay.sojourner.common.model.RawSojEventWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

@Slf4j
public class SojEventDistProcessFunction extends
    BroadcastProcessFunction<RawSojEventWrapper, PageIdTopicMapping, RawSojEventWrapper> {

  private final MapStateDescriptor<Integer, PageIdTopicMapping> stateDescriptor;

  public SojEventDistProcessFunction(MapStateDescriptor<Integer, PageIdTopicMapping> descriptor) {
    this.stateDescriptor = descriptor;
  }

  @Override
  public void processElement(RawSojEventWrapper sojEventWrapper, ReadOnlyContext ctx,
                             Collector<RawSojEventWrapper> out) throws Exception {
    ReadOnlyBroadcastState<Integer, PageIdTopicMapping> broadcastState =
        ctx.getBroadcastState(stateDescriptor);

    int pageId = sojEventWrapper.getPageId();
    PageIdTopicMapping mapping = broadcastState.get(pageId);
    if (mapping != null) {
      for (String topic : mapping.getTopics()) {
        sojEventWrapper.setTopic(topic);
        out.collect(sojEventWrapper);
      }
    }
  }

  @Override
  public void processBroadcastElement(PageIdTopicMapping mapping, Context ctx,
                                      Collector<RawSojEventWrapper> out) throws Exception {
    log.info("process broadcast pageId topic mapping: {}", mapping);
    BroadcastState<Integer, PageIdTopicMapping> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    broadcastState.put(mapping.getPageId(), mapping);
  }
}
