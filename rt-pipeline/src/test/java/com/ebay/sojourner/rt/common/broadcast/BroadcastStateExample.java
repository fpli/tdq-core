package com.ebay.sojourner.rt.common.broadcast;

import static com.ebay.sojourner.rt.common.broadcast.ActionType.ADD_TO_CART;
import static com.ebay.sojourner.rt.common.broadcast.ActionType.LOGIN;
import static com.ebay.sojourner.rt.common.broadcast.ActionType.LOGOUT;
import static com.ebay.sojourner.rt.common.broadcast.ActionType.PAYMENT_COMPLETE;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BroadcastStateExample {

  public static void main(String[] args) throws Exception {
    // set up the streaming execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    DataStream<Pattern> patterns = env.fromElements(new Pattern(LOGIN, LOGOUT));
    MapStateDescriptor<Void, Pattern> bcStateDescriptor =
        new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class));
    BroadcastStream<Pattern> bcedPatterns = patterns.broadcast(bcStateDescriptor);

    DataStream<Action> actions =
        env.fromElements(
            new Action(1001L, LOGIN),
            new Action(1003L, PAYMENT_COMPLETE),
            new Action(1002L, ADD_TO_CART),
            new Action(1001L, LOGOUT),
            new Action(1003L, ADD_TO_CART),
            new Action(1002L, LOGOUT));
    KeyedStream<Action, Long> actionsByUser =
        actions.keyBy((KeySelector<Action, Long>) action -> action.getUserId());

    DataStream<Tuple2<Long, Pattern>> matches =
        actionsByUser.connect(bcedPatterns).process(new PatternEvaluator());
    matches.print();

    // execute program
    System.out.println(env.getExecutionPlan());
    env.execute("Broadcast State Example");
  }
}
