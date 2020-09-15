package com.ebay.sojourner.rt.pipeline;

import static com.ebay.sojourner.flink.common.util.DataCenter.LVS;
import static com.ebay.sojourner.flink.common.util.DataCenter.RNO;
import static com.ebay.sojourner.flink.common.util.DataCenter.SLC;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.common.util.Property;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.common.util.OutputTagConstants;
import com.ebay.sojourner.flink.connector.hdfs.HdfsConnectorFactory;
import com.ebay.sojourner.flink.connector.kafka.SourceDataStreamBuilder;
import com.ebay.sojourner.rt.operator.event.EventDataStreamBuilder;
import com.ebay.sojourner.rt.operator.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operator.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.rt.operator.session.UbiSessionWindowProcessFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;

public class SojournerRTJobForHotDeploy {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    SourceDataStreamBuilder<RawEvent> dataStreamBuilder = new SourceDataStreamBuilder<>(
        executionEnvironment, RawEvent.class
    );

    DataStream<RawEvent> rawEventDataStreamForRNO = dataStreamBuilder
        .buildOfDC(RNO, FlinkEnvUtils.getString(Property.SOURCE_EVENT_RNO_SLOT_SHARE_GROUP));
    DataStream<RawEvent> rawEventDataStreamForSLC = dataStreamBuilder
        .buildOfDC(SLC, FlinkEnvUtils.getString(Property.SOURCE_EVENT_SLC_SLOT_SHARE_GROUP));
    DataStream<RawEvent> rawEventDataStreamForLVS = dataStreamBuilder
        .buildOfDC(LVS, FlinkEnvUtils.getString(Property.SOURCE_EVENT_LVS_SLOT_SHARE_GROUP));

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStreamForLVS = EventDataStreamBuilder
        .build(rawEventDataStreamForLVS, LVS);
    DataStream<UbiEvent> ubiEventDataStreamForSLC = EventDataStreamBuilder
        .build(rawEventDataStreamForSLC, SLC);
    DataStream<UbiEvent> ubiEventDataStreamForRNO = EventDataStreamBuilder
        .build(rawEventDataStreamForRNO, RNO);

    // union ubiEvent from SLC/RNO/LVS
    DataStream<UbiEvent> ubiEventDataStream = ubiEventDataStreamForLVS
        .union(ubiEventDataStreamForSLC)
        .union(ubiEventDataStreamForRNO);


    SingleOutputStreamOperator<UbiSession> ubiSessionDataStream =
        ubiEventDataStream
            .keyBy("guid")
            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
            .allowedLateness(Time.minutes(3))
            .sideOutputLateData(OutputTagConstants.lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());


    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        OutputTagConstants.mappedEventOutputTag);

    ubiSessionDataStream
        .setParallelism(FlinkEnvUtils.getInteger(Property.SESSION_PARALLELISM))
        .slotSharingGroup(FlinkEnvUtils.getString(Property.SESSION_SLOT_SHARE_GROUP))
        .name("Session Operator")
        .uid("session-id");

    // ubiSession to sojSession
    DataStream<SojSession> sojSessionStream =
        ubiSessionDataStream
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(FlinkEnvUtils.getInteger(Property.BROADCAST_PARALLELISM))
            .slotSharingGroup(FlinkEnvUtils.getString(Property.CROSS_SESSION_SLOT_SHARE_GROUP))
            .name("UbiSession to SojSession")
            .uid("session-transform-id");

    // hdfs sink
    sojSessionStream
        .addSink(HdfsConnectorFactory.createWithParquet(
            "hdfs://apollo-rno/sys/soj/ubd/rule_test_1", SojSession.class))
        .setParallelism(100)
        .name("hdfs-sink")
        .uid("sink-id");


    // Submit this job
    FlinkEnvUtils.execute(executionEnvironment, FlinkEnvUtils.getString(Property.FLINK_APP_NAME));
  }
}

