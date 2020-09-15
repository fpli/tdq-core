package com.ebay.sojourner.flink.connector.kafka;

import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TransactionalProducer {

  public static void main(String[] args) {
    Properties producerProps = new Properties();
    producerProps.put("bootstrap.servers", "localhost:9092");
    producerProps.put("enable.idempotence", "true");
    producerProps.put("transactional.id", "prod-1");
    producerProps.put(KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    producerProps.put(VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer");
    KafkaProducer<String, String> producer = new KafkaProducer(producerProps);
    producer.initTransactions();
    try {
      producer.beginTransaction();
      producer.send(new ProducerRecord<>("test", "1"));
      Thread.sleep(10 * 60 * 1000);
      producer.commitTransaction();
    } catch (Exception e) {
      e.printStackTrace();
      producer.abortTransaction();
    }
  }
}
