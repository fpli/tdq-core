package com.ebay.tdq.common.env;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import scala.concurrent.duration.Duration;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  private final TimeZone timeZone;
  private TdqConfig tdqConfig;

  private final JdbcEnv jdbcEnv;
  private final ProntoEnv prontoEnv;
  private String id;
  private String jobName;
  private String profile;
  private boolean local;
  private boolean noRestart;
  private int metric1stAggrParallelism;
  private int metric2ndAggrParallelism;
  private String metric1stAggrWindow;
  private List<Long> metric2ndAggrWindow;
  private long localCombineFlushTimeout;
  private int localCombineQueueSize;
  private int outputPartitions;

  private Long fromTimestamp = 0L;
  private Long toTimestamp = 0L;

  public TdqEnv() {
    this.timeZone = TimeZone.getTimeZone("MST"); // ZoneId.of("-7")

    this.jobName = EnvironmentUtils.get("flink.app.name");
    this.id = this.jobName + "." + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
    this.local = EnvironmentUtils.getBooleanOrDefault("flink.app.local", false);
    this.noRestart = EnvironmentUtils.getBooleanOrDefault("flink.app.noRestart", false);
    this.profile = EnvironmentUtils.get("flink.app.profile");
    this.prontoEnv = new ProntoEnv();
    this.jdbcEnv = new JdbcEnv();
  }

  public void setEnv(TdqConfig tdqConfig) {
    this.tdqConfig = tdqConfig;

    if (tdqConfig.getEnv() != null && MapUtils.isNotEmpty(tdqConfig.getEnv().getConfig())) {
      Map<String, Object> config = tdqConfig.getEnv().getConfig();
      this.localCombineFlushTimeout = Duration.apply(
          (String) config.get("flink.app.local-aggr.flush-timeout")).toMillis();
      this.localCombineQueueSize = (Integer) config.get("flink.app.local-aggr.queue-size");
      this.outputPartitions = (Integer) config.get("flink.app.local-aggr.output-partitions");
      this.metric1stAggrParallelism = (Integer) config.get("flink.app.parallelism.metric-1st-aggr");
      this.metric2ndAggrParallelism = (Integer) config.get("flink.app.parallelism.metric-2nd-aggr");
      this.metric1stAggrWindow = (String) config.get("flink.app.window.metric-1st-aggr");

      this.metric2ndAggrWindow = tdqConfig.getRules().stream()
          .map(r -> (String) r.getConfig().get("window"))
          .map(DateUtils::toSeconds)
          .collect(Collectors.toList());
    }

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
