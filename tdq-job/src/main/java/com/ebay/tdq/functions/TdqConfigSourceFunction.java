package com.ebay.tdq.functions;

import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.common.env.JdbcEnv;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import static com.ebay.tdq.utils.PhysicalPlanFactory.getPhysicalPlans;
import static com.ebay.tdq.utils.PhysicalPlanFactory.getTdqConfigs;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigSourceFunction extends RichSourceFunction<PhysicalPlans> {
  private final Long interval;
  private final JdbcEnv jdbcConfig;
  private volatile boolean running = true;

  public TdqConfigSourceFunction(Long interval) {
    this.interval = interval;
    jdbcConfig    = new JdbcEnv();
  }


  @Override
  public void run(SourceContext<PhysicalPlans> ctx) throws Exception {
    while (running) {
      long t = System.currentTimeMillis();
      ctx.collectWithTimestamp(getPhysicalPlans(getTdqConfigs(jdbcConfig)), t);
      TimeUnit.SECONDS.sleep(interval);
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}
