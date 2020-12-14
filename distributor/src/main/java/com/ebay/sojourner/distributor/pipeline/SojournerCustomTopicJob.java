package com.ebay.sojourner.distributor.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SINK_DC;
import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_DC;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Slf4j
public class SojournerCustomTopicJob {
  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    SourceDataStreamBuilder<RawSojEventWrapper> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<RawSojEventWrapper> sojEventDataStream = dataStreamBuilder
        .dc(DataCenter.of(getString(FLINK_APP_SOURCE_DC)))
        .parallelism(getInteger(Property.SOURCE_PARALLELISM))
        .operatorName("Non-bot SojEvent Source")
        .uid("non-bot-sojevent-source")
        .build(new SojEventDeserializationSchema());


    DataStream<PageIdTopicMapping> mappingSourceStream = executionEnvironment
        .addSource(new MappingSourceFunction(getString(Property.REST_BASE_URL), 15000L))
        .setParallelism(1)
        .name("PageId topic mapping config source")
        .uid("pageId-topic-mapping-source");


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
                          .name("SojEvent distribution operator")
                          .uid("sojevent-dist")
                          .setParallelism(getInteger(Property.SOURCE_PARALLELISM));

    KafkaProducerConfig config = KafkaProducerConfig.ofDC(getString(FLINK_APP_SINK_DC));
    FlinkKafkaProducerFactory producerFactory = new FlinkKafkaProducerFactory(config);

    sojEventDistStream.addSink(producerFactory.get(
        getString(Property.FLINK_APP_SINK_KAFKA_TOPIC), new CustomSojEventSerializationSchema()))
                      .setParallelism(getInteger(Property.SINK_KAFKA_PARALLELISM))
                      .name("Non-bot SojEvent Distribution Sink")
                      .uid("non-bot-sojevent-dist-sink");

    // Submit this job
    FlinkEnvUtils.execute(executionEnvironment, getString(Property.FLINK_APP_NAME));
  }
}
