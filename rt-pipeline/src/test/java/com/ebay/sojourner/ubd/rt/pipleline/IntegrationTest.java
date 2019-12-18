//package com.ebay.sojourner.ubd.rt.pipleline;
//
//import com.ebay.sojourner.ubd.rt.IncrementMapFunction;
//import io.flinkspector.datastream.DataStreamTestBase;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.api.functions.sink.SinkFunction;
//import org.apache.flink.test.util.MiniClusterWithClientResource;
//import org.flinkspector.core.input.Input;
//import org.flinkspector.core.input.InputBuilder;
//import org.flinkspector.datastream.DataStreamTestBase;
//import org.junit.ClassRule;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.flinkspector.datastream.DataStreamTestBase.times;
//import static org.junit.Assert.assertTrue;
//
//public class IntegrationTest {
//
//    @ClassRule
//    public static MiniClusterWithClientResource flinkCluster =
//            new MiniClusterWithClientResource(
//                    new MiniClusterResourceConfiguration.Builder()
//                            .setNumberSlotsPerTaskManager(2)
//                            .setNumberTaskManagers(1)
//                            .build());
//
//
//    @Test
//    public void testIncrementPipeline() throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//         DataStreamTestBase dataStreamTestBase = new DataStreamTestBase();
//        dataStreamTestBase.initialize();
//        // configure your test environment
//        env.setParallelism(2);
//
//        // values are collected in a static variable
//        CollectSink.values.clear();
//        Input<Tuple2<String,Integer>> input = InputBuilder
//                .startWith(Tuple2.of("one",1))
//                .emit(Tuple2.of("two",2))
//                .emit(Tuple2.of("three",3))
//                .repeatAll(times(2))
//                .emit(Tuple2.of("four",3),times(3));
//        DataStream<Tuple2<String,Integer>> dataStream = dataStreamTestBase.createTestStream(input);
//
//        dataStream.map(new IncrementMapFunction())
//                .addSink(new CollectSink());
//        // create a stream of custom elements and apply transformations
////        env.fromElements(1L, 21L, 22L)
////                .map(new IncrementMapFunction())
////                .addSink(new CollectSink());
//
//        // execute
//        env.execute();
//
//        ArrayList<Long> alss = new ArrayList<Long>();
//        alss.add(2L);
//        alss.add(22L);
//        alss.add(23L);
//        // verify your results
//        assertTrue(CollectSink.values.containsAll(alss));
//    }
//
//    // create a testing sink
//    private static class CollectSink implements SinkFunction<Integer> {
//
//        // must be static
//        public static final List<Integer> values = new ArrayList<>();
//
//        @Override
//        public synchronized void invoke(Integer value) throws Exception {
//            values.add(value);
//        }
//    }
//}
