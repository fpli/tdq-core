package com.ebay.tdq.job;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.tdq.Application;
import com.ebay.tdq.rules.TdqMetric;
import com.ebay.tdq.sources.MockBehaviorPathfinderSource;
import com.ebay.tdq.utils.FlinkEnvFactory;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author juntzhang
 */
public class ApplicationTest extends Application {

  public static void main(String[] args) throws Exception {
    new ApplicationTest().start(args);
  }

  public void start(String[] args) throws Exception {
    // step0: prepare environment
    final StreamExecutionEnvironment env = FlinkEnvFactory.create(args, true);

    // step1: build data source
    DataStream<RawEvent> rawEventDataStream = MockBehaviorPathfinderSource.build(env);

    // step2: normalize event to metric
    SingleOutputStreamOperator<TdqMetric> normalizeOperator = normalizeEvent(env,
        rawEventDataStream);

    // step3: aggregate metric by key and window
    SingleOutputStreamOperator<TdqMetric> outputTagsOperator = reduceMetric(normalizeOperator);

    // step4: output metric by window
    outputMetricByWindow(outputTagsOperator);

    env.execute("Tdq Job [topic=behavior.pathfinder]");
  }

}
