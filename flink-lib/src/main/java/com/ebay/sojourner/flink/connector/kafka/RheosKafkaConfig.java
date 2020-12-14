package com.ebay.sojourner.flink.connector.kafka;

import com.ebay.sojourner.common.util.Property;
import io.ebay.rheos.kafka.client.StreamConnectorConfig;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RheosKafkaConfig implements Serializable {
  private String rheosServiceUrls;
  private String topic;
  private String subject;
  private String producerId;
  private Properties kafkaProps;

  public Map<String, Object> toProducerConfigMap() {
    Map<String, Object> map = new HashMap<>((Map) kafkaProps);
    map.put(Property.PRODUCER_ID, producerId);
    map.put(StreamConnectorConfig.RHEOS_SERVICES_URLS, rheosServiceUrls);
    return map;
  }
}
