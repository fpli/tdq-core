package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JetstreamSojEventDataDump {

  public static void main(String[] args) throws Exception {
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ParameterTool.fromArgs(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        ExecutionEnvUtil.prepare(parameterTool);

    // bot event kafka source
    DataStream<JetStreamOutputEvent> jetStreamOutputBotEventDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_JETSTREAM_BOT_EVENT,
                Constants.BOOTSTRAP_SERVERS_JETSTREAM_SESSION, Constants.GROUP_ID_BOT_EVENT,
                JetStreamOutputEvent.class))
            .setParallelism(30)
            .name("Rheos Kafka Consumer For Jetstrem bot event")
            .uid("jetstreamBotEvent");

    // non bot event kafka source
    DataStream<JetStreamOutputEvent> jetStreamOutputNonBotEventDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_JETSTREAM_NON_BOT_EVENT,
                Constants.BOOTSTRAP_SERVERS_JETSTREAM_EVENT, Constants.GROUP_ID_NON_BOT_EVENT,
                JetStreamOutputEvent.class))
            .setParallelism(150)
            .name("Rheos Kafka Consumer For Jetstrem non bot event")
            .uid("jetstreamNonBotEvent");

    // union bot and non bot
    DataStream<JetStreamOutputEvent> jetStreamOutputEventDataStream =
        jetStreamOutputBotEventDataStream.union(jetStreamOutputNonBotEventDataStream);
    // sink hdfs
    jetStreamOutputEventDataStream
        .addSink(HdfsSinkUtil.jetstreamSojEventSinkWithParquet())
        .setParallelism(75)
        .name("jetstream sojevent sink")
        .uid("jetstreamEventSink")
        .disableChaining();

    // submit job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForFullPipeline());
  }
}
