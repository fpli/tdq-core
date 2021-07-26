package com.ebay.tdq.config;

import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class MemorySourceConfig implements Serializable {

  private String name;
  private int parallelism;
  private Long outOfOrderlessMs;
  private Long idleTimeoutMs;

  public static MemorySourceConfig build(SourceConfig config) {
    MemorySourceConfig msc = new MemorySourceConfig();
    msc.setName(config.getName());
    Map<String, Object> props = config.getConfig();
    if (props == null) {
      props = new HashMap<>();
    }
    msc.setParallelism((int) props.getOrDefault("rhs-parallelism", 1));
    msc.setOutOfOrderlessMs(
        DateUtils.toMillis((String) props.getOrDefault("rhs-out-of-orderless", "1min")));
    msc.setIdleTimeoutMs(
        DateUtils.toMillis((String) props.getOrDefault("rhs-idle-timeout", "1min")));
    return msc;
  }
}
