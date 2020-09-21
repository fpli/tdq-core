package com.ebay.sojourner.flink.connector.kafka;

import java.util.List;

public interface RheosKafkaSerializer<T> {

  byte[] encodeKey(T data, List<String> keyList);

  byte[] encodeData(T data);

}
