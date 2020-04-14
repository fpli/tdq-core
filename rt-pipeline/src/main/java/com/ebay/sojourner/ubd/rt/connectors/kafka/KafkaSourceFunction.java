package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static FlinkKafkaConsumerBase<RawEvent> generateWatermark(String topic, String brokers,
      String groupId) {
    return KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId)
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }
}
