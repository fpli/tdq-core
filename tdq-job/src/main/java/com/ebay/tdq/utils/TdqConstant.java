package com.ebay.tdq.utils;

import com.ebay.tdq.rules.TdqMetric;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.OutputTag;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getSet;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getStringOrDefault;

/**
 * @author juntzhang
 */
public class TdqConstant {
  public static final Map<Long, OutputTag<TdqMetric>> OUTPUT_TAG_MAP = new HashMap<>();
  public static Integer PARALLELISM_METRIC_COLLECTOR_BY_WINDOW =
      getInteger("flink.app.parallelism.metric-collector-by-window");
  public static Integer LOCAL_COMBINE_QUEUE_SIZE = getInteger("flink.app.advance.local-combine.queue-size");
  public static Integer LOCAL_COMBINE_FLUSH_TIMEOUT = getInteger("flink.app.advance.local-combine.flush-timeout");
  public static Integer PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR =
      getInteger("flink.app.parallelism.metric-final-collector");
  public static Long WINDOW_METRIC_COLLECTOR_BY_WINDOW =
      DateUtils.toSeconds(getString("flink.app.window.metric-collector-by-window"));
  public static Set<String> SINK_TYPES = getSet("flink.app.sink.types");

  // source
  public static String PRONTO_INDEX_PATTERN = getString("flink.app.source.pronto.index-pattern");
  public static String PRONTO_LATENCY_INDEX_PATTERN = getString("flink.app.source.pronto.latency-index-pattern");
  public static String PRONTO_SCHEME = getString("flink.app.source.pronto.scheme");
  public static String PRONTO_HOSTNAME = getString("flink.app.source.pronto.hostname");
  public static Integer PRONTO_PORT = getInteger("flink.app.source.pronto.port");
  public static String PRONTO_USERNAME = getString("flink.app.source.pronto.username");
  public static String PRONTO_PASSWORD = getString("flink.app.source.pronto.password");

  public static Double SRC_SAMPLE_FRACTION = Double.valueOf(
      getStringOrDefault("flink.app.source.sample-fraction", "0")
  );

  static {
    for (String tag : getString("flink.app.window.supports")
        .split(",")) {
      Long seconds = DateUtils.toSeconds(tag);
      OUTPUT_TAG_MAP.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }

  }

}
