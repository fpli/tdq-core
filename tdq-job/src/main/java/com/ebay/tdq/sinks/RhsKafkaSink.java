package com.ebay.tdq.sinks;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.KafkaSinkConfig;
import com.ebay.tdq.config.SinkConfig;
import com.ebay.tdq.connector.kafka.schema.RheosEventSerdeFactory;
import com.ebay.tdq.connector.kafka.schema.TdqMetricSerializationSchema;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer.Semantic;

/**
 * @author juntzhang
 */
public class RhsKafkaSink implements Sinkable {

  @Override
  public void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    KafkaSinkConfig config = KafkaSinkConfig.build(sinkConfig, tdqEnv);

    FlinkKafkaProducer<TdqMetric> producer = new FlinkKafkaProducer<>(
        config.getTopic(),
        new TdqMetricSerializationSchema(config, tdqEnv),
        config.getKafkaProducer(),
        Semantic.AT_LEAST_ONCE);

    int schemaId = RheosEventSerdeFactory.getSchemaId(config.getSchemaSubject(), config.getRheosServicesUrls());

    ds
        .map(new RichMapFunction<InternalMetric, TdqMetric>() {
          @Override
          public TdqMetric map(InternalMetric value) {
            return value.toTdqMetric(config.getProducerId(), schemaId);
          }
        })
        .uid(id + "_trans")
        .name(id + "_trans")
        .setParallelism(config.getParallelism())
        .addSink(producer)
        .setParallelism(config.getParallelism())
        .uid(id + "_kafka")
        .name(id + "_kafka");
  }

  @Override
  public void sinkLatencyMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    sinkNormalMetric(id, sinkConfig, tdqEnv, ds);
  }

  @Override
  public void sinkSampleLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqSampleData> ds) {
    throw new NotImplementedException("sample log not supported");
  }

  @Override
  public void sinkExceptionLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqErrorMsg> ds) {
    throw new NotImplementedException("exception log not supported");
  }
}
