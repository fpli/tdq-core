package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunction {

  public static FlinkKafkaConsumerBase<RawEvent> generateWatermark(String topic, String brokers,
      String groupId) {
    return KafkaConnectorFactory
<<<<<<< HEAD:rt-pipeline/src/main/java/com/ebay/sojourner/ubd/rt/connectors/kafka/KafkaSourceFunction.java
        .createKafkaConsumer(topic,brokers,groupId)
        .setStartFromLatest()
=======
        .createKafkaConsumer(topic, brokers, groupId)
>>>>>>> sojourner-performance:rt-pipeline/src/main/java/com/ebay/sojourner/ubd/rt/connectors/kafka/KafkaSourceFunctionForQA.java
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }
}
