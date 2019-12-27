package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.RheosHeader;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForPA;
import com.ebay.sojourner.ubd.rt.util.SojJobParameters;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;


public class SojournerUBDRTJobForPA {

    public static void main(String[] args) throws Exception {
        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
//        InputStream resourceAsStream = SojournerUBDRTJob.class.getResourceAsStream("/ubi.properties");
//        UBIConfig ubiConfig = UBIConfig.getInstance(resourceAsStream);

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
//        final ParameterTool params = ParameterTool.fromArgs(args);
//        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
        // LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
        executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
        executionEnvironment.enableCheckpointing(180 * 1000);
        executionEnvironment.getCheckpointConfig().setCheckpointTimeout(10 * 60 * 1000);
        executionEnvironment.setStateBackend(
                StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));
        executionEnvironment.setParallelism(2);



        // for soj nrt output
        // 1. Rheos Consumer
        // 1.1 Consume RawEvent from Rheos PathFinder topic
        // 1.2 Assign timestamps and emit watermarks.
        DataStream<GenericRecord> rawEventDataStream = executionEnvironment.addSource(
                KafkaConnectorFactoryForPA.createKafkaConsumer().assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<GenericRecord>(Time.seconds(10)) {
                            @Override
                            public long extractTimestamp(GenericRecord element) {
                                return ((RheosHeader)element.get("rheosHeader")).getEventCreateTimestamp();
                            }
                        }
                ))
                .name("Rheos Consumer");



        rawEventDataStream.print().name("IP Signature");
//        agentIpAttributeDataStream.print().name("AgentIp Signature").disableChaining();


        // Submit this job
        executionEnvironment.execute("Unified Bot Detection RT Pipeline");

    }

}
