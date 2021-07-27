package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.getStringWithPattern;

import java.io.Serializable;
import java.util.TimeZone;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * need config in tdq Config
 *
 * @author juntzhang
 */
@Slf4j
@Getter
@Deprecated
public class SinkEnv implements Serializable {

  private final TimeZone timeZone;

  private final String normalMetricPath;
  private final String normalMetricProntoIndexPattern;
  private final String normalMetricStdName;
  private final String normalMetricMemName;

  private final String latencyMetricPath;
  private final String latencyMetricProntoIndexPattern;
  private final String latencyMetricStdName;

  private final String sampleLogProntoIndexPattern;
  private final String sampleLogStdName;

  private final String exceptionLogProntoIndexPattern;
  private final String exceptionLogStdName;

  private final String rawDataPath;

  public SinkEnv() {
    this.timeZone = TimeZone.getTimeZone("MST"); // ZoneId.of("-7")

    this.normalMetricPath = getStringWithPattern("flink.app.sink.normal-metric.hdfs-path");
    this.normalMetricProntoIndexPattern = getStringWithPattern(
        "flink.app.sink.normal-metric.pronto-index-pattern");
    this.normalMetricStdName = getStringWithPattern(
        "flink.app.sink.normal-metric.std-name");
    this.normalMetricMemName = getStringWithPattern(
        "flink.app.sink.normal-metric.mem-name");

    this.latencyMetricPath = getStringWithPattern("flink.app.sink.latency-metric.hdfs-path");
    this.latencyMetricProntoIndexPattern = getStringWithPattern(
        "flink.app.sink.latency-metric.pronto-index-pattern");
    this.latencyMetricStdName = getStringWithPattern(
        "flink.app.sink.latency-metric.std-name");

    this.sampleLogProntoIndexPattern = getStringWithPattern(
        "flink.app.sink.sample-log.pronto-index-pattern");
    this.sampleLogStdName = getStringWithPattern(
        "flink.app.sink.sample-log.std-name");

    this.exceptionLogProntoIndexPattern = getStringWithPattern(
        "flink.app.sink.exception-log.pronto-index-pattern");
    this.exceptionLogStdName = getStringWithPattern(
        "flink.app.sink.exception-log.std-name");

    this.rawDataPath = getStringWithPattern("flink.app.sink.raw-data.hdfs-path");
  }

  public boolean isNormalMetricSinkStd() {
    return StringUtils.isNotBlank(normalMetricStdName);
  }

  public boolean isNormalMetricSinkPronto() {
    return StringUtils.isNotBlank(normalMetricProntoIndexPattern);
  }

  public boolean isNormalMetricSinkHdfs() {
    return StringUtils.isNotBlank(normalMetricPath);
  }

  public boolean isNormalMetricSinkMemory() {
    return StringUtils.isNotBlank(normalMetricMemName);
  }

  public boolean isLatencyMetricSinkStd() {
    return StringUtils.isNotBlank(latencyMetricStdName);
  }

  public boolean isLatencyMetricSinkPronto() {
    return StringUtils.isNotBlank(latencyMetricProntoIndexPattern);
  }

  public boolean isLatencyMetricSinkHdfs() {
    return StringUtils.isNotBlank(latencyMetricPath);
  }

  public boolean isSampleLogSinkStd() {
    return StringUtils.isNotBlank(sampleLogStdName);
  }

  public boolean isSampleLogSinkPronto() {
    return StringUtils.isNotBlank(sampleLogProntoIndexPattern);
  }

  public boolean isExceptionLogSinkStd() {
    return StringUtils.isNotBlank(exceptionLogStdName);
  }

  public boolean isExceptionLogSinkPronto() {
    return StringUtils.isNotBlank(exceptionLogProntoIndexPattern);
  }

  public String getIndexDateSuffix(long ts) {
    return FastDateFormat.getInstance("yyyy-MM-dd", timeZone).format(ts);
  }

  public String getNormalMetricIndex(Long eventTime) {
    return normalMetricProntoIndexPattern + getIndexDateSuffix(eventTime);
  }

}
