package com.ebay.sojourner.flink.connectors.kafka;

import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.model.JetStreamOutputSession;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojBytesEvent;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.schema.AvroKeyedDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.AvroKeyedSerializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.RawEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojBytesEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojBytesEventSerializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojEventDeserializationSchema;
import com.ebay.sojourner.flink.connectors.kafka.schema.SojSessionDeserializationSchema;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;

public class KafkaConnectorFactory {

  private static Properties initCommonConfig() {
    Properties props = new Properties();
    props.put("sasl.mechanism", "IAF");
    props.put("security.protocol", "SASL_PLAINTEXT");

    final String saslJaasConfig =
        String.format(
            "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId="
                + "\"urn:ebay-marketplace-consumerid:68a97ac2-013b-4915-9ed7-d6ae2ff01618\" "
                + "iafSecret=\"%s\" iafEnv=\"%s\";",
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_SECRET),
            FlinkEnvUtils.getString(Property.RHEOS_CLIENT_IAF_ENV));

    props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
    return props;
  }

  public static <T> FlinkKafkaConsumer createKafkaConsumer(String topic, String brokers,
      String groupId, Class<T> tClass) {
    Properties consumerConfig = initCommonConfig();
    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
        FlinkEnvUtils.getInteger(Property.MAX_POLL_RECORDS));
    consumerConfig
        .put(ConsumerConfig.RECEIVE_BUFFER_CONFIG,
            FlinkEnvUtils.getInteger(Property.RECEIVE_BUFFER));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
        FlinkEnvUtils.getInteger(Property.FETCH_MAX_BYTES));
    consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,
        FlinkEnvUtils.getInteger(Property.FETCH_MAX_WAIT_MS));

    consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
        FlinkEnvUtils.getInteger(Property.MAX_PARTITIONS_FETCH_BYTES));

    consumerConfig.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        FlinkEnvUtils.getString(Property.AUTO_RESET_OFFSET));

    if (tClass.isAssignableFrom(RawEvent.class)) {
      return new FlinkKafkaConsumer<>(
          topic, new RawEventDeserializationSchema(), consumerConfig);
    } else if (tClass.isAssignableFrom(SojBytesEvent.class)) {
      return new FlinkKafkaConsumer<>(
          topic, new SojBytesEventDeserializationSchema(), consumerConfig);
    } else if (tClass.isAssignableFrom(JetStreamOutputEvent.class)) {
      return new FlinkKafkaConsumer<>(
          topic, new SojEventDeserializationSchema(), consumerConfig);
    } else if (tClass.isAssignableFrom(JetStreamOutputSession.class)) {
      return new FlinkKafkaConsumer<>(
          topic, new SojSessionDeserializationSchema(), consumerConfig);
    } else {
      return new FlinkKafkaConsumer<>(
          topic, new AvroKeyedDeserializationSchema<>(tClass), consumerConfig);
    }
  }

  public static <T> FlinkKafkaProducer createKafkaProducer(String topic, String brokers,
      Class<T> sinkClass, String messageKey) {
    Properties producerConfig = initCommonConfig();
    producerConfig
        .put(ProducerConfig.BATCH_SIZE_CONFIG, FlinkEnvUtils.getInteger(Property.BATCH_SIZE));
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

    return new FlinkKafkaProducer<>(topic,
        new AvroKeyedSerializationSchema<>(sinkClass, messageKey), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }

  public static FlinkKafkaProducer createKafkaProducerForCopy(String topic, String brokers) {
    Properties producerConfig = initCommonConfig();
    producerConfig
        .put(ProducerConfig.BATCH_SIZE_CONFIG, FlinkEnvUtils.getInteger(Property.BATCH_SIZE));
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
        FlinkEnvUtils.getInteger(Property.REQUEST_TIMEOUT_MS));

    return new FlinkKafkaProducer<>(topic,
        new SojBytesEventSerializationSchema<>(), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }
}
