package com.ebay.tdq.utils;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getStringOrDefault;

import java.io.Serializable;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class HdfsConfig implements Serializable {

  private String normalMetricPath;

  public HdfsConfig() {
    this.normalMetricPath = getStringOrDefault("flink.app.source.hdfs.normal-metric",
        "hdfs://apollo-rno/user/b_bis/juntzhang/tdq/metric/");
  }
}
