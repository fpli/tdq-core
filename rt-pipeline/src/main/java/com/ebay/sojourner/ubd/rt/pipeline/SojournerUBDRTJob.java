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
        UBIConfig ubiConfig = UBIConfig.getInstance(new File("/opt/sojourner-ubd/conf/ubi.properties"));

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(60 * 1000);
        executionEnvironment.setStateBackend(StateBackendFactory.getStateBackend(StateBackendFactory.FS));
        executionEnvironment.setParallelism(1);

        // Consume RawEvent from Rheos PathFinder Topic, assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer().assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(RawEvent element) {
                                return element.getRheosHeader().getEventCreateTimestamp();
                            }
                        }
                ))
                .name("Rheos PathFinder Raw Event Consumer");

        // Parse and transform RawEvent to UbiEvent
        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
                .map(new EventParserMapFunction())
                .name("UBI Event Parser")
                .startNewChain();

        // Do sessionization and calculate session metrics
        OutputTag<UbiSession> sessionOutputTag = new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
        OutputTag<UbiEvent> lateEventOutputTag = new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
        JobID jobId = executionEnvironment.getStreamGraph().getJobGraph().getJobID();

        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction(sessionOutputTag, jobId))
                .name("Session Operator");

        // Load data to file system for batch processing
        // events with session ID
        ubiEventStreamWithSessionId.addSink(StreamingFileSinkFactory.eventSink())
                .name("Events with Session Id").disableChaining();
        // sessions ended
        DataStream<UbiSession> sessionStream =
                ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag);
        sessionStream.addSink(StreamingFileSinkFactory.sessionSink())
                .name("Sessions Ended").disableChaining();
        // events late
        DataStream<UbiEvent> lateEventStream =
                ubiEventStreamWithSessionId.getSideOutput(lateEventOutputTag);
        lateEventStream.addSink(StreamingFileSinkFactory.lateEventSink())
                .name("Events Late").disableChaining();

        // Attribute level bot indicators
        // for ip level bot detection
        DataStream<IpSignature> ipSignatureDataStream = sessionStream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction())
                .name("IP Attribute Operator");

        // Save IP Signature for load
        ipSignatureDataStream
                .keyBy("clientIp")
                .asQueryableState("bot7",
                        new ValueStateDescriptor<>("", TypeInformation.of(
                                new TypeHint<IpSignature>() {
                                }
                        ))
                );

        // Load IP Signature to file system
        ipSignatureDataStream.addSink(StreamingFileSinkFactory.ipSignatureSink())
                .name("IP Signature").disableChaining();

        // Submit dataflow
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");


    }

}
