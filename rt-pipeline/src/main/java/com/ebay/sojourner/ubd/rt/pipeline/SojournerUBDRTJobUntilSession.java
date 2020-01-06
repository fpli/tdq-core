package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForSOJ;
import com.ebay.sojourner.ubd.rt.operators.event.EventFilterFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.DiscardingSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;

public class SojournerUBDRTJobUntilSession {

    public static void main(String[] args) throws Exception {
        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
//        final ParameterTool params = ParameterTool.fromArgs(args);
//        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
//         LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
//        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.setParallelism(1);

        // checkpoint settings
        executionEnvironment.enableCheckpointing(AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds() * 1000, CheckpointingMode.EXACTLY_ONCE);
        executionEnvironment.getCheckpointConfig().setCheckpointTimeout(AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);
        executionEnvironment.getCheckpointConfig().setMinPauseBetweenCheckpoints(AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);
        executionEnvironment.getCheckpointConfig().setMaxConcurrentCheckpoints(AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent()==null?
                1:AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());
        executionEnvironment.setStateBackend(
                StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));

        // for soj nrt output
        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment
                .addSource(KafkaConnectorFactoryForSOJ.createKafkaConsumer()
                        .setStartFromEarliest()
                        .assignTimestampsAndWatermarks(
                                new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                                    @Override
                                    public long extractTimestamp(RawEvent element) {
                                        return element.getRheosHeader().getEventCreateTimestamp();
                                    }
                                }))
                .setParallelism(1)
                .name("Rheos Kafka Consumer");

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
        DataStream<RawEvent> filterRawEventDataStream = rawEventDataStream
                .filter(new EventFilterFunction())
                .setParallelism(1)
                .name("filter RawEvents");

        DataStream<UbiEvent> ubiEventDataStream = filterRawEventDataStream
                .map(new EventMapFunction())
                .name("Event Operator").disableChaining();

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
//        JobID jobId = executionEnvironment.getStreamGraph().getJobGraph().getJobID();
        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(),
                        new UbiSessionWindowProcessFunction(sessionOutputTag));
        // Hack here to use MapWithStateWindowOperator instead while bypassing DataStream API which
        // cannot be enhanced easily since we do not want to modify Flink framework sourcecode.
        WindowOperatorHelper.enrichWindowOperator(
            (OneInputTransformation) ubiEventStreamWithSessionId.getTransformation(),
            mappedEventOutputTag
        );

        ubiEventStreamWithSessionId.name("Session Operator");

        DataStream<UbiSession> sessionStream =
                ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag); // sessions ended

        DataStream<UbiEvent> mappedEventStream = ubiEventStreamWithSessionId.getSideOutput(mappedEventOutputTag);
        mappedEventStream.addSink(new DiscardingSink<>()).name("Mapped UbiEvent").disableChaining();

        sessionStream.addSink(new DiscardingSink<>()).name("session").disableChaining();
        ubiEventStreamWithSessionId.addSink(new DiscardingSink<>()).name("ubiEvent with SessionId").disableChaining();

        // Submit this job
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");

    }

}
