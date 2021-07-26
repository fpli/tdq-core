package com.ebay.tdq.sources;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.config.MemorySourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.utils.TdqContext;
import com.google.common.annotations.VisibleForTesting;
import java.time.Duration;
import java.util.List;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.runtime.operators.TdqTimestampsAndWatermarksOperator;

/**
 * @author juntzhang
 */
public class MemorySourceBuilder {

  @VisibleForTesting
  private static List<RawEvent> rawEventList;
  private static SourceFunction<RawEvent> sourceFunction;

  @VisibleForTesting
  public static void setRawEventList(List<RawEvent> rawEventList) {
    MemorySourceBuilder.rawEventList = rawEventList;
  }

  @VisibleForTesting
  public static void setSourceFunction(
      SourceFunction<RawEvent> sourceFunction) {
    MemorySourceBuilder.sourceFunction = sourceFunction;
  }

  public static DataStream<TdqMetric> build(SourceConfig sourceConfig, TdqContext tdqCxt) {
    MemorySourceConfig msc = MemorySourceConfig.build(sourceConfig);
    if (sourceFunction == null) {
      sourceFunction = new SourceFunction<RawEvent>() {
        @Override
        public void run(SourceContext<RawEvent> ctx) throws Exception {
          Thread.sleep(1000);
          rawEventList.forEach(ctx::collect);
        }

        @Override
        public void cancel() {
        }
      };
    }
    DataStream<RawEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(sourceFunction)
        .setParallelism(msc.getParallelism())
        .slotSharingGroup(msc.getName())
        .name(msc.getName())
        .uid(msc.getName());

    SingleOutputStreamOperator<TdqMetric> ds = rawEventDataStream
        .process(new RawEventProcessFunction(tdqCxt))
        .name(msc.getName() + "_normalize")
        .uid(msc.getName() + "_normalize")
        .slotSharingGroup(msc.getName())
        .setParallelism(msc.getParallelism());

    SerializableTimestampAssigner<TdqMetric> assigner =
        (SerializableTimestampAssigner<TdqMetric>) (event, timestamp) -> event.getEventTime();

    WatermarkStrategy<TdqMetric> watermarkStrategy = WatermarkStrategy
        .<TdqMetric>forBoundedOutOfOrderness(Duration.ofMillis(msc.getOutOfOrderlessMs()))
        .withTimestampAssigner(assigner)
        .withIdleness(Duration.ofMillis(msc.getIdleTimeoutMs()));
    TdqTimestampsAndWatermarksOperator<TdqMetric> operator =
        new TdqTimestampsAndWatermarksOperator<>(tdqCxt.getRhsEnv().clean(watermarkStrategy));

    ds = ds.transform("Timestamps/Watermarks", ds.getTransformation().getOutputType(), operator)
        .slotSharingGroup(msc.getName())
        .name(msc.getName() + "_wks")
        .uid(msc.getName() + "_wks")
        .slotSharingGroup(msc.getName())
        .setParallelism(msc.getParallelism());
    return ds;
  }

}
