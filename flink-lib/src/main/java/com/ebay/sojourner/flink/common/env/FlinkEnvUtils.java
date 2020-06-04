package com.ebay.sojourner.flink.common.env;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.flink.common.state.StateBackendFactory;
import com.ebay.sojourner.flink.common.util.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkEnvUtils {

  private static Map<String, String> config = new HashMap<>();

  private static void load(String[] args) {
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    String profile = parameterTool.get("profile");
    if (StringUtils.isNotBlank(profile)) {
      config.put("profile", profile);
      EnvironmentUtils.activateProfile(profile);
    }
    EnvironmentUtils.fromProperties(parameterTool.getProperties());
  }

  public static StreamExecutionEnvironment prepare(String[] args) throws Exception {

    FlinkEnvUtils.load(args);
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.getConfig().disableSysoutLogging();
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    /**
     * checkpoint
     */
    env.enableCheckpointing(FlinkEnvUtils
        .getInteger(Constants.CHECKPOINT_INTERVAL_MS)); // create a checkpoint every 5 minutes
    CheckpointConfig checkpointConf = env.getCheckpointConfig();
    checkpointConf.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

    checkpointConf.setMinPauseBetweenCheckpoints(
        FlinkEnvUtils.getInteger(Constants.CHECKPOINT_MIN_PAUSE_BETWEEN_MS));//2min

    checkpointConf.setCheckpointTimeout(
        FlinkEnvUtils.getInteger(Constants.CHECKPOINT_TIMEOUT_MS));//15min

    checkpointConf.setMaxConcurrentCheckpoints(
        FlinkEnvUtils.getInteger(Constants.CHECKPOINT_MAX_CONCURRENT));

    checkpointConf
        .enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

    /**
     * StateBackend
     */
    env.setStateBackend(StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));

    return env;
  }

  public static void execute(StreamExecutionEnvironment env, String jobName) throws Exception {

    ParameterTool parameterTool = ParameterTool.fromMap(config);
    // make parameters available in the web interface
    env.getConfig().setGlobalJobParameters(parameterTool);
    env.execute(jobName);
  }

  public static String getString(String key) {

    String value = EnvironmentUtils.get(key);
    config.put(key, value);
    return value;
  }

  public static Integer getInteger(String key) {

    Integer value = EnvironmentUtils.get(key, Integer.class);
    config.put(key, String.valueOf(value));
    return value;
  }

  public static String getListString(String key) {

    List<String> list = EnvironmentUtils.get(key, List.class);
    String value = String.join(",", list);
    config.put(key, value);
    return value;
  }
}
