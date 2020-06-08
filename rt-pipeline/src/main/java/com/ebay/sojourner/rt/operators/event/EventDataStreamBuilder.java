package com.ebay.sojourner.rt.operators.event;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import org.apache.flink.streaming.api.datastream.DataStream;

public class EventDataStreamBuilder {

  public static DataStream<UbiEvent> build(DataStream<RawEvent> sourceDataStream, String dc,
      Integer parallelism, String slotGroup) {

    DataStream<UbiEvent> eventDataStream = sourceDataStream
        .map(new EventMapFunction())
        .setParallelism(parallelism)
        .slotSharingGroup(slotGroup)
        .name(String.format("Event Operator For %s", dc))
        .uid(String.format("event-%s-id", dc));

    return eventDataStream;
  }
}
