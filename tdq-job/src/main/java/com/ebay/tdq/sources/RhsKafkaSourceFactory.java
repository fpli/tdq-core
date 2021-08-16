package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.TdqEventDeserializationSchema;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.TdqContext;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @author juntzhang
 */
@Slf4j
public class RhsKafkaSourceFactory {

  public static DataStream<InternalMetric> build(SourceConfig sourceConfig, TdqContext tdqCxt) {
    KafkaSourceConfig ksc = KafkaSourceConfig.build(sourceConfig, tdqCxt.getTdqEnv());

    tdqCxt.getTdqEnv().setFromTimestamp(ksc.getFromTimestamp());
    tdqCxt.getTdqEnv().setToTimestamp(ksc.getToTimestamp());

    DataStream<TdqEvent> inDS;
    FlinkKafkaConsumer<TdqEvent> flinkKafkaConsumer;
    if (ksc.getDeserializer() != null && ksc.getDeserializer().equals(
        "com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema")) {
      PathFinderRawEventKafkaDeserializationSchema deserializer = new PathFinderRawEventKafkaDeserializationSchema(
          ksc.getRheosServicesUrls(), ksc.getEndOfStreamTimestamp(), ksc.getEventTimeField());
      flinkKafkaConsumer = new FlinkKafkaConsumer<>(ksc.getTopics(), deserializer, ksc.getKafkaConsumer());
    } else {
      TdqEventDeserializationSchema deserializer = new TdqEventDeserializationSchema(ksc, tdqCxt.getTdqEnv());
      flinkKafkaConsumer = new FlinkKafkaConsumer<>(ksc.getTopics(), deserializer, ksc.getKafkaConsumer());
    }

    if (ksc.getStartupMode().equalsIgnoreCase("EARLIEST")) {
      flinkKafkaConsumer.setStartFromEarliest();
      log.warn("Kafka setStartFromEarliest()");
    } else if (ksc.getStartupMode().equalsIgnoreCase("LATEST")) {
      flinkKafkaConsumer.setStartFromLatest();
      log.warn("Kafka setStartFromLatest()");
    } else if (ksc.getStartupMode().equalsIgnoreCase("TIMESTAMP")) {
      long t = ksc.getFromTimestamp() - ksc.getOutOfOrderlessMs();
      flinkKafkaConsumer.setStartFromTimestamp(t);
      log.warn("Kafka setStartFromTimestamp(" + (t) + "):" + DateUtils.format(
          t, tdqCxt.getTdqEnv().getSinkEnv().getTimeZone()));
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
    return SourceFactory.getTdqMetricDS(tdqCxt, inDS, ksc.getName(), ksc.getParallelism(),
        ksc.getOutOfOrderlessMs(), ksc.getIdleTimeoutMs(), new RawEventProcessFunction(tdqCxt));
  }

}
