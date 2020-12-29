package com.ebay.sojourner.flink.connector.kafka;

public interface KafkaDeserializer<T> {

  String decodeKey(byte[] data);

  T decodeValue(byte[] data);
}
