package com.ebay.tdq.utils;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;
import static com.ebay.sojourner.common.env.EnvironmentUtils.getInteger;
import static com.ebay.sojourner.common.env.EnvironmentUtils.getSet;
import static com.ebay.tdq.common.env.TdqConstant.DEBUG_LOG;
import static com.ebay.tdq.common.env.TdqConstant.EXCEPTION_LOG;
import static com.ebay.tdq.common.env.TdqConstant.LATENCY_METRIC;
import static com.ebay.tdq.common.env.TdqConstant.NORMAL_METRIC;
import static com.ebay.tdq.common.env.TdqConstant.SAMPLE_LOG;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.util.Property;
import com.ebay.tdq.common.env.HdfsEnv;
import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.KafkaSourceEnv;
import com.ebay.tdq.common.env.ProntoEnv;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  private String jobName;
  private String profile;
  private final KafkaSourceEnv kafkaSourceEnv;
  private final JdbcEnv jdbcEnv;
  private final ProntoEnv prontoEnv;
  private final HdfsEnv hdfsConfig;
  private int localCombineFlushTimeout;
  private int localCombineQueueSize;
  private int metric1stAggrPartitions;
  private int metric1stAggrParallelism;
  private int metric2ndAggrParallelism;
  private String metric1stAggrW;
  private Long metric1stAggrWMilli;
  private Double srcSampleFraction;

  private final Map<Long, OutputTag<TdqMetric>> outputTagMap = new HashMap<>();
  private final OutputTag<TdqErrorMsg> exceptionOutputTag;
  private final OutputTag<TdqSampleData> sampleOutputTag;
  private final OutputTag<TdqSampleData> debugOutputTag;
  private final OutputTag<TdqMetric> eventLatencyOutputTag;

  private Map<String, Set<String>> sinkTypes = new HashMap<>();

  public TdqEnv() {
    this(new String[]{});
  }

  public TdqEnv(String[] args) {
    load(args);
    this.profile = get("flink.app.profile");
    this.jobName = get(Property.FLINK_APP_NAME) + "-" + getProfile();

    this.kafkaSourceEnv = new KafkaSourceEnv();
    this.localCombineFlushTimeout = getInteger("flink.app.advance.local-combine.flush-timeout");
    this.localCombineQueueSize = getInteger("flink.app.advance.local-combine.queue-size");
    this.metric1stAggrPartitions = getInteger("flink.app.parallelism.metric-1st-aggr-partitions");
    this.metric1stAggrParallelism = getInteger("flink.app.parallelism.metric-1st-aggr");
    this.metric2ndAggrParallelism = getInteger("flink.app.parallelism.metric-2nd-aggr");
    this.metric1stAggrW = get("flink.app.window.metric-1st-aggr");
    this.metric1stAggrWMilli = DateUtils.toSeconds(get("flink.app.window.metric-1st-aggr"));
    this.srcSampleFraction = Double.valueOf(get("flink.app.source.sample-fraction"));

    this.sinkTypes.put(NORMAL_METRIC, getSet("flink.app.sink.types." + NORMAL_METRIC));
    this.sinkTypes.put(LATENCY_METRIC, getSet("flink.app.sink.types." + LATENCY_METRIC));
    this.sinkTypes.put(DEBUG_LOG, getSet("flink.app.sink.types." + DEBUG_LOG));
    this.sinkTypes.put(SAMPLE_LOG, getSet("flink.app.sink.types." + SAMPLE_LOG));
    this.sinkTypes.put(EXCEPTION_LOG, getSet("flink.app.sink.types." + EXCEPTION_LOG));

    this.exceptionOutputTag = new OutputTag<>("tdq-exception", TypeInformation.of(TdqErrorMsg.class));
    this.sampleOutputTag = new OutputTag<>("tdq-sample", TypeInformation.of(TdqSampleData.class));
    this.debugOutputTag = new OutputTag<>("tdq-debug", TypeInformation.of(TdqSampleData.class));
    this.eventLatencyOutputTag = new OutputTag<>("tdq-event-latency", TypeInformation.of(TdqMetric.class));

    for (String tag : get("flink.app.window.supports").split(",")) {
      Long seconds = DateUtils.toSeconds(tag);
      outputTagMap.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }
    this.prontoEnv = new ProntoEnv();
    this.jdbcEnv = new JdbcEnv();
    this.hdfsConfig = new HdfsEnv();
    // checkstyle.off: Regexp
    System.out.println(this.toString());
    // checkstyle.on: Regexp
    log.warn(this.toString());
  }

  public static void load(String[] args) {
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    String profile = parameterTool.get(EnvironmentUtils.PROFILE);
    if (StringUtils.isNotBlank(profile)) {
      EnvironmentUtils.activateProfile(profile);
    }
    EnvironmentUtils.fromProperties(parameterTool.getProperties());
    EnvironmentUtils.print();
  }

  public boolean isNormalMetricSink(String type) {
    return isSink(NORMAL_METRIC, type);
  }

  public boolean isLatencyMetricSink(String type) {
    return isSink(LATENCY_METRIC, type);
  }

  public boolean isDebugLogSink(String type) {
    return isSink(LATENCY_METRIC, type);
  }

  public boolean isSampleLogSink(String type) {
    return isSink(SAMPLE_LOG, type);
  }

  public boolean isExceptionLogSink(String type) {
    return isSink(EXCEPTION_LOG, type);
  }

  private boolean isSink(String scene, String type) {
    Set<String> set = this.getSinkTypes().get(scene);
    return set != null && set.contains(type);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", TdqEnv.class.getSimpleName() + "[", "]")
        .add("jobName='" + jobName + "'")
        .add("profile='" + profile + "'")
        .add("jdbcEnv=" + jdbcEnv)
        .add("prontoEnv=" + prontoEnv)
        .add("kafkaSourceEnv=" + kafkaSourceEnv)
        .add("localCombineFlushTimeout=" + localCombineFlushTimeout)
        .add("localCombineQueueSize=" + localCombineQueueSize)
        .add("metric1stAggrPartitions=" + metric1stAggrPartitions)
        .add("metric1stAggrParallelism=" + metric1stAggrParallelism)
        .add("metric2ndAggrParallelism=" + metric2ndAggrParallelism)
        .add("metric1stAggrW='" + metric1stAggrW + "'")
        .add("metric1stAggrWMilli=" + metric1stAggrWMilli)
        .add("srcSampleFraction=" + srcSampleFraction)
        .add("outputTagMap=" + outputTagMap)
        .add("sinkTypes=" + sinkTypes)
        .toString();
  }
}
