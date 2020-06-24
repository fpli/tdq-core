package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;

import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaProducerFactory;
import com.ebay.sojourner.flink.connectors.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.operators.session.AgentIpFilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerRTSampleDataCopyJob {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // kafka source for copy
    SourceDataStreamBuilder<IntermediateSession> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, IntermediateSession.class
    );

    DataStream<IntermediateSession> intermediateSessionDataStream = dataStreamBuilder
        .buildOfDC(RNO);

    DataStream<IntermediateSession> filterSession = intermediateSessionDataStream
        .filter(new AgentIpFilterFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
        .name("filter session")
        .uid("session-filter-id")
        .disableChaining();

    filterSession
        .addSink(KafkaProducerFactory.getProducer(
            FlinkEnvUtils.getString(Property.KAFKA_PRODUCER_TOPIC),
            FlinkEnvUtils.getListString(Property.KAFKA_PRODUCER_BOOTSTRAP_SERVERS_RNO),
            FlinkEnvUtils.getString(Property.BEHAVIOR_MESSAGE_KEY_SESSION),
            IntermediateSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .name("IntermediateSessionSampleData")
        .uid("intermediate-session-sample-sink-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.NAME_DATA_QUALITY));
  }
}
