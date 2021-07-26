package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.RawEventAvro;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.TdqContext;
import java.time.Duration;
import java.util.Random;
import lombok.val;
import org.apache.commons.lang3.Validate;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.runtime.operators.TdqTimestampsAndWatermarksOperator;

/**
 * @author juntzhang
 */
public class RhsKafkaSourceBuilder {

  private static void dump(SourceConfig sourceConfig, TdqContext tdqCxt) throws ReflectiveOperationException {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig);
    Validate.isTrue(ksc != null);

    val tdqEnv = tdqCxt.getTdqEnv();
    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    Class<?> c = Class.forName(ksc.getDeserializer());
    KafkaDeserializationSchema<RawEvent> schema = (KafkaDeserializationSchema<RawEvent>) c
        .getConstructor(TdqEnv.class).newInstance(ksc.getEndOfStreamTimestamp());

    FlinkKafkaConsumer<RawEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        ksc.getTopics(), schema, ksc.getKafkaConsumer());

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      flinkKafkaConsumer.setStartFromTimestamp(ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs());
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    DataStream<RawEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(flinkKafkaConsumer)
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
            .setSojA(raw.getSojA())
            .setSojC(raw.getSojC())
            .setSojK(raw.getSojK())
            .setClientData(raw.getClientData().getMap())
            .setIngestTime(raw.getIngestTime())
            .setEventTimestamp(raw.getUnixEventTimestamp())
            .setSojTimestamp(raw.getEventTimestamp())
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

  public static DataStream<TdqMetric> build(SourceConfig sourceConfig, TdqContext tdqCxt)
      throws ReflectiveOperationException {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig);
    Validate.isTrue(ksc != null);

    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    Class<?> c = Class.forName(ksc.getDeserializer());
    KafkaDeserializationSchema<RawEvent> schema = (KafkaDeserializationSchema<RawEvent>) c
        .getConstructor(TdqEnv.class).newInstance(ksc.getEndOfStreamTimestamp());

    FlinkKafkaConsumer<RawEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
        ksc.getTopics(), schema, ksc.getKafkaConsumer());

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      flinkKafkaConsumer.setStartFromTimestamp(ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs());
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    DataStream<RawEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(flinkKafkaConsumer)
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

    return normalize(tdqCxt, ksc, rawEventDataStream);
  }

  private static SingleOutputStreamOperator<TdqMetric> normalize(
      TdqContext tdqCxt, KafkaSourceConfig ksc, DataStream<RawEvent> rawEventDataStream) {

    SingleOutputStreamOperator<TdqMetric> ds = rawEventDataStream
        .process(new RawEventProcessFunction(tdqCxt))
        .name(ksc.getName() + "_normalize")
        .uid(ksc.getName() + "_normalize")
        .slotSharingGroup(ksc.getName())
        .setParallelism(ksc.getParallelism());

    SerializableTimestampAssigner<TdqMetric> assigner =
        (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime();

    WatermarkStrategy<TdqMetric> watermarkStrategy = WatermarkStrategy
        .<TdqMetric>forBoundedOutOfOrderness(Duration.ofMillis(ksc.getOutOfOrderlessMs()))
        .withTimestampAssigner(assigner)
        .withIdleness(Duration.ofMillis(ksc.getIdleTimeoutMs()));
    TdqTimestampsAndWatermarksOperator<TdqMetric> operator =
        new TdqTimestampsAndWatermarksOperator<>(tdqCxt.getRhsEnv().clean(watermarkStrategy));

    ds = ds.transform("Timestamps/Watermarks", ds.getTransformation().getOutputType(), operator)
        .slotSharingGroup(ksc.getName())
        .name(ksc.getName() + "_wks")
        .uid(ksc.getName() + "_wks")
        .slotSharingGroup(ksc.getName())
        .setParallelism(ksc.getParallelism());
    return ds;
  }
}
