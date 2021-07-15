package com.ebay.tdq.utils;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getSet;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.util.Property;
import com.ebay.tdq.config.ProntoConfig;
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
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {

  public static final String NORMAL_METRIC = "normal-metric";
  public static final String LATENCY_METRIC = "latency-metric";
  public static final String DEBUG_LOG = "debug-log";
  public static final String SAMPLE_LOG = "sample-log";
  public static final String EXCEPTION_LOG = "exception-log";

  private String jobName;
  private String profile;
  private final JdbcConfig jdbcConfig;
  private final ProntoConfig prontoConfig;
  private final HdfsConfig hdfsConfig;
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
    this.profile = getString("flink.app.profile");
    this.jobName = getString(Property.FLINK_APP_NAME) + "-" + getProfile();

    this.localCombineFlushTimeout = getInteger("flink.app.advance.local-combine.flush-timeout");
    this.localCombineQueueSize = getInteger("flink.app.advance.local-combine.queue-size");
    this.metric1stAggrPartitions = getInteger("flink.app.parallelism.metric-1st-aggr-partitions");
    this.metric1stAggrParallelism = getInteger("flink.app.parallelism.metric-1st-aggr");
    this.metric2ndAggrParallelism = getInteger("flink.app.parallelism.metric-2nd-aggr");
    this.metric1stAggrW = getString("flink.app.window.metric-1st-aggr");
    this.metric1stAggrWMilli = DateUtils.toSeconds(getString("flink.app.window.metric-1st-aggr"));
    this.srcSampleFraction = Double.valueOf(getString("flink.app.source.sample-fraction"));

    this.sinkTypes.put(NORMAL_METRIC, getSet("flink.app.sink.types." + NORMAL_METRIC));
    this.sinkTypes.put(LATENCY_METRIC, getSet("flink.app.sink.types." + LATENCY_METRIC));
    this.sinkTypes.put(DEBUG_LOG, getSet("flink.app.sink.types." + DEBUG_LOG));
    this.sinkTypes.put(SAMPLE_LOG, getSet("flink.app.sink.types." + SAMPLE_LOG));
    this.sinkTypes.put(EXCEPTION_LOG, getSet("flink.app.sink.types." + EXCEPTION_LOG));

    this.exceptionOutputTag = new OutputTag<>("tdq-exception", TypeInformation.of(TdqErrorMsg.class));
    this.sampleOutputTag = new OutputTag<>("tdq-sample", TypeInformation.of(TdqSampleData.class));
    this.debugOutputTag = new OutputTag<>("tdq-debug", TypeInformation.of(TdqSampleData.class));
    this.eventLatencyOutputTag = new OutputTag<>("tdq-event-latency", TypeInformation.of(TdqMetric.class));

    for (String tag : getString("flink.app.window.supports").split(",")) {
      Long seconds = DateUtils.toSeconds(tag);
      outputTagMap.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }
    this.prontoConfig = new ProntoConfig();
    this.jdbcConfig = new JdbcConfig();
    this.hdfsConfig = new HdfsConfig();
    // checkstyle.off: Regexp
    System.out.println(this.toString());
    // checkstyle.on: Regexp
    log.warn(this.toString());
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
        .add("jdbcConfig=" + jdbcConfig)
        .add("prontoConfig=" + prontoConfig)
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
