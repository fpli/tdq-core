package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.common.model.SojBytes;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerKafkaCopyJob {

  public static void main(String[] args) throws Exception {
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        StreamExecutionEnvironment.getExecutionEnvironment();
    executionEnvironment.getConfig().setGlobalJobParameters(parameterTool);
    executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

    // checkpoint settings
    executionEnvironment.enableCheckpointing(
        AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds() * 1000,
        CheckpointingMode.EXACTLY_ONCE);
    executionEnvironment
        .getCheckpointConfig()
        .setCheckpointTimeout(
            AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMinPauseBetweenCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMaxConcurrentCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent() == null
                ? 1
                : AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());
    executionEnvironment.setStateBackend(
        StateBackendFactory.getStateBackend(StateBackendFactory.FS));
    executionEnvironment.setRestartStrategy(
        RestartStrategies.fixedDelayRestart(
            3, // number of restart attempts
            org.apache.flink.api.common.time.Time.of(10, TimeUnit.SECONDS) // delay
        ));

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    /*
    DataStream<byte[]> bytesDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_RNO, Constants.GROUP_ID_RNO, byte[].class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For RNO")
            .uid("kafkaSourceForRNO");

    DataStream<byte[]> bytesDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_SLC, Constants.GROUP_ID_SLC, byte[].class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For SLC")
            .uid("kafkaSourceForSLC");

    DataStream<byte[]> bytesDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_LVS, Constants.GROUP_ID_LVS, byte[].class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 100
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For LVS")
            .uid("kafkaSourceForLVS");
            */

    DataStream<SojBytes> bytesDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.generateWatermark(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_QA, Constants.GROUP_ID_QA, SojBytes.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 2
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For Sojourner QA");



    // bytesDataStream.print();
    executionEnvironment.execute();
  }
}
