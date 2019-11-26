package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.*;
import com.ebay.sojourner.ubd.rt.common.broadcast.AgentBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.broadcast.AgentIpBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.broadcast.IpBroadcastProcessFunction;
import com.ebay.sojourner.ubd.rt.common.state.MapStateDesc;
import com.ebay.sojourner.ubd.rt.common.windows.OnElementEarlyFiringTrigger;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactory;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForSOJ;
import com.ebay.sojourner.ubd.rt.operators.attribute.*;
import com.ebay.sojourner.ubd.rt.operators.event.AgentIpMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.SojJobParameters;
import com.ebay.sojourner.ubd.rt.util.StateBackendFactory;
import org.apache.avro.generic.GenericRecord;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.OutputTag;


public class SojournerUBDRTJobForSOJ {

    public static void main(String[] args) throws Exception {
        // 0.0 Prepare execution environment
        // 0.1 UBI configuration
        // 0.2 Flink configuration
//        InputStream resourceAsStream = SojournerUBDRTJob.class.getResourceAsStream("/ubi.properties");
//        UBIConfig ubiConfig = UBIConfig.getInstance(resourceAsStream);

        final StreamExecutionEnvironment executionEnvironment =
                StreamExecutionEnvironment.getExecutionEnvironment();
//        final ParameterTool params = ParameterTool.fromArgs(args);
        executionEnvironment.getConfig().setGlobalJobParameters(new SojJobParameters());
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
                KafkaConnectorFactoryForSOJ.createKafkaConsumer().assignTimestampsAndWatermarks(
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
