package com.ebay.sojourner.flink.connectors.kafka.schema;

import java.util.List;

public interface RheosKafkaSerializer<T> {

  byte[] encodeKey(T data, List<String> keyList);

  byte[] encodeData(T data);

}
