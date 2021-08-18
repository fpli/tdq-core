package com.ebay.tdq.utils;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.planner.utils.ConfigService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.OutputTag;

/**
 * @author juntzhang
 */
@Slf4j
@Getter
public class TdqContext implements Serializable {

  private final transient StreamExecutionEnvironment rhsEnv;
  private final TdqEnv tdqEnv;

  private final Map<Long, OutputTag<InternalMetric>> outputTagMap = new HashMap<>();
  private final OutputTag<TdqErrorMsg> exceptionOutputTag;
  private final OutputTag<TdqSampleData> sampleOutputTag;
  private final OutputTag<TdqSampleData> debugOutputTag;
  private final OutputTag<InternalMetric> eventLatencyOutputTag;

  public TdqContext(String[] args) throws Exception {
    load(args);
    this.tdqEnv = new TdqEnv();

    ConfigService.register(tdqEnv);
    TdqConfig tdqConfig = TdqConfigManager.getTdqConfig(tdqEnv);
    Validate.isTrue(tdqConfig != null);
    tdqEnv.setEnv(tdqConfig);

    this.exceptionOutputTag = new OutputTag<>("tdq-exception", TypeInformation.of(TdqErrorMsg.class));
    this.sampleOutputTag = new OutputTag<>("tdq-sample", TypeInformation.of(TdqSampleData.class));
    this.debugOutputTag = new OutputTag<>("tdq-debug", TypeInformation.of(TdqSampleData.class));
    this.eventLatencyOutputTag = new OutputTag<>("tdq-event-latency", TypeInformation.of(InternalMetric.class));
    if (tdqEnv.getMetric2ndAggrWindow() != null) {
      for (Long seconds : tdqEnv.getMetric2ndAggrWindow()) {
        outputTagMap.put(seconds,
            new OutputTag<>(String.valueOf(seconds), TypeInformation.of(InternalMetric.class)));
      }
    }

    this.rhsEnv = FlinkEnvFactory.create(this.getTdqEnv());
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
