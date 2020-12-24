package com.ebay.sojourner.distributor.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SINK_DC;
import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_DC;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getLong;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.model.PageIdTopicMapping;
import com.ebay.sojourner.common.model.RawSojEventWrapper;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.distributor.broadcast.SojEventDistProcessFunction;
import com.ebay.sojourner.distributor.function.MappingSourceFunction;
import com.ebay.sojourner.distributor.schema.CustomSojEventSerializationSchema;
import com.ebay.sojourner.distributor.schema.SojEventDeserializationSchema;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.FlinkKafkaProducerFactory;
import com.ebay.sojourner.flink.connector.kafka.KafkaProducerConfig;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojEventDistributionJob {
  public static void main(String[] args) throws Exception {

    final String DATA_SOURCE_OP_NAME = "Non-bot SojEvent Source";
    final String DATA_SOURCE_UID = "non-bot-sojevent-source";
    final String CONFIG_SOURCE_OP_NAME = "PageId Topic Mapping Source";
    final String CONFIG_SOURCE_UID = "pageId-topic-mapping-source";
    final String DIST_OP_NAME = "SojEvent Distribution";
    final String DIST_UID = "sojevent-dist";
    final String SINK_OP_NAME = "Non-bot SojEvent Distribution Sink";
    final String SINK_UID = "non-bot-sojevent-dist-sink";


    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    SourceDataStreamBuilder<RawSojEventWrapper> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<RawSojEventWrapper> sojEventDataStream = dataStreamBuilder
        .dc(DataCenter.of(getString(FLINK_APP_SOURCE_DC)))
        .parallelism(getInteger(Property.SOURCE_PARALLELISM))
        .operatorName(DATA_SOURCE_OP_NAME)
        .uid(DATA_SOURCE_UID)
        .build(new SojEventDeserializationSchema());


    DataStream<PageIdTopicMapping> mappingSourceStream = executionEnvironment
        .addSource(new MappingSourceFunction(getString(Property.REST_BASE_URL),
                                             getLong(Property.REST_PULL_INTERVAL)))
        .setParallelism(1)
        .name(CONFIG_SOURCE_OP_NAME)
        .uid(CONFIG_SOURCE_UID);


    MapStateDescriptor<Integer, PageIdTopicMapping> stateDescriptor = new MapStateDescriptor<>(
        "pageIdTopicMappingBroadcastState",
        BasicTypeInfo.INT_TYPE_INFO,
        TypeInformation.of(new TypeHint<PageIdTopicMapping>() {
        }));

    BroadcastStream<PageIdTopicMapping> broadcastStream =
        mappingSourceStream.broadcast(stateDescriptor);

    DataStream<RawSojEventWrapper> sojEventDistStream =
        sojEventDataStream.connect(broadcastStream)
                          .process(new SojEventDistProcessFunction(stateDescriptor))
                          .name(DIST_OP_NAME)
                          .uid(DIST_UID)
                          .setParallelism(getInteger(Property.SOURCE_PARALLELISM));

    KafkaProducerConfig config = KafkaProducerConfig.ofDC(getString(FLINK_APP_SINK_DC));
    FlinkKafkaProducerFactory producerFactory = new FlinkKafkaProducerFactory(config);

    sojEventDistStream.addSink(producerFactory.get(
        getString(Property.FLINK_APP_SINK_KAFKA_TOPIC), new CustomSojEventSerializationSchema()))
                      .setParallelism(getInteger(Property.SINK_KAFKA_PARALLELISM))
                      .name(SINK_OP_NAME)
                      .uid(SINK_UID);

    // Submit this job
    FlinkEnvUtils.execute(executionEnvironment, getString(Property.FLINK_APP_NAME));
  }
}