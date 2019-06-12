package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.IpSignature;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.StreamingFileSinkFactory;
import com.ebay.sojourner.ubd.rt.operators.attrubite.IpWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.operators.attrubite.IpAttributeAgg;
import com.ebay.sojourner.ubd.rt.operators.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.operators.parser.EventParserMapFunction;
import com.ebay.sojourner.ubd.rt.operators.sessionizer.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.sessionizer.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.LookupUtils;
import com.ebay.sojourner.ubd.rt.util.SojJobParameters;
import com.ebay.sojourner.ubd.common.util.UBIConfig;
import com.ebay.sojourner.ubd.rt.util.StateBackendFactory;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.io.File;

public class SojournerUBDRTJob {

    public static void main(String[] args) throws Exception {
        // 0. Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
        UBIConfig ubiConfig =
                UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties"));
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(60 * 1000);
        executionEnvironment.setStateBackend(
                StateBackendFactory.getStateBackend(StateBackendFactory.FS));
        executionEnvironment.setParallelism(1);

        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer().assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(RawEvent element) {
                                return element.getRheosHeader().getEventCreateTimestamp();
                            }
                        }
                ))
                .name("Rheos Consumer");

        // 2. Event Operator
        // 2.1 Parse and transform RawEvent to UbiEvent
        // 2.2 Event level bot detection via bot rule
        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
                .map(new EventParserMapFunction())
                .name("Event Operator")
                .startNewChain();

        // 3. Session Operator
        // 3.1 Session window
        // 3.2 Session indicator accumulation
        // 3.3 Session Level bot detection (via bot rule & signature)
        // 3.4 Event level bot detection (via session flag)
        OutputTag<UbiSession> sessionOutputTag =
                new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
        OutputTag<UbiEvent> lateEventOutputTag =
                new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
        JobID jobId = executionEnvironment.getStreamGraph().getJobGraph().getJobID();
        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(),
                        new UbiSessionWindowProcessFunction(sessionOutputTag))
                .name("Session Operator");
        DataStream<UbiSession> sessionStream =
                ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag); // sessions ended

        // 4. Attribute Operator
        // 4.1 Sliding window
        // 4.2 Attribute indicator accumulation
        // 4.3 Attribute level bot detection (via bot rule)
        // 4.4 Store bot signature
        DataStream<IpSignature> ipSignatureDataStream = sessionStream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
                .name("Attribute Operator (IP)");

        // 5. Load data to file system for batch processing
        // 5.1 Events with session ID
        // 5.2 Sessions ended
        // 5.3 Events late
        // 5.4 IP Signature
        ubiEventStreamWithSessionId.addSink(StreamingFileSinkFactory.eventSink())
                .name("Events with Session Id").disableChaining();
        sessionStream.addSink(StreamingFileSinkFactory.sessionSink())
                .name("Sessions Ended").disableChaining();
        DataStream<UbiEvent> lateEventStream =
                ubiEventStreamWithSessionId.getSideOutput(lateEventOutputTag);
        lateEventStream.addSink(StreamingFileSinkFactory.lateEventSink())
                .name("Events Late").disableChaining();
        ipSignatureDataStream.addSink(StreamingFileSinkFactory.ipSignatureSink())
                .name("IP Signature").disableChaining();

        // Submit this job
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");


    }

}
