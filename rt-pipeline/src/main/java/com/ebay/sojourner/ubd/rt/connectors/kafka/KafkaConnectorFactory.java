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

public class KafkaConnectorFactory {

  //    public static String CLIENT_ID = "82034abc-572d-4b71-82df-c9820ef1627c";
  public static String GROUP_ID = AppEnv.config().getKafka().getGroupId();
  public static String TOPIC_PATHFINDER_EVENTS = "behavior.pathfinder.events.total";
  public static String BOOTSTRAP_SERVERS =
      String.join(",", AppEnv.config().getKafka().getBootstrapServers());

  public static FlinkKafkaConsumer<RawEvent> createKafkaConsumer() {

    Properties props = new Properties();
    props.put("sasl.mechanism", "IAF");
    props.put("security.protocol", "SASL_PLAINTEXT");
    props.put(
        SaslConfigs.SASL_JAAS_CONFIG,
        "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId="
            + "\"urn:ebay-marketplace-consumerid:68a97ac2-013b-4915-9ed7-d6ae2ff01618\" "
            + "iafSecret=\"6218c197-200e-49d7-b404-2a4dbf7595ef\" iafEnv=\"staging\";");

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

    props.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RheosEventDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    //        props.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);

    return new FlinkKafkaConsumer<>(
        TOPIC_PATHFINDER_EVENTS, new RawEventDeserializationSchema(), props);
  }

  public static FlinkKafkaProducer<SojEvent> createKafkaProducer() {
    return null;
  }
}
