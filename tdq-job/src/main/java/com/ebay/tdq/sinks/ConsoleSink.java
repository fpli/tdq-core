package com.ebay.tdq.sinks;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.SinkConfig;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author juntzhang
 */
public class ConsoleSink implements Sinkable {

  @Override
  public void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    ds
        .print((String) sinkConfig.getConfig().get("std-name"))
        .uid(id + "_std")
        .name(id + "_std")
        .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
  }

  @Override
  public void sinkLatencyMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    sinkNormalMetric(id, sinkConfig, tdqEnv, ds);
  }

  @Override
  public void sinkSampleLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqSampleData> ds) {
    ds
        .print((String) sinkConfig.getConfig().get("std-name"))
        .uid(id + "_log_sample_o_std")
        .name(id + "_log_sample_o_std")
        .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
  }

  @Override
  public void sinkExceptionLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqErrorMsg> ds) {
    ds
        .print((String) sinkConfig.getConfig().get("std-name"))
        .uid(id + "_log_exception_o_std")
        .name(id + "_log_exception_o_std")
        .setParallelism(tdqEnv.getMetric2ndAggrParallelism());
  }
}
