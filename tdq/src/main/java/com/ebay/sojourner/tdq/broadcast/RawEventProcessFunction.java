package com.ebay.sojourner.tdq.broadcast;

import com.ebay.sojourner.common.model.*;
import com.ebay.sojourner.common.util.LkpManager;
import com.ebay.sojourner.common.util.SojUtils;
import com.ebay.sojourner.tdq.meta.MetricType;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

@Slf4j
public class RawEventProcessFunction extends
    BroadcastProcessFunction<RawEvent, TdqConfigMapping, RawSojEventWrapper> {

  private final MapStateDescriptor<String, TdqConfigMapping> stateDescriptor;

  public RawEventProcessFunction(MapStateDescriptor<String, TdqConfigMapping> descriptor) {
    this.stateDescriptor = descriptor;
  }

  @Override
  public void processElement(RawEvent RawEvent, ReadOnlyContext ctx,
                             Collector<RawSojEventWrapper> out) throws Exception {
    ReadOnlyBroadcastState<String, TdqConfigMapping> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    RawEventMetric rawEventMetric = new RawEventMetric();
    for (Map.Entry<String,TdqConfigMapping> entry: broadcastState.immutableEntries()){


    }
    int pageId = RawEvent.getPageId();
    PageIdTopicMapping mapping = broadcastState.get(pageId);
    if (mapping != null) {
      for (String topic : mapping.getTopics()) {
        sojEventWrapper.setTopic(topic);
        out.collect(sojEventWrapper);
      }
    }
  }

  @Override
  public void processBroadcastElement(TdqConfigMapping mapping, Context ctx,
                                      Collector<RawSojEventWrapper> out) throws Exception {
    log.info("process broadcast pageId topic mapping: {}", mapping);
    BroadcastState<String, TdqConfigMapping> broadcastState =
        ctx.getBroadcastState(stateDescriptor);
    int metricTypeId=mapping.getMetricType();
    StringBuilder metricDesc = new StringBuilder(mapping.getMetricName());
    MetricType metricType=MetricType.of(metricTypeId);
    if(metricType!=null) {
      metricDesc.append("_").append(metricType.getDesc());
    }
    broadcastState.put(metricDesc.toString(), mapping);
  }

  private void calculateMetrics(RawEventMetric rawEventMetric,RawEvent rawEvent,TdqConfigMapping tdqConfigMapping){
    MetricType metricType=MetricType.of(tdqConfigMapping.getMetricType());
    switch(metricType){
      case TAGMISSCNT:

    }
  }
  private void calculateTagMissingCntMetrics(RawEventMetric rawEventMetric,RawEvent rawEvent,TdqConfigMapping tdqConfigMapping){
          Integer pageId = SojUtils.getPageId(rawEvent);
          if(pageId!=null){
            String pageFamily = LkpManager.getInstance().get
          }
  }
}
