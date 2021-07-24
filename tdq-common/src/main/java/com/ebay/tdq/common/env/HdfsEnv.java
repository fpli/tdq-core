package com.ebay.tdq.common.env;


import static com.ebay.tdq.common.env.TdqConstant.DEBUG_LOG;
import static com.ebay.tdq.common.env.TdqConstant.EXCEPTION_LOG;
import static com.ebay.tdq.common.env.TdqConstant.LATENCY_METRIC;
import static com.ebay.tdq.common.env.TdqConstant.NORMAL_METRIC;
import static com.ebay.tdq.common.env.TdqConstant.SAMPLE_LOG;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import java.io.Serializable;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class HdfsEnv implements Serializable {

  private String normalMetricPath;
  private String latencyMetricPath;
  private String debugLogPath;
  private String sampleLogPath;
  private String exceptionLogPath;
  private String rawDataPath;
  private int rawDataParallelism;

  public HdfsEnv() {
    this.normalMetricPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + NORMAL_METRIC);
    this.latencyMetricPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + LATENCY_METRIC);
    this.debugLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + DEBUG_LOG);
    this.sampleLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + SAMPLE_LOG);
    this.exceptionLogPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + EXCEPTION_LOG);
    this.rawDataPath = EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs.raw-data");
    this.rawDataParallelism = Integer
        .parseInt(EnvironmentUtils.getStringOrDefault("flink.app.parallelism.hdfs.raw-data", "50"));
  }
}
