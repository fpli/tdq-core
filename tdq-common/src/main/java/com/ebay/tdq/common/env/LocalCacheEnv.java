package com.ebay.tdq.common.env;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import java.io.Serializable;
import lombok.Data;
import scala.concurrent.duration.Duration;

/**
 * @author juntzhang
 */
@Data
public class LocalCacheEnv implements Serializable {

  private long localCombineFlushTimeout;
  private int localCombineQueueSize;
  private int outputPartitions;

  public LocalCacheEnv() {
    this.localCombineFlushTimeout =
        Duration.apply(EnvironmentUtils.getStringOrDefault(
            "flink.app.local-cache.flush-timeout", "5min")).toMillis();
    this.localCombineQueueSize = EnvironmentUtils.getIntegerOrDefault(
        "flink.app.local-cache.queue-size", 5000);
    this.outputPartitions = EnvironmentUtils.getIntegerOrDefault(
        "flink.app.local-cache.output-partitions", 57);
  }
}
