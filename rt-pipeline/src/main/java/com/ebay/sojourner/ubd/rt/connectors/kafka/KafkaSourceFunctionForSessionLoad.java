package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.rt.util.Constants;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunctionForSessionLoad {
  public static FlinkKafkaConsumerBase<SojSession> generateWatermark() {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(Constants.TOPIC_PRODUCER_SESSION, Constants.BOOTSTRAP_SERVERS_SESSION,
            Constants.GROUP_ID_SESSION, SojSession.class)
        .setStartFromLatest()
        .assignTimestampsAndWatermarks(
            new SojSessionBoundedOutOfOrdernessTimestampExtractor(Time.seconds(10)));
  }
}
