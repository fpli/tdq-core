package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.RawEventFilterFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventToSojEventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.Constants;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;

public class SojournerRTLoadJob {

  public static void main(String[] args) throws Exception {

    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        ExecutionEnvUtil.prepare(parameterTool);

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic(RNO/LVS/SLC)
    // 1.2 Assign timestamps and emit watermarks.
    DataStream<RawEvent> rawEventDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_RNO, Constants.GROUP_ID_RNO_DQ, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For RNO")
            .uid("kafkaSourceForRNO");

    DataStream<RawEvent> rawEventDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_SLC, Constants.GROUP_ID_SLC_DQ, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For SLC")
            .uid("kafkaSourceForSLC");

    DataStream<RawEvent> rawEventDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction.buildSource(Constants.TOPIC_PATHFINDER_EVENTS,
                Constants.BOOTSTRAP_SERVERS_LVS, Constants.GROUP_ID_LVS_DQ, RawEvent.class))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer For LVS")
            .uid("kafkaSourceForLVS");

    // union three DC data
    DataStream<RawEvent> rawEventDataStream = rawEventDataStreamForRNO
        .union(rawEventDataStreamForLVS)
        .union(rawEventDataStreamForSLC);

    // filter 33% throughput group by guid for reduce kafka consumer lag
    DataStream<RawEvent> filteredRawEvent = rawEventDataStream
        .filter(new RawEventFilterFunction())
        .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
        .name("RawEvent Filter Operator")
        .disableChaining()
        .uid("filterSource");

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStream =
        filteredRawEvent
            .map(new EventMapFunction())
            .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
            .name("Event Operator")
            .uid("eventLevel");

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
        .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
        .name("Session Operator")
        .uid("sessionLevel");

    // UbiSession to SojSession
    SingleOutputStreamOperator<SojSession> sojSessionStream =
        ubiSessionDataStream
            .map(new UbiSessionToSojSessionMapFunction())
            .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
            .name("UbiSession to SojSession")
            .uid("sessionTransform");

    DataStream<UbiEvent> ubiEventWithSessionId = ubiSessionDataStream
        .getSideOutput(mappedEventOutputTag);

    // UbiEvent to SojEvent
    DataStream<SojEvent> sojEventWithSessionId = ubiEventWithSessionId
        .map(new UbiEventToSojEventMapFunction())
        .setParallelism(AppEnv.config().getFlink().app.getSessionParallelism())
        .name("UbiEvent to SojEvent")
        .uid("eventTransform");

    // This path is for local test. For production, we should use
    // "hdfs://apollo-rno//user/o_ubi/events/"
    sojSessionStream
        .addSink(HdfsSinkUtil.sojSessionSinkWithParquet())
        .setParallelism(AppEnv.config().getFlink().app.getSessionKafkaParallelism())
        .name("SojSession sink")
        .uid("sessionHdfsSink")
        .disableChaining();

    sojEventWithSessionId
        .addSink(HdfsSinkUtil.sojEventSinkWithParquet())
        .setParallelism(AppEnv.config().getFlink().app.getEventKafkaParallelism())
        .name("SojEvent sink")
        .uid("eventHdfsSink")
        .disableChaining();

    // Submit this job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getNameForDQPipeline());
  }
}
