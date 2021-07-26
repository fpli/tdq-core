package com.ebay.tdq.common.env;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import scala.concurrent.duration.Duration;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  private final JdbcEnv jdbcEnv;
  private final ProntoEnv prontoEnv;
  private final SinkEnv sinkEnv;
  private String jobName;
  private String profile;
  private boolean local;
  private List<Long> winTags;

  private int metric1stAggrParallelism;
  private int metric2ndAggrParallelism;
  private String metric1stAggrWindow;
  private long localCombineFlushTimeout;
  private int localCombineQueueSize;
  private int outputPartitions;

  private Long fromTimestamp = 0L;
  private Long toTimestamp = 0L;

  public TdqEnv() {
    this.jobName = EnvironmentUtils.get("flink.app.name");
    this.local = EnvironmentUtils.getBooleanOrDefault("flink.app.local", false);
    this.profile = EnvironmentUtils.get("flink.app.profile");
    this.sinkEnv = new SinkEnv();
    this.prontoEnv = new ProntoEnv();

    this.localCombineFlushTimeout = Duration
        .apply(EnvironmentUtils.getStringOrDefault("flink.app.local-cache.flush-timeout", "5min")).toMillis();
    this.localCombineQueueSize = EnvironmentUtils.getIntegerOrDefault("flink.app.local-cache.queue-size", 5000);
    this.outputPartitions = EnvironmentUtils.getIntegerOrDefault("flink.app.local-cache.output-partitions", 57);
    this.metric1stAggrParallelism = EnvironmentUtils.getIntegerOrDefault("flink.app.parallelism.metric-1st-aggr", 100);
    this.metric2ndAggrParallelism = EnvironmentUtils.getIntegerOrDefault("flink.app.parallelism.metric-2nd-aggr", 61);
    this.metric1stAggrWindow = EnvironmentUtils.getStringOrDefault("flink.app.window.metric-1st-aggr", "1min");

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

  public void setFromTimestamp(Long fromTimestamp) {
    if (fromTimestamp != 0) {
      this.fromTimestamp = Math.max(fromTimestamp, this.fromTimestamp);
    }
  }

  public void setToTimestamp(Long toTimestamp) {
    if (toTimestamp != 0) {
      if (this.toTimestamp == 0) {
        this.toTimestamp = toTimestamp;
      } else {
        this.toTimestamp = Math.min(toTimestamp, this.toTimestamp);
      }
    }
  }

  public boolean isProcessElement(long t) {
    return (this.fromTimestamp == 0 || (this.fromTimestamp > 0 && t >= this.fromTimestamp)) &&
        (this.toTimestamp == 0 || (this.toTimestamp > 0 && t <= this.toTimestamp));
  }

  public boolean isNotProcessElement(long t) {
    return !isProcessElement(t);
  }
}
