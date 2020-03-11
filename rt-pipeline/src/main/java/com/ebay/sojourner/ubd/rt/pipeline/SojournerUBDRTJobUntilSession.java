package com.ebay.sojourner.ubd.rt.pipeline;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.SojSession;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.model.UbiSession;
import com.ebay.sojourner.ubd.rt.common.state.StateBackendFactory;
import com.ebay.sojourner.ubd.rt.connectors.filesystem.HdfsSinkUtil;
import com.ebay.sojourner.ubd.rt.connectors.kafka.KafkaConnectorFactoryForSOJ;
import com.ebay.sojourner.ubd.rt.operators.event.EventMapFunction;
import com.ebay.sojourner.ubd.rt.operators.event.UbiEventMapWithStateFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionAgg;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionToSojSessionMapFunction;
import com.ebay.sojourner.ubd.rt.operators.session.UbiSessionWindowProcessFunction;
import com.ebay.sojourner.ubd.rt.util.AppEnv;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.api.common.typeinfo.SOjStringFactory;
import org.apache.flink.api.common.typeinfo.TypeInfoFactory;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.transformations.OneInputTransformation;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.runtime.operators.windowing.WindowOperatorHelper;
import org.apache.flink.util.OutputTag;

public class SojournerUBDRTJobUntilSession {

  public static void main(String[] args) throws Exception {

    System.out.println(ReflectData.AllowNull.get().getSchema(UbiEvent.class).toString());
    // Make sure this is being executed at start up.
    ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    AppEnv.config(parameterTool);

    // hack StringValue to use the version 1.10
    //        Method m = TypeExtractor.class.getDeclaredMethod("registerFactory", Type.class,
    // Class.class);
    //        m.setAccessible(true);
    //        m.invoke(null, String.class, SOjStringFactory.class);
    //        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
    Field modifiersField = TypeExtractor.class.getDeclaredField("registeredTypeInfoFactories");
    modifiersField.setAccessible(true);
    Map<Type, Class<? extends TypeInfoFactory>> registeredTypeInfoFactories =
        (Map<Type, Class<? extends TypeInfoFactory>>) modifiersField.get(null);
    registeredTypeInfoFactories.put(String.class, SOjStringFactory.class);
    modifiersField.set(null, registeredTypeInfoFactories);
    // 0.0 Prepare execution environment
    // 0.1 UBI configuration
    // 0.2 Flink configuration
    final StreamExecutionEnvironment executionEnvironment =
        StreamExecutionEnvironment.getExecutionEnvironment();
    executionEnvironment.getConfig().setGlobalJobParameters(parameterTool);
    //         LookupUtils.uploadFiles(executionEnvironment, params, ubiConfig);
    executionEnvironment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
    //        executionEnvironment.getConfig().setLatencyTrackingInterval(2000);
    //        executionEnvironment.setParallelism(1);

    // checkpoint settings
    executionEnvironment.enableCheckpointing(
        AppEnv.config().getFlink().getCheckpoint().getInterval().getSeconds() * 1000,
        CheckpointingMode.EXACTLY_ONCE);
    executionEnvironment
        .getCheckpointConfig()
        .setCheckpointTimeout(
            AppEnv.config().getFlink().getCheckpoint().getTimeout().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMinPauseBetweenCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMinPauseBetween().getSeconds() * 1000);
    executionEnvironment
        .getCheckpointConfig()
        .setMaxConcurrentCheckpoints(
            AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent() == null
                ? 1
                : AppEnv.config().getFlink().getCheckpoint().getMaxConcurrent());
    executionEnvironment.setStateBackend(
        StateBackendFactory.getStateBackend(StateBackendFactory.ROCKSDB));

    // for soj nrt output
    // 1. Rheos Consumer
    // 1.1 Consume RawEvent from Rheos PathFinder topic
    // 1.2 Assign timestamps and emit watermarks.
    DataStream<RawEvent> rawEventDataStream =
        executionEnvironment
            .addSource(
                KafkaConnectorFactoryForSOJ.createKafkaConsumer()
                    .setStartFromLatest()
                    .assignTimestampsAndWatermarks(
                        new BoundedOutOfOrdernessTimestampExtractor<RawEvent>(Time.seconds(10)) {
                          @Override
                          public long extractTimestamp(RawEvent element) {
                            return element.getRheosHeader().getEventCreateTimestamp();
                          }
                        }))
            .setParallelism(
                AppEnv.config().getFlink().getApp().getSourceParallelism() == null
                    ? 30
                    : AppEnv.config().getFlink().getApp().getSourceParallelism())
            .name("Rheos Kafka Consumer");
    rawEventDataStream.print();
//    // 2. Event Operator
//    // 2.1 Parse and transform RawEvent to UbiEvent
//    // 2.2 Event level bot detection via bot rule
//    DataStream<UbiEvent> ubiEventDataStream =
//        rawEventDataStream
//            .map(new EventMapFunction())
//            .setParallelism(AppEnv.config().getFlink().getApp().getEventParallelism())
//            .name("Event Operator");
//
//    // 3. Session Operator
//    // 3.1 Session window
//    // 3.2 Session indicator accumulation
//    // 3.3 Session Level bot detection (via bot rule & signature)
//    // 3.4 Event level bot detection (via session flag)
//    OutputTag<UbiSession> sessionOutputTag =
//        new OutputTag<>("session-output-tag", TypeInformation.of(UbiSession.class));
//    OutputTag<UbiEvent> lateEventOutputTag =
//        new OutputTag<>("late-event-output-tag", TypeInformation.of(UbiEvent.class));
//    OutputTag<UbiEvent> mappedEventOutputTag =
//        new OutputTag<>("mapped-event-output-tag", TypeInformation.of(UbiEvent.class));
//    SingleOutputStreamOperator<UbiSession> ubiSessinDataStream =
//        ubiEventDataStream
//            .keyBy("guid")
//            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
//            .allowedLateness(Time.hours(1))
//            .sideOutputLateData(lateEventOutputTag)
//            .aggregate(new UbiSessionAgg(), new UbiSessionWindowProcessFunction());
//
//    // Hack here to use MapWithStateWindowOperator instead while bypassing DataStream API which
//    // cannot be enhanced easily since we do not want to modify Flink framework sourcecode.
//    WindowOperatorHelper.enrichWindowOperator(
//        (OneInputTransformation) ubiSessinDataStream.getTransformation(),
//        new UbiEventMapWithStateFunction(),
//        mappedEventOutputTag);
//
//    ubiSessinDataStream.name("Session Operator");
//
//    // UbiSession to SojSession
//    SingleOutputStreamOperator<SojSession> sojSessionStream =
//        ubiSessinDataStream
//            .map(new UbiSessionToSojSessionMapFunction())
//            .name("UbiSession to SojSession");
//
//    // This path is for local test. For production, we should use
//    // "hdfs://apollo-rno//user/o_ubi/events/"
//    StreamingFileSink ubiSessionStreamingFileSink = HdfsSinkUtil
//        .createWithParquet("hdfs://apollo-rno//user/o_ubi/sessions/", SojSession.class);
//
//    sojSessionStream.addSink(ubiSessionStreamingFileSink).name("sojSession sink").disableChaining();
    // Submit this job
    executionEnvironment.execute(AppEnv.config().getFlink().getApp().getName());
  }
}
