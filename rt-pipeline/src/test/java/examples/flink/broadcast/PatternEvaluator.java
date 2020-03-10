package examples.flink.broadcast;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class PatternEvaluator
    extends KeyedBroadcastProcessFunction<Long, Action, Pattern, Tuple2<Long, Pattern>> {

  // handle for keyed state (per user)
  ValueState<ActionType> prevActionState;

  @Override
  public void open(Configuration conf) {
    // initialize keyed state
    prevActionState =
        getRuntimeContext()
            .getState(new ValueStateDescriptor<>("lastAction", Types.ENUM(ActionType.class)));
  }

  /**
   * Called for each user action. Evaluates the current pattern against the previous and current
   * action of the user.
   */
  @Override
  public void processElement(
      Action action, ReadOnlyContext ctx, Collector<Tuple2<Long, Pattern>> out) throws Exception {
    // get current pattern from broadcast state
    Pattern pattern =
        ctx.getBroadcastState(
            new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class)))
            // access MapState with null as VOID default value
            .get(null);
    // get previous action of current user from keyed state
    ActionType prevAction = prevActionState.value();
    if (pattern != null && prevAction != null) {
      // user had an action before, check if pattern matches
      if (pattern.getFirstAction().equals(prevAction)
          && pattern.getSecondAction().equals(action.getAction())) {
        // MATCH
        out.collect(new Tuple2<>(ctx.getCurrentKey(), pattern));
      }
    }
    // update keyed state and remember action for next pattern evaluation
    prevActionState.update(action.getAction());
  }

  /**
   * Called for each new pattern. Overwrites the current pattern with the new pattern.
   */
  @Override
  public void processBroadcastElement(
      Pattern pattern, Context ctx, Collector<Tuple2<Long, Pattern>> out) throws Exception {
    // store the new pattern by updating the broadcast state
    BroadcastState<Void, Pattern> bcState =
        ctx.getBroadcastState(
            new MapStateDescriptor<>("patterns", Types.VOID, Types.POJO(Pattern.class)));
    // storing in MapState with null as VOID default value
    bcState.put(null, pattern);
  }
}
