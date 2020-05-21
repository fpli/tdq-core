package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.JetStreamOutputSession;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JetstreamSojSessionDataDump {

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
    DataStream<JetStreamOutputSession> jetStreamOutputBotSessionDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_JETSTREAM_BOT_SESSION,
                Constants.BOOTSTRAP_SERVERS_JETSTREAM_SESSION, Constants.GROUP_ID_BOT_SESSION,
                JetStreamOutputSession.class))
            .setParallelism(20)
            .name("Rheos Kafka Consumer For Jetstrem bot session")
            .uid("jetstreamBotSession");

    // non bot event kafka source
    DataStream<JetStreamOutputSession> jetStreamOutputNonBotSessionDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_JETSTREAM_NON_BOT_SESSION,
                Constants.BOOTSTRAP_SERVERS_JETSTREAM_SESSION, Constants.GROUP_ID_NON_BOT_SESSION,
                JetStreamOutputSession.class))
            .setParallelism(15)
            .name("Rheos Kafka Consumer For Jetstrem non bot session")
            .uid("jetstreamNonBotSession");

    // union bot and non bot
    DataStream<JetStreamOutputSession> jetStreamOutputSessionDataStream =
        jetStreamOutputBotSessionDataStream.union(jetStreamOutputNonBotSessionDataStream);

    // sink hdfs
    jetStreamOutputSessionDataStream
        .addSink(HdfsSinkUtil.jetstreamSojSessionSinkWithParquet())
        .setParallelism(50)
        .name("jetstream sojsession sink")
        .uid("jetstreamSessionSink")
        .disableChaining();

    // submit job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForFullPipeline());

  }
}
