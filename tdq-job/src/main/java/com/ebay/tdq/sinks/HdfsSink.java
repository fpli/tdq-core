package com.ebay.tdq.sinks;

import static com.ebay.sojourner.common.env.EnvironmentUtils.replaceStringWithPattern;

import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.common.model.InternalMetric;
import com.ebay.tdq.common.model.RawEvent;
import com.ebay.tdq.common.model.SojEvent;
import com.ebay.tdq.common.model.TdqErrorMsg;
import com.ebay.tdq.common.model.TdqEvent;
import com.ebay.tdq.common.model.TdqMetric;
import com.ebay.tdq.common.model.TdqSampleData;
import com.ebay.tdq.config.SinkConfig;
import com.ebay.tdq.connector.kafka.schema.RheosEventSerdeFactory;
import com.ebay.tdq.sources.HdfsConnectorFactory;
import com.ebay.tdq.sources.RawEventDateTimeBucketAssigner;
import com.ebay.tdq.sources.SojEventDateTimeBucketAssigner;
import com.ebay.tdq.sources.TdqMetricDateTimeBucketAssigner;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;

/**
 * @author juntzhang
 */
public class HdfsSink implements Sinkable {

  private static void sinkMetric0(
      String id, String path, String producerId, int schemaId, TdqEnv tdqEnv,
      DataStream<InternalMetric> ds) {
    StreamingFileSink<TdqMetric> sink = HdfsConnectorFactory.createWithParquet(
        path + "/winId=" + id, TdqMetric.class, new TdqMetricDateTimeBucketAssigner(tdqEnv.getTimeZone().toZoneId()));
    ds.map(m -> m.toTdqMetric(producerId, schemaId))
        .uid(id + "_avro")
        .name(id + "_avro")
        .setParallelism(1)
        .slotSharingGroup(id + "_hdfs")
        .addSink(sink)
        .uid(id + "_o_hdfs")
        .name(id + "_o_hdfs")
        .slotSharingGroup(id + "_hdfs")
        .setParallelism(1);

  }

  @Override
  public void sinkNormalMetric(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<InternalMetric> ds) {
    Map<String, Object> props = sinkConfig.getConfig();
    String rheosServicesUrls = (String) sinkConfig.getConfig().get("rheos-services-urls");
    String schemaSubject = (String) sinkConfig.getConfig().get("schema-subject");
    String path = replaceStringWithPattern((String) sinkConfig.getConfig().get("hdfs-path"));
    int schemaId = RheosEventSerdeFactory.getSchemaId(schemaSubject, rheosServicesUrls);
    String producerId;
    if (props.get("producer-id") != null) {
      producerId = (String) props.get("producer-id");
    } else {
      producerId = tdqEnv.getJobName();
    }
    sinkMetric0(id, path, producerId, schemaId, tdqEnv, ds);
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

  public void sinkRawEvent(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<TdqEvent> ds) {
    int parallelism = (int) sinkConfig.getConfig().get("rhs-parallelism");
    String path = replaceStringWithPattern((String) sinkConfig.getConfig().get("hdfs-path"));
    StreamingFileSink<RawEvent> sink = HdfsConnectorFactory.createWithParquet(
        path + "/" + tdqEnv.getJobName() + "/source=" + id,
        RawEvent.class, new RawEventDateTimeBucketAssigner(tdqEnv.getTimeZone().toZoneId()));

    ds
        .map(raw -> RawEvent.newBuilder()
            .setSojA((Map) raw.get("sojA"))
            .setSojC((Map) raw.get("sojC"))
            .setSojK((Map) raw.get("sojK"))
            .setClientData((Map) raw.get("clientData"))
            .setIngestTime(raw.get("ingestTime") != null ? (long) raw.get("ingestTime") : 0)
            .setEventTimestamp(raw.getEventTimeMs())
            .setSojTimestamp(raw.get("soj_timestamp") != null ? (long) raw.get("soj_timestamp") : 0)
            .setProcessTimestamp(System.currentTimeMillis())
            .build())
        .name(id + "_normalize")
        .uid(id + "_normalize")
        .setParallelism(parallelism)
        .addSink(sink)
        .setParallelism(parallelism)
        .name(id + "_dump")
        .uid(id + "_dump");
  }

  public void sinkSojEvent(String id, SinkConfig sinkConfig, TdqEnv tdqEnv, DataStream<SojEvent> ds) {
    int parallelism = (int) sinkConfig.getConfig().get("rhs-parallelism");
    String path = replaceStringWithPattern((String) sinkConfig.getConfig().get("hdfs-path"));
    StreamingFileSink<SojEvent> sink = HdfsConnectorFactory.createWithParquet(
        path + "/" + tdqEnv.getJobName() + "/source=" + id,
        SojEvent.class, new SojEventDateTimeBucketAssigner(
            tdqEnv.getTimeZone().toZoneId()
        ));

    ds
        .addSink(sink)
        .setParallelism(parallelism)
        .name(id + "_dump")
        .uid(id + "_dump");
  }
}
