package com.ebay.sojourner.flink.connector.kafka;

import java.util.List;

public interface KafkaSerializer<T> {

  byte[] encodeKey(T data, List<String> keyFields);

  byte[] encodeValue(T data);

}
