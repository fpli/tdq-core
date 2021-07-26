package com.ebay.tdq.config;

import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import org.apache.commons.collections.MapUtils;

/**
 * @author juntzhang
 */
@Data
public class KafkaSourceConfig implements Serializable {

  private String name;
  private int parallelism;

  private String startupMode = "EARLIEST";
  private Long fromTimestamp = 0L;
  private Long toTimestamp = 0L;

  private Double sampleFraction = 0d;
  private Long outOfOrderlessMs;
  private Long idleTimeoutMs;

  private List<String> topics;
  private String deserializer;
  private Properties kafkaConsumer;

  public static KafkaSourceConfig build(SourceConfig config) {
    KafkaSourceConfig ksc = new KafkaSourceConfig();
    ksc.setName(config.getName());
    Map<String, Object> props = config.getConfig();
    if (MapUtils.isEmpty(props)) {
      throw new IllegalArgumentException("realtime.kafka config is empty!");
    }
    ksc.setParallelism((int) props.get("rhs-parallelism"));
    ksc.setOutOfOrderlessMs(DateUtils.toMillis((String) props.get("rhs-out-of-orderless")));
    ksc.setIdleTimeoutMs(DateUtils.toMillis((String) props.get("rhs-idle-timeout")));
    ksc.setSampleFraction((double) props.getOrDefault("sample-fraction", 0d));
    ksc.setStartupMode((String) props.get("startup-mode"));
    if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      ksc.setFromTimestamp((long) props.getOrDefault("from-timestamp", 0L));
    }
    ksc.setToTimestamp((long) props.getOrDefault("to-timestamp", 0L));
    ksc.setTopics(Arrays.asList(((String) props.get("topics")).split(",")));
    ksc.setDeserializer((String) props.get("deserializer"));
    ksc.kafkaConsumer = new Properties();
    ksc.kafkaConsumer.putAll((Map<?, ?>) props.get("kafka-consumer"));
    return ksc;
  }

  public Long getEndOfStreamTimestamp() {
    if (this.toTimestamp > 0) {
      return this.toTimestamp + this.outOfOrderlessMs;
    }
    return 0L;
  }
}
