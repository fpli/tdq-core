package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import java.util.Properties;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

public class KafkaConnectorFactoryForQA {

  public static final String GROUP_ID = AppEnv.config().getKafka().getGroupIdForQA();
  public static final String TOPIC_PATHFINDER_EVENTS = AppEnv.config().getKafka().getTopic();
  public static final String BOOTSTRAP_SERVERS =
      String.join(",", AppEnv.config().getKafka().getBootstrapServersForQA());

  public static FlinkKafkaConsumer<RawEvent> createKafkaConsumer() {
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

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

    props.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RheosEventDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

    return new FlinkKafkaConsumer<>(
        TOPIC_PATHFINDER_EVENTS, new RawEventDeserializationSchemaForRNO(), props);
  }

  public static FlinkKafkaProducer<SojEvent> createKafkaProducer() {
    return null;
  }
}