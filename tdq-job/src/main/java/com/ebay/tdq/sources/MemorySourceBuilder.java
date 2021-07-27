package com.ebay.tdq.sources;

import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.config.MemorySourceConfig;
import com.ebay.tdq.config.SourceConfig;
import com.ebay.tdq.functions.RawEventProcessFunction;
import com.ebay.tdq.utils.TdqContext;
import com.google.common.annotations.VisibleForTesting;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author juntzhang
 */
public class MemorySourceBuilder {

  @VisibleForTesting
  private static List<TdqEvent> rawEventList;
  private static SourceFunction<TdqEvent> sourceFunction;

  @VisibleForTesting
  public static void setRawEventList(List<TdqEvent> rawEventList) {
    MemorySourceBuilder.rawEventList = rawEventList;
  }

  @VisibleForTesting
  public static void setSourceFunction(
      SourceFunction<TdqEvent> sourceFunction) {
    MemorySourceBuilder.sourceFunction = sourceFunction;
  }

  public static DataStream<TdqMetric> build(SourceConfig sourceConfig, TdqContext tdqCxt) {
    MemorySourceConfig msc = MemorySourceConfig.build(sourceConfig);
    if (sourceFunction == null) {
      sourceFunction = new SourceFunction<TdqEvent>() {
        @Override
        public void run(SourceContext<TdqEvent> ctx) throws Exception {
          Thread.sleep(1000);
          rawEventList.forEach(ctx::collect);
        }

        @Override
        public void cancel() {
        }
      };
    }
    DataStream<TdqEvent> rawEventDataStream = tdqCxt.getRhsEnv().addSource(sourceFunction)
        .setParallelism(msc.getParallelism())
        .slotSharingGroup(msc.getName())
        .name(msc.getName())
        .uid(msc.getName());

    return SourceFactory.getTdqMetricDS(tdqCxt, rawEventDataStream, msc.getName(), msc.getParallelism(),
        msc.getOutOfOrderlessMs(), msc.getIdleTimeoutMs(), new RawEventProcessFunction(tdqCxt));
  }

}
