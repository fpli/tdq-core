package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.utils.TdqContext;
import java.time.Duration;
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
