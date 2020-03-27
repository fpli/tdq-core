package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.rt.util.Constants;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunctionForRNO {

  public static FlinkKafkaConsumerBase<RawEvent> generateWatermark() {
    return KafkaConnectorFactory
        .createKafkaConsumer(Constants.TOPIC_PATHFINDER_EVENTS, Constants.BOOTSTRAP_SERVERS_RNO,
            Constants.GROUP_ID_RNO)
        .setStartFromLatest()
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }
}
