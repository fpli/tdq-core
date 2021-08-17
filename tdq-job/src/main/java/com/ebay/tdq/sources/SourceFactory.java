package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.sinks.SinkFactory;
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

  public static DataStream<InternalMetric> build(TdqContext tdqCxt) {
    List<DataStream<InternalMetric>> list = new ArrayList<>();
    for (SourceConfig sourceConfig : tdqCxt.getTdqEnv().getTdqConfig().getSources()) {
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

  public static <T> SingleOutputStreamOperator<InternalMetric> getTdqMetricDS(
      TdqContext tdqCxt, DataStream<T> inDS, String name, int parallelism,
      Long outOfOrderlessMs, Long idleTimeoutMs,
      ProcessFunction<T, InternalMetric> processFunction
  ) {
    SingleOutputStreamOperator<InternalMetric> outDS = inDS
        .process(processFunction)
        .name(name + "_normalize")
        .uid(name + "_normalize")
        .slotSharingGroup(name)
        .setParallelism(parallelism);

    SinkFactory.sinkException(name, tdqCxt, outDS);
    SinkFactory.sinkSampleLog(name, tdqCxt, outDS);

    SerializableTimestampAssigner<InternalMetric> assigner =
        (SerializableTimestampAssigner<InternalMetric>) (event, timestamp) -> event.getEventTime();

    WatermarkStrategy<InternalMetric> watermarkStrategy = WatermarkStrategy
        .<InternalMetric>forBoundedOutOfOrderness(Duration.ofMillis(outOfOrderlessMs))
        .withTimestampAssigner(assigner)
        .withIdleness(Duration.ofMillis(idleTimeoutMs));

    TdqTimestampsAndWatermarksOperator<InternalMetric> operator =
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
