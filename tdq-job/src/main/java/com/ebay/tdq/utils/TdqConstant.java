package com.ebay.tdq.utils;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

/**
 * use TdqEnv
 * @author juntzhang
 */
@Deprecated
public class TdqConstant {
  public static Integer METRIC_1ST_AGGR_PARALLELISM = getInteger("flink.app.parallelism.metric-1st-aggr");
  public static Integer METRIC_1ST_AGGR_PARTITIONS = getInteger("flink.app.parallelism.metric-1st-aggr-partitions");
  public static Integer LOCAL_COMBINE_QUEUE_SIZE = getInteger("flink.app.advance.local-combine.queue-size");
  public static Integer LOCAL_COMBINE_FLUSH_TIMEOUT = getInteger("flink.app.advance.local-combine.flush-timeout");
  public static Integer METRIC_2ND_AGGR_PARALLELISM = getInteger("flink.app.parallelism.metric-2nd-aggr");
  public static String METRIC_1ST_AGGR_W = getString("flink.app.window.metric-1st-aggr");
  public static Long METRIC_1ST_AGGR_W_MILLI = DateUtils.toSeconds(METRIC_1ST_AGGR_W);

  // source
  public static String PRONTO_INDEX_PATTERN = getString("flink.app.source.pronto.index-pattern");
  public static String PRONTO_LATENCY_INDEX_PATTERN = getString("flink.app.source.pronto.latency-index-pattern");
  public static String PRONTO_SCHEME = getString("flink.app.source.pronto.scheme");
  public static String PRONTO_HOSTNAME = getString("flink.app.source.pronto.hostname");
  public static Integer PRONTO_PORT = getInteger("flink.app.source.pronto.port");
  public static String PRONTO_USERNAME = getString("flink.app.source.pronto.username");
  public static String PRONTO_PASSWORD = getString("flink.app.source.pronto.password");

  public static Double SRC_SAMPLE_FRACTION = Double.valueOf(getString("flink.app.source.sample-fraction"));

}
