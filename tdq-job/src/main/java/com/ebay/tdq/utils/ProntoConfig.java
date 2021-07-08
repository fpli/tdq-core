package com.ebay.tdq.utils;

import java.io.Serializable;
import lombok.Data;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

/**
 * @author juntzhang
 */
@Data
public class ProntoConfig implements Serializable {
  private String indexPattern;
  private String latencyIndexPattern;
  private String schema;
  private String hostname;
  private int port;
  private String username;
  private String password;

  public ProntoConfig() {
    indexPattern        = getString("flink.app.source.pronto.index-pattern");
    latencyIndexPattern = getString("flink.app.source.pronto.latency-index-pattern");
    schema              = getString("flink.app.source.pronto.scheme");
    hostname            = getString("flink.app.source.pronto.hostname");
    port                = getInteger("flink.app.source.pronto.port");
    username            = getString("flink.app.source.pronto.api-key");
    password            = getString("flink.app.source.pronto.api-value");
  }
}
