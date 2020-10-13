package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import java.util.Properties;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;

public class KafkaConsumerFactory {

  public static <T> FlinkKafkaConsumer<T> getConsumer(DeserializationSchema<T> valueDeserializer,
                                                      KafkaConsumerConfig config) {

    Properties commonConfig = buildKafkaCommonConfig(config);
    FlinkKafkaConsumer<T> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        config.getTopicList(),
        valueDeserializer,
        commonConfig);

    flinkKafkaConsumer.setStartFromLatest();
    return flinkKafkaConsumer;
  }

  public static <T> FlinkKafkaConsumer<T> getConsumer(KafkaDeserializationSchema<T> deserializer,
                                                      KafkaConsumerConfig config) {

    Properties commonConfig = buildKafkaCommonConfig(config);
    FlinkKafkaConsumer<T> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        config.getTopicList(),
        deserializer,
        commonConfig);

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
