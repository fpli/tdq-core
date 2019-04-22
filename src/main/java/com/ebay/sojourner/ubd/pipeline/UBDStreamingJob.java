package com.ebay.sojourner.ubd.pipeline;

import com.ebay.sojourner.ubd.common.operators.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.model.UbiSession;
import com.ebay.sojourner.ubd.operators.parser.EventParserMapFunction;
import com.ebay.sojourner.ubd.operators.sessionizer.UbiSessionAgg;
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
import org.apache.flink.util.OutputTag;

import java.io.File;

public class UBDStreamingJob {

    private static void uploadFiles(
            StreamExecutionEnvironment executionEnvironment,
            ParameterTool params,
            UBIConfig ubiConfig) {
        String configFile = params.get("config","./src/main/resources/ubi.properties");
        executionEnvironment.registerCachedFile(configFile,"configFile");

        String iframePageIds = params.get("lookup/iframePageIds","./src/main/resources/lookup/iframePageIds");
        executionEnvironment.registerCachedFile(iframePageIds, ubiConfig.getUBIProperty(Property.IFRAME_PAGE_IDS));

        String findingFlag = params.get("lookup/findingFlag","./src/main/resources/lookup/findingFlag");
        executionEnvironment.registerCachedFile(findingFlag, ubiConfig.getUBIProperty(Property.FINDING_FLAGS));

        String vtNewIdSource = params.get("lookup/vtNewIdSource","./src/main/resources/lookup/vtNewIdSource");
        executionEnvironment.registerCachedFile(vtNewIdSource, ubiConfig.getUBIProperty(Property.VTNEW_IDS));

        String iabAgentRex = params.get("lookup/iabAgentRex","./src/main/resources/lookup/iabAgentRex");
        executionEnvironment.registerCachedFile(iabAgentRex, ubiConfig.getUBIProperty(Property.IAB_AGENT));

        String appid = params.get("lookup/appid","./src/main/resources/lookup/appid");
        executionEnvironment.registerCachedFile(appid, ubiConfig.getUBIProperty(Property.APP_ID));


        String testUserIds = params.get("lookup/testUserIds","./src/main/resources/lookup/testUserIds");
        executionEnvironment.registerCachedFile(testUserIds, ubiConfig.getUBIProperty(Property.TEST_USER_IDS));

        String largeSessionGuid = params.get("lookup/largeSessionGuid","./src/main/resources/lookup/largeSessionGuid");
        executionEnvironment.registerCachedFile(largeSessionGuid, ubiConfig.getUBIProperty(Property.LARGE_SESSION_GUID));

        String pageFmly = params.get("lookup/pageFmly","./src/main/resources/lookup/pageFmly");
        executionEnvironment.registerCachedFile(pageFmly, ubiConfig.getUBIProperty(Property.PAGE_FMLY));

        String mpx = params.get("lookup/mpx","./src/main/resources/lookup/mpx");
        executionEnvironment.registerCachedFile(mpx, ubiConfig.getUBIProperty(Property.MPX_ROTATION));
    }

    public static void main(String[] args) throws Exception {
        UBIConfig ubiConfig = UBIConfig.getInstance();
        ubiConfig.initAppConfiguration(new File("./src/main/resources/ubi.properties"));

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        final ParameterTool params = ParameterTool.fromArgs(args);
        uploadFiles(executionEnvironment, params,ubiConfig);
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
                .trigger(OnElementEarlyFiringTrigger.create())
                .allowedLateness(Time.hours(1))
                .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction(outputTag));
//                .reduce(new UbiSessionReducer(), new UbiSessionWindowProcessFunction(outputTag));
        //ubiEventSingleOutputStreamOperator.print();
          DataStream<UbiSession> sideOutputStream = ubiEventSingleOutputStreamOperator.getSideOutput(outputTag);


        final String sessionOutputPath = "c:\\tmp\\sojourner-ubd\\data\\session-output";
        final StreamingFileSink<UbiSession> sessionSink = StreamingFileSink
                .forRowFormat(new Path(sessionOutputPath), new SimpleStringEncoder<UbiSession>("UTF-8"))
                .build();
        final String eventOutputPath = "c:\\tmp\\sojourner-ubd\\data\\event-output";
        final StreamingFileSink<UbiEvent> eventSink = StreamingFileSink
                .forRowFormat(new Path(eventOutputPath), new SimpleStringEncoder<UbiEvent>("UTF-8"))
                .build();


        sideOutputStream.addSink(sessionSink);
        ubiEventSingleOutputStreamOperator.addSink(eventSink);
        // Submit dataflow
        executionEnvironment.execute("unified bot detection");
    }


}
