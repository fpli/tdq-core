package com.ebay.sojourner.rt.pipeline;

import com.ebay.sojourner.common.model.IntermediateSession;
import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.flink.common.util.Constants;
import com.ebay.sojourner.flink.common.env.FlinkEnvUtils;
import com.ebay.sojourner.flink.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.flink.connectors.kafka.KafkaSourceFunction;
import com.ebay.sojourner.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.rt.operators.session.UbiSessionToIntermediateSessionMapFunction;
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

public class SojournerRTJobForSessionDataCopy {

  public static void main(String[] args) throws Exception {

    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment = FlinkEnvUtils.prepare(args);

    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic(RNO/LVS/SLC)
    // 1.2 Assign timestamps and emit watermarks.
    DataStream<RawEvent> rawEventDataStreamForRNO =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_RNO),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_RNO),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For RNO")
            .uid("source-rno-id");

    DataStream<RawEvent> rawEventDataStreamForSLC =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_SLC),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_SLC),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For SLC")
            .uid("source-slc-id");

    DataStream<RawEvent> rawEventDataStreamForLVS =
        executionEnvironment
            .addSource(KafkaSourceFunction
                .buildSource(FlinkEnvUtils.getString(Constants.BEHAVIOR_PATHFINDER_TOPIC),
                    FlinkEnvUtils
                        .getListString(Constants.BEHAVIOR_PATHFINDER_BOOTSTRAP_SERVERS_LVS),
                    FlinkEnvUtils
                        .getString(Constants.BEHAVIOR_PATHFINDER_GROUP_ID_DEFAULT_LVS),
                    RawEvent.class))
            .setParallelism(FlinkEnvUtils.getInteger(Constants.SOURCE_PARALLELISM))
            .name("Rheos Kafka Consumer For LVS")
            .uid("source-lvs-id");

    // 2. Event Operator
    // 2.1 Parse and transform RawEvent to UbiEvent
    // 2.2 Event level bot detection via bot rule
    DataStream<UbiEvent> ubiEventDataStreamForRNO = rawEventDataStreamForRNO
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .name("Event Operator For RNO")
        .uid("event-rno-id");

    DataStream<UbiEvent> ubiEventDataStreamForLVS = rawEventDataStreamForLVS
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .name("Event Operator For LVS")
        .uid("event-lvs-id");

    DataStream<UbiEvent> ubiEventDataStreamForSLC = rawEventDataStreamForSLC
        .map(new EventMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.EVENT_PARALLELISM))
        .name("Event Operator For SLC")
        .uid("event-slc-id");

    // union ubiEvent from SLC/RNO/LVS
    DataStream<UbiEvent> ubiEventDataStream = ubiEventDataStreamForLVS
        .union(ubiEventDataStreamForSLC)
        .union(ubiEventDataStreamForRNO);

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
            .allowedLateness(Time.minutes(3))
            .sideOutputLateData(lateEventOutputTag)
            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());

    WindowOperatorHelper.enrichWindowOperator(
        (OneInputTransformation) ubiSessionDataStream.getTransformation(),
        new UbiEventMapWithStateFunction(),
        mappedEventOutputTag);

    ubiSessionDataStream
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .name("Session Operator")
        .uid("session-id");

    DataStream<IntermediateSession> intermediateSessionDataStream = ubiSessionDataStream
        .map(new UbiSessionToIntermediateSessionMapFunction())
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .name("IntermediateSession For Cross Session DQ")
        .uid("session-enhance-id");

    // sink for cross session dq
    intermediateSessionDataStream
        .addSink(KafkaConnectorFactory
            .createKafkaProducer(
                FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_TOPIC_DQ_CROSS_SESSION),
                FlinkEnvUtils.getListString(Constants.BEHAVIOR_TOTAL_NEW_BOOTSTRAP_SERVERS_DEFAULT),
                IntermediateSession.class,
                FlinkEnvUtils.getString(Constants.BEHAVIOR_TOTAL_NEW_MESSAGE_KEY_SESSION)))
        .setParallelism(FlinkEnvUtils.getInteger(Constants.SESSION_PARALLELISM))
        .name("IntermediateSession")
        .uid("intermediate-session-sink-id");

    // Submit this job
    FlinkEnvUtils
        .execute(executionEnvironment, FlinkEnvUtils.getString(Constants.NAME_DATA_QUALITY));
  }
}

