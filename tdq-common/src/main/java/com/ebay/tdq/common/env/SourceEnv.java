package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.getStringOrDefault;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.utils.DateUtils;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author juntzhang
 */
@Setter
@Getter
public class SourceEnv implements Serializable {

  protected final Long outOfOrderless;
  protected final Long idleTimeout;
  protected final Double srcSampleFraction;

  public SourceEnv() {
    this.outOfOrderless = DateUtils.toSeconds(getStringOrDefault(
        "flink.app.source.out-of-orderless", "3min"));
    this.idleTimeout = DateUtils.toSeconds(
        getStringOrDefault("flink.app.source.idle-timeout", "3min"));
    this.srcSampleFraction = EnvironmentUtils.getDoubleOrDefault(
        "flink.app.source.sample-fraction", 0d);

  }
}
