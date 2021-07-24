package com.ebay.tdq.utils;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.rules.TdqErrorMsg;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.rules.TdqSampleData;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Slf4j
@Getter
public class TdqContext implements Serializable {

  private TdqEnv tdqEnv;

  private final Map<Long, OutputTag<TdqMetric>> outputTagMap = new HashMap<>();
  private final OutputTag<TdqErrorMsg> exceptionOutputTag;
  private final OutputTag<TdqSampleData> sampleOutputTag;
  private final OutputTag<TdqSampleData> debugOutputTag;
  private final OutputTag<TdqMetric> eventLatencyOutputTag;

  public TdqContext(String[] args) {
    load(args);
    this.tdqEnv = new TdqEnv();
    this.exceptionOutputTag = new OutputTag<>("tdq-exception", TypeInformation.of(TdqErrorMsg.class));
    this.sampleOutputTag = new OutputTag<>("tdq-sample", TypeInformation.of(TdqSampleData.class));
    this.debugOutputTag = new OutputTag<>("tdq-debug", TypeInformation.of(TdqSampleData.class));
    this.eventLatencyOutputTag = new OutputTag<>("tdq-event-latency", TypeInformation.of(TdqMetric.class));
    for (Long seconds : tdqEnv.getWinTags()) {
      outputTagMap.put(seconds,
          new OutputTag<>(String.valueOf(seconds), TypeInformation.of(TdqMetric.class)));
    }
  }

  private static void load(String[] args) {
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    String profile = parameterTool.get(EnvironmentUtils.PROFILE);
    if (StringUtils.isNotBlank(profile)) {
      EnvironmentUtils.activateProfile(profile);
    }
    EnvironmentUtils.fromProperties(parameterTool.getProperties());
  }
}
