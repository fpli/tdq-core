package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.connector.kafka.schema.PathFinderRawEventKafkaDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.TdqEventDeserializationSchema;
import com.ebay.tdq.connector.kafka.schema.TdqMetricDeserializationSchema;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.functions.TdqMetricProcessFunction;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.TdqContext;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
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

    if (ksc.getDeserializer() != null && ksc.getDeserializer().equals(
        TdqMetricDeserializationSchema.class.getName())) {
      TdqMetricDeserializationSchema deserializer = new TdqMetricDeserializationSchema(ksc, tdqCxt.getTdqEnv());
      FlinkKafkaConsumer<TdqMetric> flinkKafkaConsumer = new FlinkKafkaConsumer<>(
          ksc.getTopics(), deserializer, ksc.getKafkaConsumer());
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
            t, tdqCxt.getTdqEnv().getTimeZone()));
      } else {
        throw new IllegalArgumentException("Cannot parse fromTimestamp value");
      }

      SingleOutputStreamOperator<InternalMetric> inDS = tdqCxt.getRhsEnv()
          .addSource(flinkKafkaConsumer)
          .setParallelism(ksc.getParallelism())
          .slotSharingGroup(ksc.getName())
          .name(ksc.getName())
          .uid(ksc.getName())
          .process(new TdqMetricProcessFunction(tdqCxt))
          .name(ksc.getName() + "_normalize")
          .uid(ksc.getName() + "_normalize")
          .slotSharingGroup(ksc.getName());

      if (ksc.getParallelism() != -1) {
        inDS.setParallelism(ksc.getParallelism());
      }

      return SourceFactory.assignTimestampsAndWatermarks(
          tdqCxt, ksc.getName(), ksc.getParallelism(), ksc.getOutOfOrderlessMs(), ksc.getIdleTimeoutMs(), inDS
      );
    }

    SingleOutputStreamOperator<TdqEvent> inDS;
    FlinkKafkaConsumer<TdqEvent> flinkKafkaConsumer;
    if (ksc.getDeserializer() != null && ksc.getDeserializer().equals(
        PathFinderRawEventKafkaDeserializationSchema.class.getName())) {
      PathFinderRawEventKafkaDeserializationSchema deserializer = new PathFinderRawEventKafkaDeserializationSchema(ksc);
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
          t, tdqCxt.getTdqEnv().getTimeZone()));
    } else {
      throw new IllegalArgumentException("Cannot parse fromTimestamp value");
    }

    inDS = tdqCxt.getRhsEnv()
        .addSource(flinkKafkaConsumer)
        .slotSharingGroup(ksc.getName())
        .name(ksc.getName())
        .uid(ksc.getName());

    if (ksc.getParallelism() != -1) {
      inDS.setParallelism(ksc.getParallelism());
    }

    double sf = ksc.getSampleFraction();
    Random random = new Random();
    if (sf > 0 && sf < 1) {
      inDS = inDS
          .filter(r -> Math.abs(random.nextDouble()) < sf)
          .name(ksc.getName() + "_sample")
          .uid(ksc.getName() + "_sample")
          .slotSharingGroup(ksc.getName())
          .setParallelism(ksc.getParallelism());
    }

    return SourceFactory.getTdqMetricDS(tdqCxt, inDS, ksc.getName(), ksc.getParallelism(),
        ksc.getOutOfOrderlessMs(), ksc.getIdleTimeoutMs(), new RawEventProcessFunction(tdqCxt));
  }

}
