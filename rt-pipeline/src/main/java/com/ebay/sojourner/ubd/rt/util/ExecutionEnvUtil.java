package com.ebay.sojourner.ubd.rt.util;

import com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * Created 2019-08-01 10:11
 *
 * @author : Unikal Liu
 * @version : 1.0.0
 */
public class ExecutionEnvUtil {
  public static final ParameterTool PARAMETER_TOOL = createParameterTool();

  public static ParameterTool createParameterTool(final String[] args) throws Exception {

    ParameterTool parameterTool =
        ParameterTool.fromPropertiesFile(
                ExecutionEnvUtil.class.getResourceAsStream(
                    PropertiesConstants.DEFAULT_APPLICATION_PROPERTIES_FILENAME))
            .mergeWith(ParameterTool.fromArgs(args))
            .mergeWith(ParameterTool.fromSystemProperties())
            .mergeWith(ParameterTool.fromMap(getenv()));

    // Checking input parameters
    //        final String propertiesFilename = PropertiesConstants.APPLICATION_PROPERTIES_FILENAME;
    //        if (parameterTool.has(propertiesFilename)) {
    //            parameterTool = ParameterTool
    //                    .fromPropertiesFile(parameterTool.get(propertiesFilename))
    //                    .mergeWith(ParameterTool.fromArgs(args))
    //                    .mergeWith(ParameterTool.fromSystemProperties())
    //                    .mergeWith(ParameterTool.fromMap(getenv()));
    //        }

    parameterTool.getProperties().list(System.out);

    return parameterTool;
  }

  private static ParameterTool createParameterTool() {
    try {
      return ParameterTool.fromPropertiesFile(
              ExecutionEnvUtil.class.getResourceAsStream(
                  PropertiesConstants.DEFAULT_APPLICATION_PROPERTIES_FILENAME))
          .mergeWith(ParameterTool.fromSystemProperties())
          .mergeWith(ParameterTool.fromMap(getenv()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  //    public static StreamExecutionEnvironment prepare( ParameterTool parameterTool) throws
  // Exception {
  //        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
  //        env.setParallelism(parameterTool.getInt(PropertiesConstants.STREAM_PARALLELISM, 5));
  //        env.getConfig().disableSysoutLogging();
  //
  //        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in
  // the web interface
  //        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
  //
  //
  //        /**
  //         * checkpoint
  //         */
  //        if (parameterTool.getBoolean(PropertiesConstants.STREAM_CHECKPOINT_ENABLE, true)) {
  //
  // env.enableCheckpointing(parameterTool.getInt(PropertiesConstants.STREAM_CHECKPOINT_INTERVAL,
  // 60000)); // create a checkpoint every 60 seconds
  //            CheckpointConfig checkpointConf = env.getCheckpointConfig();
  //            checkpointConf.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
  //
  // checkpointConf.setMinPauseBetweenCheckpoints(
  // parameterTool.getLong(PropertiesConstants.STREAM_CHECKPOINT_MINPAUSE, 30000L));//30s
  //
  // checkpointConf.setCheckpointTimeout(
  // parameterTool.getLong(PropertiesConstants.STREAM_CHECKPOINT_TIMEOUT, 10000L));//10s
  //
  // checkpointConf.enableExternalizedCheckpoints(
  // CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
  //
  //        }
  //        /**
  //         * StateBackend
  //         */
  //        env.setStateBackend((StateBackend) new
  // FsStateBackend(parameterTool.get(CHECKPOINT_PATH)));
  //
  //
  //        /**
  //         * restart
  //         */
  //        env.getConfig().setRestartStrategy(
  //
  // RestartStrategies.fixedDelayRestart(
  // parameterTool.getInt(
  // PropertiesConstants.STREAM_RESTARTSTRATEGIES_ATTEMPTS, 10), // number of restart attempts
  //
  // org.apache.flink.api.common.time.Time.of(
  // parameterTool.getInt(
  // PropertiesConstants.STREAM_RESTARTSTRATEGIES_DELAYINTERVAL, 30), TimeUnit.SECONDS) // delay
  //                ));
  //
  //
  //        return env;
  //    }

  private static Map<String, String> getenv() {
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }
}
