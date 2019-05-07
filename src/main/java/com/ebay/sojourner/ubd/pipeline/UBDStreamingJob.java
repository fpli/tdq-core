package com.ebay.sojourner.ubd.pipeline;

import com.ebay.sojourner.ubd.common.operators.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.model.UbiSession;
import com.ebay.sojourner.ubd.operators.parser.EventParserMapFunction;
import com.ebay.sojourner.ubd.operators.sessionizer.UbiSessionAgg;
import com.ebay.sojourner.ubd.operators.sessionizer.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.util.LookupUtils;
import com.ebay.sojourner.ubd.util.SojJobParameters;
import com.ebay.sojourner.ubd.util.UBIConfig;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
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

public class UBDStreamingJob {

    public static void main(String[] args) throws Exception {
        UBIConfig ubiConfig = UBIConfig.getInstance();
        ubiConfig.initAppConfiguration(new File("./src/main/resources/ubi.properties"));

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        LookupUtils.uploadFiles(executionEnvironment, params,ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(60 * 1000);
        executionEnvironment.setStateBackend(
                (StateBackend) new FsStateBackend("file:///tmp/sojourner-ubd/checkpoint"));
        executionEnvironment.setParallelism(1);

        // Consume RawEvent from Rheos PathFinder Topic, assign timestamps and emit watermarks.
        DataStream<RawEvent> rawEventDataStream = executionEnvironment
                .addSource(KafkaConnectorFactory.createKafkaConsumer())
                .name("Rheos PathFinder Raw Event Consumer")
                .assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(RawEvent element) {
                return element.getRheosHeader().getEventCreateTimestamp();
            }
        });

        // Parse and transform RawEvent to UbiEvent
        DataStream<UbiEvent> ubiEventDataStream = rawEventDataStream
                .map(new EventParserMapFunction())
                .name("UBI Event Parser")
                .startNewChain();

        // Do sessionization and calculate session metrics
        OutputTag<UbiSession> sessionOutputTag = new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
        OutputTag<UbiEvent> lateEventOutputTag = new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
        SingleOutputStreamOperator<UbiEvent> ubiEventStreamWithSessionId = ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .sideOutputLateData(lateEventOutputTag)
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction(sessionOutputTag))
                .name("Sessionizer & Session Metrics Calculator");

        // Load data to file system for batch processing
        // events with session id
        final String eventSinkPath = "/tmp/sojourner-ubd/data/events-with-session-id";
        final StreamingFileSink<UbiEvent> eventSink = StreamingFileSink
                .forRowFormat(new Path(eventSinkPath), new SimpleStringEncoder<UbiEvent>("UTF-8"))
                .build();
        ubiEventStreamWithSessionId.addSink(eventSink).name("Events with Session Id").disableChaining();
        // sessions ended
        DataStream<UbiSession> sessionStream = ubiEventStreamWithSessionId.getSideOutput(sessionOutputTag);
        final String sessionSinkPath = "/tmp/sojourner-ubd/data/sessions-ended";
        final StreamingFileSink<UbiSession> sessionSink = StreamingFileSink
                .forRowFormat(new Path(sessionSinkPath), new SimpleStringEncoder<UbiSession>("UTF-8"))
                .build();
        sessionStream.addSink(sessionSink).name("Sessions Ended").disableChaining();
        // events late
        DataStream<UbiEvent> lateEventStream = ubiEventStreamWithSessionId.getSideOutput(lateEventOutputTag);
        final String lateEventSinkPath = "/tmp/sojourner-ubd/data/events-late";
        final StreamingFileSink<UbiEvent> lateEventSink = StreamingFileSink
                .forRowFormat(new Path(lateEventSinkPath), new SimpleStringEncoder<UbiEvent>("UTF-8"))
                .build();
        lateEventStream.addSink(lateEventSink).name("Events Late").disableChaining();

        // Attribute level bot indicators
        sessionStream
                .keyBy("agentInfo","clientIp")
                .window(SlidingEventTimeWindows.of(Time.hours(24), Time.hours(1)));

        // Submit dataflow
        executionEnvironment.execute("Unified Bot Detection");
    }

}
