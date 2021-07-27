package com.ebay.tdq.sources;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.RawEventAvro;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.RheosEventDeserializationSchema;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.utils.TdqContext;
import java.util.Map;
import java.util.Random;
import org.apache.avro.Schema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author juntzhang
 */
public class RhsKafkaSourceBuilder {

  private static void dump(SourceConfig sourceConfig, TdqContext tdqCxt) throws ReflectiveOperationException {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig);
    final TdqEnv tdqEnv = tdqCxt.getTdqEnv();
    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    //    Class<?> c = Class.forName(ksc.getDeserializer());
    //    KafkaDeserializationSchema<RawEvent> schema = (KafkaDeserializationSchema<RawEvent>) c
    //        .getConstructor(Long.class).newInstance(ksc.getEndOfStreamTimestamp());
    PathFinderRawEventKafkaDeserializationSchema deserializer = new PathFinderRawEventKafkaDeserializationSchema(
        ksc.getRheosServicesUrls(), ksc.getEndOfStreamTimestamp(), ksc.getEventTimeField());
    FlinkKafkaConsumer<TdqEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        ksc.getTopics(), deserializer, ksc.getKafkaConsumer());

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      flinkKafkaConsumer.setStartFromTimestamp(ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs());
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    DataStream<TdqEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(flinkKafkaConsumer)
        .setParallelism(ksc.getParallelism())
        .slotSharingGroup(ksc.getName())
        .name(ksc.getName())
        .uid(ksc.getName());
    double sf = ksc.getSampleFraction();

    if (sf > 0 && sf < 1) {
      rawEventDataStream = rawEventDataStream
          .filter(r -> Math.abs(new Random().nextDouble()) < sf)
          .name(ksc.getName() + "_sample")
          .uid(ksc.getName() + "_sample")
          .slotSharingGroup(ksc.getName())
          .setParallelism(ksc.getParallelism());
    }

    StreamingFileSink<RawEventAvro> sink = HdfsConnectorFactory.createWithParquet(
        tdqEnv.getSinkEnv().getRawDataPath() + "/name=" + ksc.getName(),
        RawEventAvro.class, new RawEventDateTimeBucketAssigner(tdqEnv.getSinkEnv().getTimeZone().toZoneId()));

    rawEventDataStream
        .map(raw -> RawEventAvro.newBuilder()
            .setSojA((Map) raw.get("sojA"))
            .setSojC((Map) raw.get("sojC"))
            .setSojK((Map) raw.get("sojK"))
            .setClientData((Map) raw.get("clientData"))
            .setIngestTime((long) raw.get("ingestTime"))
            .setEventTimestamp(raw.getEventTimeMs())
            .setSojTimestamp((long) raw.get("soj_timestamp"))
            .setProcessTimestamp(System.currentTimeMillis())
            .build())
        .name(ksc.getName() + "_normalize")
        .uid(ksc.getName() + "_normalize")
        .setParallelism(ksc.getParallelism())
        .addSink(sink)
        .setParallelism(ksc.getParallelism())
        .name(ksc.getName() + "_dump")
        .uid(ksc.getName() + "_dump");
  }

  public static void dump(TdqConfig tdqConfig, TdqContext tdqCxt)
      throws ReflectiveOperationException {
    for (SourceConfig sourceConfig : tdqConfig.getSources()) {
      dump(sourceConfig, tdqCxt);
    }
  }

  public static DataStream<TdqMetric> build(SourceConfig sourceConfig, TdqContext tdqCxt) {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig);

    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    DataStream<TdqEvent> inDS;
    FlinkKafkaConsumer<TdqEvent> flinkKafkaConsumer;
    Schema schema = null;
    if (ksc.getDeserializer().equals(
        "com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema")) {
      PathFinderRawEventKafkaDeserializationSchema deserializer = new PathFinderRawEventKafkaDeserializationSchema(
          ksc.getRheosServicesUrls(), ksc.getEndOfStreamTimestamp(), ksc.getEventTimeField());
      flinkKafkaConsumer = new FlinkKafkaConsumer<>(ksc.getTopics(), deserializer, ksc.getKafkaConsumer());
    } else {
      RheosEventDeserializationSchema deserializer = new RheosEventDeserializationSchema(ksc.getRheosServicesUrls(),
          ksc.getEndOfStreamTimestamp(), ksc.getEventTimeField(), ksc.getSchemaSubject());
      flinkKafkaConsumer = new FlinkKafkaConsumer<>(ksc.getTopics(), deserializer, ksc.getKafkaConsumer());
    }

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      flinkKafkaConsumer.setStartFromTimestamp(ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs());
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    inDS = tdqCxt.getRhsEnv()
        .addSource(flinkKafkaConsumer)
        .setParallelism(ksc.getParallelism())
        .slotSharingGroup(ksc.getName())
        .name(ksc.getName())
        .uid(ksc.getName());

    double sf = ksc.getSampleFraction();
    if (sf > 0 && sf < 1) {
      inDS = inDS
          .filter(r -> Math.abs(new Random().nextDouble()) < sf)
          .name(ksc.getName() + "_sample")
          .uid(ksc.getName() + "_sample")
          .slotSharingGroup(ksc.getName())
          .setParallelism(ksc.getParallelism());
    }
    return transfer(tdqCxt, ksc, inDS, schema);
  }

  private static SingleOutputStreamOperator<TdqMetric> transfer(TdqContext tdqCxt, KafkaSourceConfig ksc,
      DataStream<TdqEvent> inDS, Schema schema) {
    return SourceFactory.getTdqMetricDS(tdqCxt, inDS, ksc.getName(), ksc.getParallelism(),
        ksc.getOutOfOrderlessMs(), ksc.getIdleTimeoutMs(), new RawEventProcessFunction(tdqCxt));
  }

}
