package com.ebay.tdq.utils;

import com.ebay.sojourner.common.util.Property;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.OutputTag;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getSet;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getStringOrDefault;
import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_FLUSH_TIMEOUT;
import static com.ebay.tdq.utils.TdqConstant.LOCAL_COMBINE_QUEUE_SIZE;
import static com.ebay.tdq.utils.TdqConstant.METRIC_1ST_AGGR_PARTITIONS;

/**
 * @author juntzhang
 */
@Data
@Slf4j
public class TdqEnv implements Serializable {
  private final JdbcConfig jdbcConfig;
  private Map<Long, OutputTag<TdqMetric>> outputTagMap = new HashMap<>();
  private int localCombineFlushTimeout;
  private int localCombineQueueSize;
  private int metric1stAggrPartitions;
  private String jobName;
  private boolean debug;
  private Set<String> sinkTypes;
  private OutputTag<TdqErrorMsg> exceptionOutputTag;
  private OutputTag<TdqSampleData> sampleOutputTag;

  public TdqEnv() {
    this.debug = Boolean.parseBoolean(getStringOrDefault("flink.app.debug", "false").toLowerCase());

    this.jdbcConfig               = new JdbcConfig();
    this.localCombineFlushTimeout = LOCAL_COMBINE_FLUSH_TIMEOUT;
    this.localCombineQueueSize    = LOCAL_COMBINE_QUEUE_SIZE;
    this.metric1stAggrPartitions  = METRIC_1ST_AGGR_PARTITIONS;
    this.jobName                  = getString(Property.FLINK_APP_NAME);
    this.sinkTypes                = getSet("flink.app.sink.types");
    this.exceptionOutputTag       = new OutputTag<>("tdq-exception", TypeInformation.of(TdqErrorMsg.class));
    this.sampleOutputTag          = new OutputTag<>("tdq-sample", TypeInformation.of(TdqSampleData.class));

    for (String tag : getString("flink.app.window.supports").split(",")) {
      Long seconds = DateUtils.toSeconds(tag);
      outputTagMap.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }

    try {
      // checkstyle.off: Regexp
      System.out.println(JsonUtils.toJSONString(this));
      // checkstyle.on: Regexp
      log.warn(JsonUtils.toJSONString(this));
    } catch (JsonProcessingException e) {
      log.warn(e.getMessage(), e);
    }
  }
}
