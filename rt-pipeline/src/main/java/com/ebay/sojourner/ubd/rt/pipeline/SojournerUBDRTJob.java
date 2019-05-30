package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.IpSignature;
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
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;

import java.io.File;

public class SojournerUBDRTJob {

    public static void main(String[] args) throws Exception {
        UBIConfig ubiConfig = UBIConfig.getInstance();
        ubiConfig.initAppConfiguration(new File("/opt/sojourner-ubd/conf/ubi.properties"));

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        LookupUtils.uploadFiles(executionEnvironment, params,ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(60 * 1000);
        executionEnvironment.setStateBackend(
                (StateBackend) new FsStateBackend("file:///opt/sojourner-ubd/checkpoint"));
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
        JobID jobId=executionEnvironment.getStreamGraph().getJobGraph().getJobID();
        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.minutes(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction(sessionOutputTag,jobId))
                .name("Sessionizer & Session Metrics Calculator");

        // Load data to file system for batch processing
        // events with session id
        final String eventSinkPath = "/opt/sojourner-ubd/data/events-with-session-id";
        final StreamingFileSink<UbiEvent> eventSink = StreamingFileSink
                .forRowFormat(new Path(eventSinkPath), new SimpleStringEncoder<UbiEvent>("UTF-8"))
                .build();
        ubiEventStreamWithSessionId.addSink(eventSink).name("Events with Session Id").disableChaining();
        // sessions ended
        DataStream<UbiSession> sessionStream = ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag);
        final String sessionSinkPath = "/opt/sojourner-ubd/data/sessions-ended";
        final StreamingFileSink<UbiSession> sessionSink = StreamingFileSink
                .forRowFormat(new Path(sessionSinkPath), new SimpleStringEncoder<UbiSession>("UTF-8"))
                .build();
        sessionStream.addSink(sessionSink).name("Sessions Ended").disableChaining();
        // events late
        DataStream<UbiEvent> lateEventStream = ubiEventStreamWithSessionId.getSideOutput(lateEventOutputTag);
        final String lateEventSinkPath = "/opt/sojourner-ubd/data/events-late";
        final StreamingFileSink<UbiEvent> lateEventSink = StreamingFileSink
                .forRowFormat(new Path(lateEventSinkPath), new SimpleStringEncoder<UbiEvent>("UTF-8"))
                .build();
        lateEventStream.addSink(lateEventSink).name("Events Late").disableChaining();

        // Attribute level bot indicators
        // for ip level bot detection
        DataStream<IpSignature> ipSignatureDataStream = sessionStream
                .keyBy("clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .aggregate(new IpAttributeAgg(), new IpWindowProcessFunction());

        // Save IP Signature for query
        ipSignatureDataStream
                .keyBy("clientIp")
                .asQueryableState("bot7",new ValueStateDescriptor<>("", TypeInformation.of(new TypeHint<IpSignature>() {})));

        // Submit dataflow
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");

    }

}
