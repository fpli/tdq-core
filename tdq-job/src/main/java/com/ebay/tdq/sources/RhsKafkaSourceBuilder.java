package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.ClientData;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.SojTimestamp;
import com.ebay.tdq.common.model.RawEventAvro;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.RheosEventDeserializationSchema;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.TdqContext;
import io.ebay.rheos.schema.event.RheosEvent;
import java.util.HashMap;
import java.util.Random;
import lombok.val;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

/**
 * @author juntzhang
 */
public class RhsKafkaSourceBuilder {

  private static void dump(SourceConfig sourceConfig, TdqContext tdqCxt) throws ReflectiveOperationException {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig);
    val tdqEnv = tdqCxt.getTdqEnv();
    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    Class<?> c = Class.forName(ksc.getDeserializer());
    KafkaDeserializationSchema<RawEvent> schema = (KafkaDeserializationSchema<RawEvent>) c
        .getConstructor(Long.class).newInstance(ksc.getEndOfStreamTimestamp());

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

    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    DataStream<RawEvent> inDS;
    if (ksc.getDeserializer().equals(
        "com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema")) {
      Class<?> c = Class.forName(ksc.getDeserializer());
      KafkaDeserializationSchema<RawEvent> schema = new PathFinderRawEventKafkaDeserializationSchema(
          ksc.getEndOfStreamTimestamp());
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
      inDS = tdqCxt.getRhsEnv()
          .addSource(flinkKafkaConsumer)
          .setParallelism(ksc.getParallelism())
          .slotSharingGroup(ksc.getName())
          .name(ksc.getName())
          .uid(ksc.getName());
    } else {
      // todo for test
      val schema = new RheosEventDeserializationSchema("https://rheos-services.qa.ebay.com",
          ksc.getEndOfStreamTimestamp());
      FlinkKafkaConsumer<RheosEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
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

      inDS = tdqCxt.getRhsEnv()
          .addSource(flinkKafkaConsumer)
          .setParallelism(ksc.getParallelism())
          .slotSharingGroup(ksc.getName())
          .name(ksc.getName())
          .uid(ksc.getName())
          .map((MapFunction<RheosEvent, RawEvent>) rec -> {
            RawEvent event = new RawEvent();
            event.setSojA(new HashMap<>());
            event.setSojK(new HashMap<>());
            event.setSojC(new HashMap<>());
            event.setClientData(new ClientData());
            event.setIngestTime(System.currentTimeMillis());
            event.setEventTimestamp(SojTimestamp.getUnixTimestampToSojTimestamp((long) rec.get("viTimestamp")));
            event.getSojA().put("t", String.valueOf(rec.get("siteId")));
            return event;
          })
          .setParallelism(ksc.getParallelism())
          .slotSharingGroup(ksc.getName())
          .name(ksc.getName() + "_test")
          .uid(ksc.getName() + "_test");
    }

    double sf = ksc.getSampleFraction();
    if (sf > 0 && sf < 1) {
      inDS = inDS
          .filter(r -> Math.abs(new Random().nextDouble()) < sf)
          .name(ksc.getName() + "_sample")
          .uid(ksc.getName() + "_sample")
          .slotSharingGroup(ksc.getName())
          .setParallelism(ksc.getParallelism());
    }

    return SourceFactory.getTdqMetricDS(tdqCxt, inDS, ksc.getName(), ksc.getParallelism(),
        ksc.getOutOfOrderlessMs(), ksc.getIdleTimeoutMs(), new RawEventProcessFunction(tdqCxt));
  }

}
