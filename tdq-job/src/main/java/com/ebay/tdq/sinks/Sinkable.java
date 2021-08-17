package com.ebay.tdq.sinks;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.SinkConfig;
import java.io.Serializable;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author juntzhang
 */
public interface Sinkable extends Serializable {

  void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds);

  void sinkLatencyMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds);

  void sinkSampleLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqSampleData> ds);

  void sinkExceptionLog(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqErrorMsg> ds);
}
