package com.ebay.tdq.sinks;


import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.utils.TdqContext;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;

/**
 * @author juntzhang
 */
@Slf4j
public class SinkFactory implements Serializable {

  public static void sink(String id, String subType, TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    tdqCxt.getTdqEnv().getTdqConfig().getSinks().stream().filter(s -> subType.equals(s.getConfig().get("sub-type")))
        .forEach(sinkConfig -> {
          String type = sinkConfig.getType();
          Sinkable sinkable;
          switch (type) {
            case "realtime.kafka":
              sinkable = new RhsKafkaSink();
              break;
            case "realtime.hdfs":
              sinkable = new HdfsSink();
              break;
            case "realtime.pronto":
              sinkable = new ProntoSink();
              break;
            case "realtime.console":
              sinkable = new ConsoleSink();
              break;
            case "realtime.memory":
              sinkable = new MemorySink();
              break;
            default:
              throw new NotImplementedException(type + " not supported!");
          }
          switch (subType) {
            case "normal-metric":
              sinkable.sinkNormalMetric(id, sinkConfig, tdqCxt.getTdqEnv(), ds);
              break;
            case "latency-metric":
              sinkable.sinkLatencyMetric(id, sinkConfig, tdqCxt.getTdqEnv(),
                  ds.getSideOutput(tdqCxt.getEventLatencyOutputTag()));
              break;
            case "sample-log":
              sinkable.sinkSampleLog(id, sinkConfig, tdqCxt.getTdqEnv(),
                  ds.getSideOutput(tdqCxt.getSampleOutputTag()));
              break;
            case "exception-log":
              sinkable.sinkExceptionLog(id, sinkConfig, tdqCxt.getTdqEnv(),
                  ds.getSideOutput(tdqCxt.getExceptionOutputTag()));
              break;
            default:
              throw new NotImplementedException(subType + " not supported!");
          }
        });
  }

  public static void sinkNormalMetric(String id, TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    sink(id, "normal-metric", tdqCxt, ds);
  }

  public static void sinkLatencyMetric(TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    sink("evt_latency", "latency-metric", tdqCxt, ds);
  }

  public static void sinkSampleLog(String name, TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    sink(name, "sample-log", tdqCxt, ds);
  }

  public static void sinkException(String name, TdqContext tdqCxt, SingleOutputStreamOperator<InternalMetric> ds) {
    sink(name, "exception-log", tdqCxt, ds);
  }


}
