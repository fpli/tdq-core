package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;

public class KafkaConsumerFactory {

  private static FlinkKafkaConsumer flinkKafkaConsumer;

  public static <T> FlinkKafkaConsumer<T> getConsumer(KafkaConsumerConfig config, Class<T> tClass) {

    Properties consumerConfig = KafkaConnectorFactory.getKafkaCommonConfig();

    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBrokers());
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroupId());

    consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
        FlinkEnvUtils.getInteger(Property.MAX_POLL_RECORDS));
    consumerConfig.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG,
        FlinkEnvUtils.getInteger(Property.RECEIVE_BUFFER));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
        FlinkEnvUtils.getInteger(Property.FETCH_MAX_BYTES));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
        FlinkEnvUtils.getInteger(Property.FETCH_MAX_WAIT_MS));
    consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
        FlinkEnvUtils.getInteger(Property.MAX_PARTITIONS_FETCH_BYTES));
    consumerConfig.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
        RoundRobinAssignor.class.getName());
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        FlinkEnvUtils.getString(Property.AUTO_RESET_OFFSET));

    if (tClass.isAssignableFrom(SojEvent.class)
        || tClass.isAssignableFrom(SojSession.class)
        || tClass.isAssignableFrom(BotSignature.class)
        || tClass.isAssignableFrom(IntermediateSession.class)) {

      flinkKafkaConsumer = new FlinkKafkaConsumer(
          config.getTopic(),
          DeserializationSchemaManager.getKeyedSchema(tClass),
          consumerConfig
      );

    } else {
      flinkKafkaConsumer = new FlinkKafkaConsumer(
          config.getTopic(),
          DeserializationSchemaManager.getSchema(tClass),
          consumerConfig
      );
    }

    if (config.getTopic().contains("dq")) {
      // 2020-06-29 21:10:00 Beijing
      flinkKafkaConsumer.setStartFromEarliest();
      // flinkKafkaConsumer.setStartFromTimestamp(1593436200000L);
    } else {
      flinkKafkaConsumer.setStartFromLatest();
      // flinkKafkaConsumer.setStartFromTimestamp(1599374683000L);
    }

    return flinkKafkaConsumer;

  }
}
