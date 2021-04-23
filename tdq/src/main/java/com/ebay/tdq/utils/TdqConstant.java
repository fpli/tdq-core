package com.ebay.tdq.utils;

import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.util.DateUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.OutputTag;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

/**
 * @author juntzhang
 */
public class TdqConstant {
  public static final Map<Long, OutputTag<TdqMetric>> OUTPUT_TAG_MAP =
      new HashMap<>();
  public static Integer PARALLELISM_METRIC_NORMALIZER =
      getInteger("flink.app.parallelism.metric-normalizer");
  public static Integer PARALLELISM_METRIC_PRE_COLLECTOR =
      getInteger("flink.app.parallelism.metric-pre-collector");
  public static Integer PARALLELISM_METRIC_COLLECTOR_BY_WINDOW =
      getInteger("flink.app.parallelism.metric-collector-by-window");
  public static Integer PARALLELISM_METRIC_METRIC_FINAL_COLLECTOR =
      getInteger("flink.app.parallelism.metric-final-collector");
  public static Long WINDOW_METRIC_PRE_COLLECTOR =
      DateUtils.toSeconds(getString("flink.app.window.metric-pre-collector"));
  public static Long WINDOW_METRIC_COLLECTOR_BY_WINDOW =
      DateUtils.toSeconds(getString("flink.app.window.metric-collector-by-window"));

  static {
    for (String tag : getString("flink.app.window.supports")
        .split(",")) {
      Long seconds = DateUtils.toSeconds(tag);
      OUTPUT_TAG_MAP.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }

  }

}
