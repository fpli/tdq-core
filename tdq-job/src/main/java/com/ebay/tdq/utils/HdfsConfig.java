package com.ebay.tdq.utils;

import static com.ebay.tdq.utils.TdqEnv.DEBUG_LOG;
import static com.ebay.tdq.utils.TdqEnv.EXCEPTION_LOG;
import static com.ebay.tdq.utils.TdqEnv.LATENCY_METRIC;
import static com.ebay.tdq.utils.TdqEnv.NORMAL_METRIC;
import static com.ebay.tdq.utils.TdqEnv.SAMPLE_LOG;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import java.io.Serializable;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class HdfsConfig implements Serializable {

  private String normalMetricPath;
  private String latencyMetricPath;
  private String debugLogPath;
  private String sampleLogPath;
  private String exceptionLogPath;

  public HdfsConfig() {
    this.normalMetricPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + NORMAL_METRIC);
    this.latencyMetricPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + LATENCY_METRIC);
    this.debugLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + DEBUG_LOG);
    this.sampleLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + SAMPLE_LOG);
    this.exceptionLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + EXCEPTION_LOG);
  }
}
