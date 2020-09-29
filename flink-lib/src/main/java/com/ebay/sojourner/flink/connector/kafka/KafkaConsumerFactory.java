package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.model.BotSignature;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import java.util.Properties;
import java.util.Set;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;

public class KafkaConsumerFactory {

  public static <T> FlinkKafkaConsumer<T> getConsumer(KafkaConsumerConfig config, Class<T> tClass) {

    return buildFlinkKafkaConsumer(config, tClass);
  }

  public static <T> FlinkKafkaConsumer<T> getConsumer(KafkaConsumerConfig config, Class<T> tClass,
      Set<String> guidSet) {

    return buildFlinkKafkaConsumer(config, tClass, guidSet);
  }

  private static <T> FlinkKafkaConsumer<T> buildFlinkKafkaConsumer(
      KafkaConsumerConfig config, Class<T> tClass) {

    FlinkKafkaConsumer flinkKafkaConsumer;
    Properties commonConfig = buildKafkaCommonConfig(config);

    if (tClass.isAssignableFrom(BotSignature.class)) {

      flinkKafkaConsumer = new FlinkKafkaConsumer<>(
          config.getTopicList(),
          DeserializationSchemaManager.getKeyedSchema(tClass),
          commonConfig);
    } else {

      flinkKafkaConsumer = new FlinkKafkaConsumer<>(
          config.getTopicList(),
          DeserializationSchemaManager.getSchema(tClass),
          commonConfig);
    }

    flinkKafkaConsumer.setStartFromLatest();
    return flinkKafkaConsumer;
  }

  private static <T> FlinkKafkaConsumer<T> buildFlinkKafkaConsumer(
      KafkaConsumerConfig config, Class<T> tClass, Set<String> guidSet) {

    FlinkKafkaConsumer flinkKafkaConsumer;
    Properties commonConfig = buildKafkaCommonConfig(config);

    if (tClass.isAssignableFrom(RawEvent.class)) {

      flinkKafkaConsumer = new FlinkKafkaConsumer<>(
          config.getTopicList(),
          DeserializationSchemaManager.getSchema(tClass, guidSet),
          commonConfig);
    } else {

      flinkKafkaConsumer = new FlinkKafkaConsumer<>(
          config.getTopicList(),
          DeserializationSchemaManager.getSchema(tClass),
          commonConfig);
    }

    flinkKafkaConsumer.setStartFromLatest();
    return flinkKafkaConsumer;
  }

  private static Properties buildKafkaCommonConfig(KafkaConsumerConfig config) {

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
    return consumerConfig;
  }
}
