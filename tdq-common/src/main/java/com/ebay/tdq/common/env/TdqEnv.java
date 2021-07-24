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
 * todo need classify by application.yml
 *
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  private String jobName;
  private String profile;
  private boolean local;

  private final KafkaSourceEnv kafkaSourceEnv;
  private final JdbcEnv jdbcEnv;
  private final ProntoEnv prontoEnv;
  private final LocalCacheEnv localCacheEnv;
  private final SinkEnv sinkEnv;
  private List<Long> winTags;

  private int metric1stAggrParallelism;
  private int metric2ndAggrParallelism;

  private String metric1stAggrW;
  private Long metric1stAggrWMilli;

  public TdqEnv() {
    this.local = EnvironmentUtils.getBooleanOrDefault("flink.app.local", false);
    this.profile = EnvironmentUtils.get("flink.app.profile");
    this.jobName = EnvironmentUtils.get(Property.FLINK_APP_NAME) + "-" + getProfile();

    this.kafkaSourceEnv = new KafkaSourceEnv();
    this.localCacheEnv = new LocalCacheEnv();
    this.sinkEnv = new SinkEnv();
    this.prontoEnv = new ProntoEnv();

    this.metric1stAggrParallelism = EnvironmentUtils.getInteger("flink.app.parallelism.metric-1st-aggr");
    this.metric2ndAggrParallelism = EnvironmentUtils.getInteger("flink.app.parallelism.metric-2nd-aggr");
    this.metric1stAggrW = EnvironmentUtils.get("flink.app.window.metric-1st-aggr");
    this.metric1stAggrWMilli = DateUtils.toSeconds(EnvironmentUtils.get("flink.app.window.metric-1st-aggr"));

    this.winTags = EnvironmentUtils.getStringList("flink.app.window.supports", ",")
        .stream().map(DateUtils::toSeconds)
        .collect(Collectors.toList());

    this.jdbcEnv = new JdbcEnv();
    // checkstyle.off: Regexp
    System.out.println(this.toString());
    // checkstyle.on: Regexp
    log.warn(this.toString());
  }
}
