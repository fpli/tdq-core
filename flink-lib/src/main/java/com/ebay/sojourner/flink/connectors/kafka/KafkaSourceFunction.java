package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.SojBytesEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import java.util.Properties;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.config.SaslConfigs;

public class KafkaSourceFunction {

  public static <T> FlinkKafkaConsumer<T> buildSource(KafkaConfig config, Class<T> tClass) {
    KafkaConsumerFactory<T> factory = new KafkaConsumerFactory<>();

    Properties consumerConfig = initCommonConfig();
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

    return factory.getConsumer(config.getTopic(), consumerConfig, tClass);
  }

  private static Properties initCommonConfig() {
    Properties props = new Properties();
    props.put("sasl.mechanism", "IAF");
    props.put("security.protocol", "SASL_PLAINTEXT");

    final String saslJaasConfig =
        String.format(
            "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId="
                + "\"%s\" iafSecret=\"%s\" iafEnv=\"%s\";",
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_ID),
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_SECRET),
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_ENV));

    props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
    return props;
  }


  public static <T> FlinkKafkaConsumerBase buildSource(String topic, String brokers,
      String groupId, Class<T> tClass) {

    FlinkKafkaConsumerBase<T> flinkKafkaConsumerBase
        = initKafkaConsumer(topic, brokers, groupId, tClass);

    if (tClass.isAssignableFrom(SojBytesEvent.class)) {
      return flinkKafkaConsumerBase;
    } else {
      return flinkKafkaConsumerBase
          .assignTimestampsAndWatermarks(
              new SojBoundedOutOfOrderlessTimestampExtractor(Time.seconds(10)));
    }
  }

  private static <T> FlinkKafkaConsumerBase initKafkaConsumer(String topic, String brokers,
      String groupId, Class<T> tClass) {

    FlinkKafkaConsumer kafkaConsumer = KafkaConnectorFactory
        .createKafkaConsumer(topic, brokers, groupId, tClass);
    if (topic.contains("dq")) {
      return kafkaConsumer.setStartFromEarliest();
    } else {
      return kafkaConsumer.setStartFromLatest();
    }
  }
}
