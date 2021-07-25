package com.ebay.tdq.common.env;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  private final KafkaSourceEnv kafkaSourceEnv;
  private final JdbcEnv jdbcEnv;
  private final ProntoEnv prontoEnv;
  private final LocalCacheEnv localCacheEnv;
  private final SinkEnv sinkEnv;
  private String jobName;
  private String profile;
  private boolean local;
  private List<Long> winTags;

  private int metric1stAggrParallelism;
  private int metric2ndAggrParallelism;

  private String metric1stAggrW;

  public TdqEnv() {
    this.local = EnvironmentUtils.getBooleanOrDefault("flink.app.local", false);
    this.profile = EnvironmentUtils.get("flink.app.profile");
    this.jobName = EnvironmentUtils.getStringOrDefault(Property.FLINK_APP_NAME, "tdq") + "-" + getProfile();

    this.kafkaSourceEnv = new KafkaSourceEnv();
    this.localCacheEnv = new LocalCacheEnv();
    this.sinkEnv = new SinkEnv();
    this.prontoEnv = new ProntoEnv();

    this.metric1stAggrParallelism = EnvironmentUtils.getIntegerOrDefault("flink.app.parallelism.metric-1st-aggr", 100);
    this.metric2ndAggrParallelism = EnvironmentUtils.getIntegerOrDefault("flink.app.parallelism.metric-2nd-aggr", 61);
    this.metric1stAggrW = EnvironmentUtils.getStringOrDefault("flink.app.window.metric-1st-aggr", "1min");

    if (EnvironmentUtils.contains("flink.app.window.supports")) {
      this.winTags = EnvironmentUtils.getStringList("flink.app.window.supports", ",")
          .stream().map(DateUtils::toSeconds)
          .collect(Collectors.toList());
    }

    this.jdbcEnv = new JdbcEnv();
    // checkstyle.off: Regexp
    System.out.println(this.toString());
    // checkstyle.on: Regexp
    log.warn(this.toString());
  }
}
