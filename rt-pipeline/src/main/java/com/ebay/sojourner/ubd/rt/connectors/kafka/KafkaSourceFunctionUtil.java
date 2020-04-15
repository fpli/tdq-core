package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.rt.util.Constants;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

public class KafkaSourceFunctionUtil {

  public static FlinkKafkaConsumerBase<SojEvent> generateSojEventWatermark() {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(Constants.TOPIC_PRODUCER_EVENT, Constants.BOOTSTRAP_SERVERS_SESSION_QA,
            Constants.GROUP_ID_SESSION_QA, SojEvent.class)
        .setStartFromEarliest()
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractorForEventLoad(Time.seconds(10)));
  }

  public static FlinkKafkaConsumerBase<SojSession> generateSojSessionWatermark() {
    return KafkaConnectorFactoryForLoad
        .createKafkaConsumer(Constants.TOPIC_PRODUCER_SESSION,
            Constants.BOOTSTRAP_SERVERS_SESSION_QA,
            Constants.GROUP_ID_SESSION_QA, SojSession.class)
        .setStartFromEarliest()
        .assignTimestampsAndWatermarks(
            new SojBoundedOutOfOrdernessTimestampExtractorForSessionLoad(Time.seconds(10)));
  }
}
