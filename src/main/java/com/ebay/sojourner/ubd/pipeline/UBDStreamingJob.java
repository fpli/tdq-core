package com.ebay.sojourner.ubd.pipeline;

import com.ebay.sojourner.ubd.connectors.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.functions.EventParserMapFunction;
import com.ebay.sojourner.ubd.model.RawEvent;
import com.ebay.sojourner.ubd.model.UbiEvent;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class UBDStreamingJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        final ParameterTool params = ParameterTool.fromArgs(args);

        String configFile = params.get("config","./src/main/resources/ubi.properties");
        executionEnvironment.registerCachedFile(configFile,"configFile");

        String iframePageIds = params.get("iframePageIds","./src/main/resources/iframePageIds");
        executionEnvironment.registerCachedFile(iframePageIds,"iframePageIds");

        String findingFlag = params.get("findingFlag","./src/main/resources/findingFlag");
        executionEnvironment.registerCachedFile(findingFlag,"findingFlag");

        String vtNewIdSource = params.get("vtNewIdSource","./src/main/resources/vtNewIdSource");
        executionEnvironment.registerCachedFile(vtNewIdSource,"vtNewIdSource");

        String iabAgentRex = params.get("iabAgentRex","./src/main/resources/iabAgentRex");
        executionEnvironment.registerCachedFile(iabAgentRex,"iabAgentRex");

        String appid = params.get("appid","./src/main/resources/appid");
        executionEnvironment.registerCachedFile(appid,"appid");


        String testUserIds = params.get("testUserIds","./src/main/resources/testUserIds");
        executionEnvironment.registerCachedFile(testUserIds,"testUserIds");

        String largeSessionGuid = params.get("largeSessionGuid","./src/main/resources/largeSessionGuid");
        executionEnvironment.registerCachedFile(largeSessionGuid,"largeSessionGuid");

        String pageFmly = params.get("pageFmly","./src/main/resources/pageFmly");
        executionEnvironment.registerCachedFile(pageFmly,"pageFmly");

        String mpx = params.get("mpx","./src/main/resources/mpx");
        executionEnvironment.registerCachedFile(mpx,"mpx");

        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(5000);
        DataStream<RawEvent> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactory.createKafkaConsumer());
        DataStream<UbiEvent> ubiEventDataStream =  rawEventDataStream.map(new EventParserMapFunction());
        ubiEventDataStream.print();

        executionEnvironment.execute("unified bot detection");

    }
}
