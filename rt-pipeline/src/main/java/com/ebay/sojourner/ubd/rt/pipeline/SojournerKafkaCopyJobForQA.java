package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.SojBytesEvent;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.operators.event.SojBytesEventFilterFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerKafkaCopyJobForQA {

  public static void main(String[] args) throws Exception {
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        ExecutionEnvUtil.prepare(parameterTool);

    DataStream<SojBytesEvent> bytesDataStream =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_QA, Constants.GROUP_ID_QA, SojBytesEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 2
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For Sojourner QA");

    DataStream<SojBytesEvent> bytesFilterDataStream = bytesDataStream
        .filter(new SojBytesEventFilterFunction())
        .setParallelism(AppEnv.config().getFlink().getApp().getSourceParallelism())
        .name("Bytes Filter");

    // sink for copy
    bytesFilterDataStream
        .addSink(KafkaConnectorFactory.createKafkaProducerForCopy(Constants.TOPIC_PRODUCER_COPY,
            Constants.BOOTSTRAP_SERVERS_COPY))
        .setParallelism(AppEnv.config().getFlink().app.getCopyKafkaParallelism())
        .name("Copy Data Sink");

    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForKafkaCopyPipeline());
  }
}
