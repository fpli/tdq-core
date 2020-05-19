package com.ebay.sojourner.ubd.rt.util;

import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ExecutionEnvUtil {

  public static ParameterTool createParameterTool(final String[] args) throws Exception {

    ParameterTool parameterTool =
        ParameterTool.fromPropertiesFile(
            ExecutionEnvUtil.class.getResourceAsStream(
                Constants.DEFAULT_APPLICATION_PROPERTIES_FILENAME))
            .mergeWith(ParameterTool.fromArgs(args))
            .mergeWith(ParameterTool.fromSystemProperties())
            .mergeWith(ParameterTool.fromMap(getenv()));

    parameterTool.getProperties().list(System.out);

    return parameterTool;
  }

  private static ParameterTool createParameterTool() {

    try {
      return ParameterTool.fromPropertiesFile(
          ExecutionEnvUtil.class.getResourceAsStream(
              Constants.DEFAULT_APPLICATION_PROPERTIES_FILENAME))
          .mergeWith(ParameterTool.fromSystemProperties())
          .mergeWith(ParameterTool.fromMap(getenv()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static StreamExecutionEnvironment prepare(ParameterTool parameterTool) throws
      Exception {

    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.getConfig().disableSysoutLogging();

    env.getConfig()
        .setGlobalJobParameters(parameterTool); // make parameters available in the web interface
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    /**
     * checkpoint
     */
    env.enableCheckpointing(AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds()
        * 1000); // create a checkpoint every 5 minutes
    CheckpointConfig checkpointConf = env.getCheckpointConfig();
    checkpointConf.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

    checkpointConf.setMinPauseBetweenCheckpoints(
        AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);//2min

    checkpointConf.setCheckpointTimeout(
        AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);//15min

    checkpointConf
        .setMaxConcurrentCheckpoints(AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());

    checkpointConf
        .enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

    /**
     * StateBackend
     */
    env.setStateBackend(StateBackendFactory.getStateBackend(StateBackendFactory.FS));

    return env;
  }

  private static Map<String, String> getenv() {
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }
}
