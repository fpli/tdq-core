package com.ebay.sojourner.ubd.pipeline;

import com.ebay.sojourner.ubd.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.model.UbiSession;
import com.ebay.sojourner.ubd.operators.parser.EventParserMapFunction;
import com.ebay.sojourner.ubd.operators.sessionizer.UbiSessionReducer;
import com.ebay.sojourner.ubd.operators.sessionizer.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.util.Property;
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
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.util.OutputTag;

import java.io.File;
import java.io.FileOutputStream;

public class UBDStreamingJob {

    private static void uploadFiles(
            StreamExecutionEnvironment executionEnvironment,
            ParameterTool params) {
        String configFile = params.get("config","./src/main/resources/ubi.properties");
        executionEnvironment.registerCachedFile(configFile,"configFile");

        String iframePageIds = params.get("lookup/iframePageIds","./src/main/resources/lookup/iframePageIds");
        executionEnvironment.registerCachedFile(iframePageIds, UBIConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));

        String findingFlag = params.get("lookup/findingFlag","./src/main/resources/lookup/findingFlag");
        executionEnvironment.registerCachedFile(findingFlag, UBIConfig.getUBIProperty(Property.FINDING_FLAGS));

        String vtNewIdSource = params.get("lookup/vtNewIdSource","./src/main/resources/lookup/vtNewIdSource");
        executionEnvironment.registerCachedFile(vtNewIdSource, UBIConfig.getUBIProperty(Property.VTNEW_IDS));

        String iabAgentRex = params.get("lookup/iabAgentRex","./src/main/resources/lookup/iabAgentRex");
        executionEnvironment.registerCachedFile(iabAgentRex, UBIConfig.getUBIProperty(Property.IAB_AGENT));

        String appid = params.get("lookup/appid","./src/main/resources/lookup/appid");
        executionEnvironment.registerCachedFile(appid, UBIConfig.getUBIProperty(Property.APP_ID));


        String testUserIds = params.get("lookup/testUserIds","./src/main/resources/lookup/testUserIds");
        executionEnvironment.registerCachedFile(testUserIds, UBIConfig.getUBIProperty(Property.TEST_USER_IDS));

        String largeSessionGuid = params.get("lookup/largeSessionGuid","./src/main/resources/lookup/largeSessionGuid");
        executionEnvironment.registerCachedFile(largeSessionGuid, UBIConfig.getUBIProperty(Property.LARGE_SESSION_GUID));

        String pageFmly = params.get("lookup/pageFmly","./src/main/resources/lookup/pageFmly");
        executionEnvironment.registerCachedFile(pageFmly, UBIConfig.getUBIProperty(Property.PAGE_FMLY));

        String mpx = params.get("lookup/mpx","./src/main/resources/lookup/mpx");
        executionEnvironment.registerCachedFile(mpx, UBIConfig.getUBIProperty(Property.MPX_ROTATION));
    }

    public static void main(String[] args) throws Exception {
        UBIConfig.initAppConfiguration(new File("./src/main/resources/ubi.properties"));
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        uploadFiles(executionEnvironment, params);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(60 * 1000);
        executionEnvironment.setStateBackend(
                (StateBackend) new FsStateBackend("file:///tmp/sojourner-ubd/checkpoint"));
        executionEnvironment.setParallelism(1);

        // Consume RawEvent from Rheos
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer()).assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
            @Override
            public long extractTimestamp(RawEvent element) {
                return element.getRheosHeader().getEventCreateTimestamp();
            }
        });

        // Parse and transform RawEvent to UbiEvent
        DataStream<UbiEvent> ubiEventDataStream =  rawEventDataStream.map(new EventParserMapFunction());

        // Sessionization
         OutputTag<UbiSession> outputTag = new OutputTag<UbiSession>("session-output", TypeInformation.of(UbiSession.class));
        SingleOutputStreamOperator<UbiEvent> ubiEventSingleOutputStreamOperator =   ubiEventDataStream
                .keyBy("guid")
                .window(EventTimeSessionWindows.withGap(Time.minutes(1)))
                .trigger(CountTrigger.of(1))
                .allowedLateness(Time.hours(1))
                .reduce(new UbiSessionReducer(), new UbiSessionWindowProcessFunction(outputTag));
        DataStream<UbiSession> sideOutputStream = ubiEventSingleOutputStreamOperator.getSideOutput(outputTag);
        //ubiEventSingleOutputStreamOperator.print();

        final String sessionOutputPath = "/tmp/sojourner-ubd/data/session-output.txt";
        final StreamingFileSink<UbiSession> sessionSink = StreamingFileSink
                .forRowFormat(new Path(sessionOutputPath), new SimpleStringEncoder<UbiSession>("UTF-8"))
                .build();
        sideOutputStream.addSink(sessionSink);

        // Submit dataflow
        executionEnvironment.execute("unified bot detection");
    }
}
