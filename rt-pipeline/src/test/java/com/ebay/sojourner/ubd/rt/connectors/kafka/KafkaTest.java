package com.ebay.sojourner.ubd.rt.connectors.kafka;

import io.ebay.rheos.schema.avro.RheosEventDeserializer;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaTest {

  /**
   * <pre>
   * bin/zkServer.sh start
   * bin/kafka-server-start.sh config/server.properties
   * bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1
   *                     --topic test
   * bin/kafka-topics.sh --list --zookeeper localhost:2181
   * bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test
   * </pre>
   */
  public static void testLocalKafkaConsumer() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConnectorFactory.GROUP_ID);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("test"));
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf(
            "offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
      }
    }
  }

  public static void testRheosStagingKafkaConsumer() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConnectorFactory.BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConnectorFactory.GROUP_ID);
    //        props.put(ConsumerConfig.CLIENT_ID_CONFIG, KafkaConnectorFactory.CLIENT_ID);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RheosEventDeserializer.class.getName());
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList(KafkaConnectorFactory.TOPIC_PATHFINDER_EVENTS));
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf(
            "offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
      }
    }
  }

  public static void main(String[] args) {
    testLocalKafkaConsumer();
  }
}
