package com.ebay.tdq.config;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class KafkaSourceConfig implements Serializable {

  private String name;
  private int parallelism;
  private String rheosServicesUrls;
  private String eventTimeField;
  private String schemaSubject;

  private String startupMode = "EARLIEST";
  private Long fromTimestamp = 0L;
  private Long toTimestamp = 0L;

  private Double sampleFraction = 0d;
  private Long outOfOrderlessMs;
  private Long idleTimeoutMs;

  private List<String> topics;
  private String deserializer;
  private Properties kafkaConsumer;

  public static KafkaSourceConfig build(SourceConfig config, TdqEnv tdqEnv) {
    KafkaSourceConfig ksc = new KafkaSourceConfig();
    ksc.setName(config.getName());
    Map<String, Object> props = config.getConfig();
    Validate.isTrue(MapUtils.isNotEmpty(props), "realtime.kafka source config is empty!");
    ksc.setRheosServicesUrls((String) props.getOrDefault("rheos-services-urls", ""));
    ksc.setEventTimeField((String) props.getOrDefault("event-time-field", ""));
    ksc.setSchemaSubject((String) props.getOrDefault("schema-subject", ""));

    ksc.setParallelism((int) props.getOrDefault("rhs-parallelism", -1));
    ksc.setOutOfOrderlessMs(DateUtils.toMillis((String) props.getOrDefault("rhs-out-of-orderless", "3min")));
    ksc.setIdleTimeoutMs(DateUtils.toMillis((String) props.getOrDefault("rhs-idle-timeout", "3min")));
    ksc.setSampleFraction((double) props.getOrDefault("sample-fraction", 0d));
    ksc.setStartupMode((String) props.getOrDefault("startup-mode", "LATEST"));
    if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      ksc.setFromTimestamp(((Number) props.getOrDefault("from-timestamp", 0L)).longValue());
    }
    ksc.setToTimestamp(((Number) props.getOrDefault("to-timestamp", 0L)).longValue());
    ksc.setTopics(Arrays.asList(((String) props.getOrDefault("topics", "")).split(",")));
    ksc.setDeserializer((String) props.getOrDefault("deserializer", ""));

    ksc.kafkaConsumer = new Properties();
    if (props.get("kafka-consumer") != null) {
      ksc.kafkaConsumer.putAll((Map<?, ?>) props.get("kafka-consumer"));
      ksc.kafkaConsumer.put("group.id", tdqEnv.getJobName());
    }
    Validate.isTrue(StringUtils.isNotBlank(ksc.getKafkaConsumer().getProperty("group.id")), "group.is must be unique!");
    log.info(ksc.toString());
    return ksc;
  }

  public Long getEndOfStreamTimestamp() {
    if (this.toTimestamp > 0) {
      return this.toTimestamp + this.outOfOrderlessMs;
    }
    return 0L;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("name", name)
        .append("parallelism", parallelism)
        .append("startupMode", startupMode)
        .append("fromTimestamp", fromTimestamp)
        .append("toTimestamp", toTimestamp)
        .append("sampleFraction", sampleFraction)
        .append("outOfOrderlessMs", outOfOrderlessMs)
        .append("idleTimeoutMs", idleTimeoutMs)
        .append("topics", topics)
        .append("deserializer", deserializer)
        .append("kafkaConsumer", kafkaConsumer)
        .toString();
  }
}
