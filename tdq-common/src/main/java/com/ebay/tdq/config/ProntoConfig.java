package com.ebay.tdq.config;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;
import static com.ebay.sojourner.common.env.EnvironmentUtils.getInteger;
import static com.ebay.sojourner.common.env.EnvironmentUtils.getStringWithPattern;

import java.io.Serializable;
import java.util.StringJoiner;
import java.util.TimeZone;
import lombok.Data;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author juntzhang
 */
@Data
public class ProntoConfig implements Serializable {

  private String indexPattern;
  private String latencyIndexPattern;
  private String exceptionIndexPattern;
  private String sampleIndexPattern;
  private String debugIndexPattern;
  private String schema;
  private String hostname;
  private int port;
  private String username;
  private String password;

  public ProntoConfig() {
    this.indexPattern = getStringWithPattern("flink.app.source.pronto.index-pattern");
    this.latencyIndexPattern = getStringWithPattern("flink.app.source.pronto.latency-index-pattern");

    this.exceptionIndexPattern = getStringWithPattern("flink.app.source.pronto.exception-index-pattern");
    this.sampleIndexPattern = getStringWithPattern("flink.app.source.pronto.sample-index-pattern");
    this.debugIndexPattern = getStringWithPattern("flink.app.source.pronto.debug-index-pattern");
    this.schema = get("flink.app.source.pronto.scheme");
    this.hostname = get("flink.app.source.pronto.hostname");
    this.port = getInteger("flink.app.source.pronto.port");
    this.username = get("flink.app.source.pronto.api-key");
    this.password = get("flink.app.source.pronto.api-value");
  }

  public String getIndexDateSuffix(long ts) {
    return FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("MST")).format(ts);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ProntoConfig.class.getSimpleName() + "[", "]")
        .add("indexPattern='" + indexPattern + "'")
        .add("latencyIndexPattern='" + latencyIndexPattern + "'")
        .add("exceptionIndexPattern='" + exceptionIndexPattern + "'")
        .add("sampleIndexPattern='" + sampleIndexPattern + "'")
        .add("debugIndexPattern='" + debugIndexPattern + "'")
        .add("schema='" + schema + "'")
        .add("hostname='" + hostname + "'")
        .add("port=" + port)
        .add("username='" + username + "'")
        .add("password='******'")
        .toString();
  }
}
