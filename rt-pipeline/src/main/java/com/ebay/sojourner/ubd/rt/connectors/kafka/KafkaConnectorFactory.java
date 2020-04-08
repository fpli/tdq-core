package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import java.util.Optional;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;

public class KafkaConnectorFactory {

  public static FlinkKafkaConsumer<RawEvent> createKafkaConsumer(String topic, String brokers,
      String groupId) {

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
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3000);
    props.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 8 * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 250 * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);

    props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 8 * 1024 * 1024);

    props.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

    return new FlinkKafkaConsumer<>(
        topic, new RawEventDeserializationSchema(), props);
  }

  public static <T> FlinkKafkaProducer createKafkaProducer(String topic, String brokers,
      Class<T> sinkClass, String messageKey) {
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
    props.put(ProducerConfig.BATCH_SIZE_CONFIG,4 * 1024);
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);

    return new FlinkKafkaProducer<>(topic,
        new AvroKeyedSerializationSchema<>(sinkClass, messageKey), props,
        Optional.of(new SojKafkaPartitioner<>()));
  }
}
