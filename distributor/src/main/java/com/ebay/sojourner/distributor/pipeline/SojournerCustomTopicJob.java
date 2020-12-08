package com.ebay.sojourner.distributor.pipeline;

import static com.ebay.sojourner.common.util.Property.FLINK_APP_SINK_DC;
import static com.ebay.sojourner.common.util.Property.FLINK_APP_SOURCE_DC;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getInteger;
import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

import com.ebay.sojourner.common.model.RawSojEventWrapper;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.distributor.SojEventFilterFunction;
import com.ebay.sojourner.distributor.schema.CustomSojEventSerializationSchema;
import com.ebay.sojourner.distributor.schema.SojEventDeserializationSchema;
import com.ebay.sojourner.flink.common.DataCenter;
import com.ebay.sojourner.flink.common.FlinkEnvUtils;
import com.ebay.sojourner.flink.connector.kafka.FlinkKafkaProducerFactory;
import com.ebay.sojourner.flink.connector.kafka.KafkaProducerConfig;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SojournerCustomTopicJob {
  public static void main(String[] args) throws Exception {

    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    SourceDataStreamBuilder<RawSojEventWrapper> dataStreamBuilder =
        new SourceDataStreamBuilder<>(executionEnvironment);

    DataStream<RawSojEventWrapper> sojEventDataStream = dataStreamBuilder
        .dc(DataCenter.of(getString(FLINK_APP_SOURCE_DC)))
        .parallelism(getInteger(Property.SOURCE_PARALLELISM))
        .operatorName("Nonbot SojEvent Source")
        .uid("nonbot-sojevent-source")
        .build(new SojEventDeserializationSchema());

    DataStream<RawSojEventWrapper> filteredDataStream = sojEventDataStream
        .filter(new SojEventFilterFunction())
        .name("Filter pageId")
        .uid("filter-pageId")
        .setParallelism(getInteger(Property.SOURCE_PARALLELISM));

    KafkaProducerConfig config = KafkaProducerConfig.ofDC(getString(FLINK_APP_SINK_DC));
    FlinkKafkaProducerFactory producerFactory = new FlinkKafkaProducerFactory(config);

    filteredDataStream.addSink(producerFactory.get(
        getString(Property.FLINK_APP_SINK_KAFKA_TOPIC), new CustomSojEventSerializationSchema()))
                      .setParallelism(getInteger(Property.SINK_KAFKA_PARALLELISM))
                      .name("Nonbot SojEvent Customized Sink")
                      .uid("nonbot-sojevent-customized-sink");

    // Submit this job
    FlinkEnvUtils.execute(executionEnvironment, getString(Property.FLINK_APP_NAME));
  }
}
