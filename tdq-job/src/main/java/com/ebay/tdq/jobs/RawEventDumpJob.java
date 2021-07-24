package com.ebay.tdq.jobs;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.common.model.RawEventAvro;
import com.ebay.tdq.sources.BehaviorPathfinderSource;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.RawEventDateTimeBucketAssigner;
import com.ebay.tdq.utils.FlinkEnvFactory;
import com.ebay.tdq.utils.TdqEnv;
import java.util.List;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

/**
 * @author juntzhang
 */
public class RawEventDumpJob {

  protected TdqEnv tdqEnv;
  protected transient StreamExecutionEnvironment env;

  public DataStream<RawEventAvro> normalize(DataStream<RawEvent> ds, int index) {
    return ds
        .map(raw -> {
          return RawEventAvro.newBuilder()
              .setSojA(raw.getSojA())
              .setSojC(raw.getSojC())
              .setSojK(raw.getSojK())
              .setClientData(raw.getClientData().getMap())
              .setIngestTime(raw.getIngestTime())
              .setEventTimestamp(raw.getUnixEventTimestamp())
              .setSojTimestamp(raw.getEventTimestamp())
              .setProcessTimestamp(System.currentTimeMillis())
              .build();
        })
        .name("normalize_" + index)
        .uid("normalize_" + index)
        .setParallelism(ds.getParallelism());
  }

  public void submit(String[] args) throws Exception {
    env = FlinkEnvFactory.create(new TdqEnv(args));

    // step1: build data source
    List<DataStream<RawEvent>> rawEventDataStream = new BehaviorPathfinderSource(tdqEnv, env).build();
    for (int i = 0; i < rawEventDataStream.size(); i++) {
      DataStream<RawEvent> ds = rawEventDataStream.get(0);
      StreamingFileSink<RawEventAvro> sink = HdfsConnectorFactory.createWithParquet(
          tdqEnv.getHdfsEnv().getRawDataPath() + "/i=" + i,
          RawEventAvro.class, new RawEventDateTimeBucketAssigner(tdqEnv.getTimeZone().toZoneId()));
      ds
          .map(raw -> RawEventAvro.newBuilder()
              .setSojA(raw.getSojA())
              .setSojC(raw.getSojC())
              .setSojK(raw.getSojK())
              .setClientData(raw.getClientData().getMap())
              .setIngestTime(raw.getIngestTime())
              .setEventTimestamp(raw.getUnixEventTimestamp())
              .setSojTimestamp(raw.getEventTimestamp())
              .setProcessTimestamp(System.currentTimeMillis())
              .build())
          .name("normalize_" + i)
          .uid("normalize_" + i)
          .setParallelism(ds.getParallelism())
          .addSink(sink)
          .setParallelism(ds.getParallelism())
          .name("tdq_hdfs_dump_" + i)
          .uid("tdq_hdfs_dump_" + i);

    }
    env.execute(tdqEnv.getJobName());
  }

  public static void main(String[] args) throws Exception {
    new RawEventDumpJob().submit(args);
  }

}
