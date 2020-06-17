package com.ebay.sojourner.rt.pipeline;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operators.event.UbiEventToSojEventMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionWindowProcessFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;

public class SojournerRTJobForSessionDQ {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // kafka source for copy
    DataStream<RawEvent> rawEventDataStream =
        executionEnvironment.addSource(KafkaSourceFunction.buildSource(
            FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_TOPIC),
            FlinkEnvUtils
                .getListString(Property.KAFKA_CONSUMER_BOOTSTRAP_SERVERS_RNO),
            FlinkEnvUtils.getString(Property.KAFKA_CONSUMER_GROUP_ID),
            RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Property.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For Session DQ")
            .uid("source-id");

    DataStream<UbiEvent> ubiEventDataStream =
        rawEventDataStream
            .map(new EventMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.EVENT_PARALLELISM))
            .name("Event Operator")
            .uid("event-id");

    // 3. Session Operator
    // 3.1 Session window
    // 3.2 Session indicator accumulation
    // 3.3 Session Level bot detection (via bot rule & signature)
    // 3.4 Event level bot detection (via session flag)
    OutputTag<UbiSession> sessionOutputTag =
        new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
    OutputTag<UbiEvent> lateEventOutputTag =
        new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
    OutputTag<UbiEvent> mappedEventOutputTag =
        new OutputTag<>("mapped-event-output-tag", TypeInformation.of(UbiEvent.class));
    SingleOutputStreamOperator<UbiSession> ubiSessionDataStream =
        ubiEventDataStream
            .keyBy("guid")
            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
            .allowedLateness(Time.minutes(5))
            .sideOutputLateData(lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());

    // Hack here to use MapWithStateWindowOperator instead while bypassing DataStream API which
    // cannot be enhanced easily since we do not want to modify Flink framework sourcecode.
    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        mappedEventOutputTag);

    ubiSessionDataStream
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .name("Session Operator")
        .uid("session-id");

    DataStream<UbiEvent> ubiEventWithSessionId = ubiSessionDataStream
        .getSideOutput(mappedEventOutputTag);

    // UbiSession to SojSession
    SingleOutputStreamOperator<SojSession> sojSessionStream =
        ubiSessionDataStream
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
            .name("UbiSession to SojSession")
            .uid("session-transform-id");

    // UbiEvent to SojEvent
    DataStream<SojEvent> sojEventWithSessionId = ubiEventWithSessionId
        .map(new UbiEventToSojEventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .name("UbiEvent to SojEvent")
        .uid("event-transform-id");

    // This path is for local test. For production, we should use
    // "hdfs://apollo-rno//user/o_ubi/events/"
    sojSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_PATH_PARENT) +
                FlinkEnvUtils.getString(Property.HDFS_PATH_SESSION_NON_BOT),
            SojSession.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .name("SojSession sink")
        .uid("session-sink-id");

    sojEventWithSessionId
        .addSink(HdfsConnectorFactory.createWithParquet(
            FlinkEnvUtils.getString(Property.HDFS_PATH_PARENT) +
                FlinkEnvUtils.getString(Property.HDFS_PATH_EVENT_NON_BOT),
            SojEvent.class))
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .name("SojEvent sink")
        .uid("event-sink-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Property.NAME_DATA_QUALITY));
  }
}
