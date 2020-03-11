package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.RoundRobinAssignor;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

public class KafkaConnectorFactoryForSLC {

  public static String GROUP_ID = AppEnv.config().getKafka().getGroupId();
  public static String TOPIC_PATHFINDER_EVENTS = AppEnv.config().getKafka().getTopic();
  public static String BOOTSTRAP_SERVERS =
      Arrays.asList(
          "rhs-mwsvkiaa-kfk-slc-1.rheos-streaming-prod.svc.45.tess.io:9092",
          "rhs-mwsvkiaa-kfk-slc-2.rheos-streaming-prod.svc.45.tess.io:9092",
          "rhs-mwsvkiaa-kfk-slc-3.rheos-streaming-prod.svc.45.tess.io:9092",
          "rhs-mwsvkiaa-kfk-slc-4.rheos-streaming-prod.svc.45.tess.io:9092",
          "rhs-mwsvkiaa-kfk-slc-5.rheos-streaming-prod.svc.45.tess.io:9092")
          .stream().collect(Collectors.joining(","));

  public static FlinkKafkaConsumer<RawEvent> createKafkaConsumer() {

    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3000);
    props.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, 8 * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 250 * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);

    props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 8 * 1024 * 1024);

    props.put(
        ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RheosEventDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

    return new FlinkKafkaConsumer<>(
        TOPIC_PATHFINDER_EVENTS, new RawEventDeserializationSchema(), props);
  }

  public static FlinkKafkaProducer<SojEvent> createKafkaProducer() {
    return null;
  }
}
