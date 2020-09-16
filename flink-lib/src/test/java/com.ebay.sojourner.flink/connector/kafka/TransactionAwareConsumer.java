package com.ebay.sojourner.flink.connector.kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TransactionAwareConsumer {

  public static void main(String[] args) {
    Properties consumerProps = new Properties();
    consumerProps.put("bootstrap.servers", "localhost:9092");
    consumerProps.put("group.id", "my-group-id");
    consumerProps.put("auto.offset.reset", "earliest");
    consumerProps.put("enable.auto.commit", "false");
    consumerProps.put("isolation.level", "read_uncommitted");
    // consumerProps.put("isolation.level", "read_committed");
    consumerProps.put(KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
    consumer.subscribe(Collections.singleton("test"));
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3).toMillis());
  }
}
