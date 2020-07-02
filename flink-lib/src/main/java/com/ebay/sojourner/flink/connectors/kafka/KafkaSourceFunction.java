package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.SojBytesEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumer<T> buildSource(KafkaConsumerConfig config, Class<T> tClass) {

    FlinkKafkaConsumer<T> kafkaConsumer = KafkaConsumerFactory.getConsumer(config, tClass);

    if (tClass.isAssignableFrom(SojBytesEvent.class)) {
      return kafkaConsumer;
    } else {
      kafkaConsumer.assignTimestampsAndWatermarks(
          new SojBoundedOutOfOrderlessTimestampExtractor(Time.seconds(10)));
    }

    return kafkaConsumer;
  }
}
