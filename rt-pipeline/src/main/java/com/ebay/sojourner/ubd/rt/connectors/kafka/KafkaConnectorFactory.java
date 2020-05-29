package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.ubd.common.model.JetStreamOutputSession;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic;
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
            AppEnv.config().getRheos().getIaf().getSecret(),
            AppEnv.config().getRheos().getIaf().getEnv());

    props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
    return props;
  }

  public static <T> FlinkKafkaConsumer createKafkaConsumer(String topic, String brokers,
      String groupId, Class<T> tClass) {
    Properties consumerConfig = initCommonConfig();
    consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    consumerConfig.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3000);
    consumerConfig.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 8 * 1024 * 1024);
    consumerConfig.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 250 * 1024 * 1024);
    consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);

    consumerConfig.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 8 * 1024 * 1024);

    consumerConfig.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    consumerConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

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
    producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, 4 * 1024);
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

    return new FlinkKafkaProducer<>(topic,
        new AvroKeyedSerializationSchema<>(sinkClass, messageKey), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }

  public static FlinkKafkaProducer createKafkaProducerForCopy(String topic, String brokers) {
    Properties producerConfig = initCommonConfig();
    producerConfig.put(ProducerConfig.BATCH_SIZE_CONFIG, 4 * 1024);
    producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    producerConfig.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);

    return new FlinkKafkaProducer<>(topic,
        new SojBytesEventSerializationSchema<>(), producerConfig,
        Optional.of(new SojKafkaPartitioner<>()));
  }
}
