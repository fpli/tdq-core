package com.ebay.tdq.sinks;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.SinkConfig;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * @author juntzhang
 */
@VisibleForTesting
public class MemorySink implements Sinkable {

  private static RichSinkFunction<InternalMetric> memoryFunction;

  public static RichSinkFunction<InternalMetric> getMemoryFunction() {
    return memoryFunction;
  }

  public static void setMemoryFunction(RichSinkFunction<InternalMetric> memoryFunction) {
    MemorySink.memoryFunction = memoryFunction;
  }

  @Override
  public void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    ds.addSink(memoryFunction)
        .name(id + "_mem")
        .setParallelism(1)
        .uid(id + "_mem");
  }

  @Override
  public void sinkLatencyMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    sinkNormalMetric(id, sinkConfig, tdqEnv, ds);
  }

  @Override
  public void sinkSampleLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqSampleData> ds) {
    throw new NotImplementedException("sample log not supported");
  }

  @Override
  public void sinkExceptionLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqErrorMsg> ds) {
    throw new NotImplementedException("exception log not supported");
  }
}
