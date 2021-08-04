package com.ebay.tdq.jobs;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.SojEvent;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.SojEventDeserializationSchema;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.SojEventDateTimeBucketAssigner;
import com.ebay.tdq.utils.TdqConfigManager;
import com.ebay.tdq.utils.TdqContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author juntzhang
 */
public class SojEventDumpJob {

  protected TdqContext tdqCxt;

  public static void main(String[] args) throws Exception {
    new SojEventDumpJob().submit(args);
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

    SojEventDeserializationSchema deserializer = new SojEventDeserializationSchema();
    FlinkKafkaConsumer<SojEvent> flinkKafkaConsumer = new FlinkKafkaConsumer<>(ksc.getTopics(), deserializer,
        ksc.getKafkaConsumer());

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      flinkKafkaConsumer.setStartFromTimestamp(ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs());
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    DataStream<SojEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(flinkKafkaConsumer)
        .setParallelism(ksc.getParallelism())
        .slotSharingGroup(ksc.getName())
        .name(ksc.getName())
        .uid(ksc.getName());

    StreamingFileSink<SojEvent> sink = HdfsConnectorFactory.createWithParquet(
        tdqEnv.getSinkEnv().getRawDataPath() + "/" + tdqEnv.getJobName() + "/source=" + ksc.getName(),
        SojEvent.class, new SojEventDateTimeBucketAssigner(
            tdqEnv.getSinkEnv().getTimeZone().toZoneId()
        ));

    rawEventDataStream
        .addSink(sink)
        .setParallelism(ksc.getParallelism())
        .name(ksc.getName() + "_dump")
        .uid(ksc.getName() + "_dump");
  }

}
