package com.ebay.tdq.functions;

import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * TODO get from DB or http request https://blog.csdn.net/qq_31866793/article/details/105950810
 * add metric in flink
 *
 * @author juntzhang
 */
@Slf4j
public class TdqConfigSourceFunction extends RichSourceFunction<PhysicalPlan> {
  private final String baseURL;
  private final Long interval;
  private final String env;

  public TdqConfigSourceFunction(String baseURL, Long interval, String env) {
    this.baseURL  = baseURL;
    this.interval = interval;
    this.env      = env;
  }

  private static TdqConfig getTdqConfig() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper
        .reader().forType(TdqConfig.class)
        .readValue(TdqConfigSourceFunction.class
            .getResourceAsStream("/tdq_rules.json"));
  }

  public static Map<String, PhysicalPlan> getPhysicalPlanMap() throws IOException {
    TdqConfig config = getTdqConfig();
    Map<String, PhysicalPlan> planMap = new HashMap<>();
    for (RuleConfig ruleConfig : config.getRules()) {
      for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
        try {
          ProfilingSqlParser parser = new ProfilingSqlParser(
              profilerConfig,
              DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString())
          );
          PhysicalPlan plan = parser.parsePlan();
          plan.validatePlan();
          planMap.put(plan.metricKey(), plan);
        } catch (Exception e) {
          log.warn("profilerConfig[" + profilerConfig + "] validate exception:"
              + e.getMessage(), e);
        }
      }
    }
    return planMap;
  }

  @Override
  public void run(SourceContext<PhysicalPlan> ctx) throws Exception {
    while (true) {
      long t = System.currentTimeMillis();
      for (PhysicalPlan plan : getPhysicalPlanMap().values()) {
        log.warn("TdqConfigSourceFunction={}", plan);
        ctx.collectWithTimestamp(plan, t);
      }

      Thread.sleep(interval * 1000);
    }
  }

  @Override
  public void cancel() {
  }
}
