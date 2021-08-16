package com.ebay.tdq.jobs;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.RawEvent;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.RawEventDateTimeBucketAssigner;
import com.ebay.tdq.utils.TdqConfigManager;
import com.ebay.tdq.utils.TdqContext;
import java.util.Map;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author juntzhang
 */
public class RawEventDumpJob {

  protected TdqContext tdqCxt;

  public static void main(String[] args) throws Exception {
    new RawEventDumpJob().submit(args);
  }

  public void submit(String[] args) throws Exception {
    tdqCxt = new TdqContext(args);

    TdqConfig tdqConfig = TdqConfigManager.getTdqConfig(tdqCxt.getTdqEnv());

    for (SourceConfig sourceConfig : tdqConfig.getSources()) {
      dump(sourceConfig, tdqCxt);
    }

    tdqCxt.getRhsEnv().execute(tdqCxt.getTdqEnv().getJobName());
  }

  private static void dump(SourceConfig sourceConfig, TdqContext tdqCxt) {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig, tdqCxt.getTdqEnv());
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

    StreamingFileSink<RawEvent> sink = HdfsConnectorFactory.createWithParquet(
        tdqEnv.getSinkEnv().getRawDataPath() + "/" + tdqEnv.getJobName() + "/source=" + ksc.getName(),
        RawEvent.class, new RawEventDateTimeBucketAssigner(tdqEnv.getSinkEnv().getTimeZone().toZoneId()));

    rawEventDataStream
        .map(raw -> RawEvent.newBuilder()
            .setSojA((Map) raw.get("sojA"))
            .setSojC((Map) raw.get("sojC"))
            .setSojK((Map) raw.get("sojK"))
            .setClientData((Map) raw.get("clientData"))
            .setIngestTime(raw.get("ingestTime") != null ? (long) raw.get("ingestTime") : 0)
            .setEventTimestamp(raw.getEventTimeMs())
            .setSojTimestamp(raw.get("soj_timestamp") != null ? (long) raw.get("soj_timestamp") : 0)
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

}
