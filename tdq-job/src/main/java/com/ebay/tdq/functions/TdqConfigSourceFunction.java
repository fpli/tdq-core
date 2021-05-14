package com.ebay.tdq.functions;

import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.utils.JdbcConfig;
import com.ebay.tdq.utils.PhysicalPlanFactory;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigSourceFunction extends RichSourceFunction<PhysicalPlan> {
  private final Long interval;
  private final JdbcConfig jdbcConfig;
  private boolean running = true;

  public TdqConfigSourceFunction(Long interval) {
    this.interval = interval;
    jdbcConfig    = new JdbcConfig();
  }


  @Override
  public void run(SourceContext<PhysicalPlan> ctx) throws Exception {
    while (running) {
      long t = System.currentTimeMillis();
      for (PhysicalPlan plan : PhysicalPlanFactory.getPhysicalPlanMap(this.jdbcConfig).values()) {
        log.warn("TdqConfigSourceFunction={}", plan);
        ctx.collectWithTimestamp(plan, t);
      }
      TimeUnit.SECONDS.sleep(interval);
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}
