package com.ebay.tdq.config;

import com.ebay.tdq.common.env.TdqEnv;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class KafkaSinkConfig implements Serializable {

  private String topic;
  private String producerId;
  private int parallelism;
  private String schemaSubject;
  private String rheosServicesUrls;
  private Properties kafkaProducer;

  public static KafkaSinkConfig build(SinkConfig config, TdqEnv tdqEnv) {
    KafkaSinkConfig ksc = new KafkaSinkConfig();
    Map<String, Object> props = config.getConfig();
    Validate.isTrue(MapUtils.isNotEmpty(props), "realtime.kafka sink config is empty!");
    ksc.setRheosServicesUrls((String) props.getOrDefault("rheos-services-urls", ""));
    ksc.setSchemaSubject((String) props.getOrDefault("schema-subject", ""));
    ksc.setTopic((String) props.get("topic"));
    if (props.get("producer-id") != null) {
      ksc.setProducerId((String) props.get("producer-id"));
    } else {
      ksc.setProducerId(tdqEnv.getJobName());
    }
    ksc.setParallelism((int) props.getOrDefault("rhs-parallelism", 3));
    ksc.kafkaProducer = new Properties();
    if (props.get("kafka-producer") != null) {
      ksc.kafkaProducer.putAll((Map<?, ?>) props.get("kafka-producer"));
    }
    return ksc;
  }
}
