package com.ebay.tdq.utils;

import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.config.TransformationConfig;
import com.ebay.tdq.expressions.Expression;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.TdqMetric;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
@Slf4j
public class FlinkEnvFactory {
  public static StreamExecutionEnvironment create(String[] args, boolean local) {
    StreamExecutionEnvironment env;
    if (local) {
      env = createLocal();
    } else {
      env = FlinkEnvUtils.prepare(args);
    }
    log.warn("===success==");
    return env;
  }

  public static StreamExecutionEnvironment createLocal() {
    Configuration conf = new Configuration();
    conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
    conf.setInteger(TaskManagerOptions.NUM_TASK_SLOTS, 16);
    final StreamExecutionEnvironment env =
        StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
    env.setParallelism(1);
    env.registerTypeWithKryoSerializer(Expression.class, JavaSerializer.class);
    env.registerTypeWithKryoSerializer(PhysicalPlan.class, JavaSerializer.class);
    env.registerTypeWithKryoSerializer(TdqMetric.class, JavaSerializer.class);
    env.registerTypeWithKryoSerializer(TransformationConfig.class, JavaSerializer.class);
    env.registerTypeWithKryoSerializer(TdqConfig.class, JavaSerializer.class);
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    env.setRestartStrategy(RestartStrategies.noRestart());
    return env;
  }
}
