package com.ebay.tdq.functions;

import static com.ebay.tdq.utils.PhysicalPlanFactory.getPhysicalPlans;
import static com.ebay.tdq.utils.PhysicalPlanFactory.getTdqConfigs;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.utils.TdqEnv;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigSourceFunction extends RichSourceFunction<PhysicalPlans> {

  private final TdqEnv tdqEnv;
  private final JdbcEnv jdbcConfig;
  private volatile boolean running = true;

  public TdqConfigSourceFunction(TdqEnv tdqEnv) {
    this.tdqEnv = tdqEnv;
    jdbcConfig = new JdbcEnv();
  }


  @Override
  public void run(SourceContext<PhysicalPlans> ctx) throws Exception {
    while (running) {
      long t = System.currentTimeMillis();
      ctx.collectWithTimestamp(getPhysicalPlans(getTdqConfigs(jdbcConfig)), t);
      TimeUnit.SECONDS.sleep(tdqEnv.getTdqConfigRefreshInterval());
      if (tdqEnv.getKafkaSourceEnv().isBackFill()) {
        cancel();
      }
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}
