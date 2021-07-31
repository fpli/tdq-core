package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.sinks.TdqSinks;
import com.ebay.tdq.utils.TdqContext;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.runtime.operators.TdqTimestampsAndWatermarksOperator;

/**
 * @author juntzhang
 */
public class SourceFactory {

  public static DataStream<TdqMetric> build(TdqConfig tdqConfig, TdqContext tdqCxt) {
    List<DataStream<TdqMetric>> list = new ArrayList<>();
    for (SourceConfig sourceConfig : tdqConfig.getSources()) {
      if (sourceConfig.getType().equals("realtime.kafka")) {
        list.add(RhsKafkaSourceFactory.build(sourceConfig, tdqCxt));
      } else if (sourceConfig.getType().equals("realtime.memory")) {
        list.add(MemorySourceFactory.build(sourceConfig, tdqCxt));
      } else {
        throw new IllegalArgumentException(sourceConfig.getType() + " not implement!");
      }
    }
    return list.stream().reduce(DataStream::union).orElse(null);
  }

  public static <T> SingleOutputStreamOperator<TdqMetric> getTdqMetricDS(
      TdqContext tdqCxt, DataStream<T> inDS, String name, int parallelism,
      Long outOfOrderlessMs, Long idleTimeoutMs,
      ProcessFunction<T, TdqMetric> processFunction
  ) {
    SingleOutputStreamOperator<TdqMetric> outDS = inDS
        .process(processFunction)
        .name(name + "_normalize")
        .uid(name + "_normalize")
        .slotSharingGroup(name)
        .setParallelism(parallelism);

    TdqSinks.sinkException(tdqCxt, outDS, name);
    TdqSinks.sinkSampleLog(tdqCxt, outDS, name);

    SerializableTimestampAssigner<TdqMetric> assigner =
        (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime();

    WatermarkStrategy<TdqMetric> watermarkStrategy = WatermarkStrategy
        .<TdqMetric>forBoundedOutOfOrderness(Duration.ofMillis(outOfOrderlessMs))
        .withTimestampAssigner(assigner)
        .withIdleness(Duration.ofMillis(idleTimeoutMs));

    TdqTimestampsAndWatermarksOperator<TdqMetric> operator =
        new TdqTimestampsAndWatermarksOperator<>(tdqCxt.getRhsEnv().clean(watermarkStrategy));

    outDS = outDS.transform("Timestamps/Watermarks", outDS.getTransformation().getOutputType(), operator)
        .slotSharingGroup(name)
        .name(name + "_wks")
        .uid(name + "_wks")
        .slotSharingGroup(name)
        .setParallelism(parallelism);

    return outDS;
  }
}
