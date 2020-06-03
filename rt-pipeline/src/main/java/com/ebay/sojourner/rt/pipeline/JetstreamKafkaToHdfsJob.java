package com.ebay.sojourner.rt.pipeline;

import com.ebay.sojourner.common.model.JetStreamOutputEvent;
import com.ebay.sojourner.common.util.Constants;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class JetstreamKafkaToHdfsJob {

  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    String botSourceTopic = FlinkEnvUtils.getString("dump.bot.source.topic");
    String nonBotSourceTopic = FlinkEnvUtils.getString("dump.non.bot.source.topic");
    Class<?> deserializeClass = Class.forName(FlinkEnvUtils.getString("dump.source.class"));
    String hdfsPath = FlinkEnvUtils.getString("dump.hdfs.path");
    String botGroupId = FlinkEnvUtils.getString("dump.bot.group.id");
    String nonBotGroupId = FlinkEnvUtils.getString("dump.non.bot.group.id");
    int botSourceParallelNum = FlinkEnvUtils.getInteger("dump.bot.source.parallel.number");
    int nonBotSourceParallelNum = FlinkEnvUtils.getInteger("dump.non.bot.source.parallel.number");
    int sinkParallelNum = FlinkEnvUtils.getInteger("dump.sink.parallel.number");
    String botBootstrapServers = FlinkEnvUtils.getString("dump.bot.bootstrap.servers");
    String nonBotBootstrapServers = FlinkEnvUtils.getString("dump.non.bot.bootstrap.servers");

    // bot event kafka source
    DataStream<JetStreamOutputEvent> jetStreamOutputBotEventDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(botSourceTopic, botBootstrapServers, botGroupId, deserializeClass))
            .setParallelism(botSourceParallelNum)
            .name(String.format("Rheos Kafka Consumer from topic: %s", botSourceTopic))
            .uid("bot-source-id");

    // non bot event kafka source
    DataStream<JetStreamOutputEvent> jetStreamOutputNonBotEventDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(nonBotSourceTopic, nonBotBootstrapServers, nonBotGroupId,
                    deserializeClass))
            .setParallelism(nonBotSourceParallelNum)
            .name(String.format("Rheos Kafka Consumer from topic: %s", nonBotSourceTopic))
            .uid("non-bot-source-id");

    // union bot and non bot
    DataStream<JetStreamOutputEvent> jetStreamOutputDataStream =
        jetStreamOutputBotEventDataStream.union(jetStreamOutputNonBotEventDataStream);

    // hdfs sink
    jetStreamOutputDataStream
        .addSink(HdfsConnectorFactory.createWithParquet(hdfsPath, deserializeClass))
        .setParallelism(sinkParallelNum)
        .name(String.format("Hdfs sink to location: %s", hdfsPath))
        .uid("event-sink-id");

    FlinkEnvUtils.execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_HDFS_DUMP));
  }
}
