package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.ProfilingSqlParser;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntzhang
 */
@Slf4j
public class PhysicalPlanFactory {

  public static PhysicalPlans getPhysicalPlans(JdbcEnv jdbcEnv) {
    List<TdqConfig> configs = LkpManager.getInstance(jdbcEnv).getTdqConfigs();
    List<PhysicalPlan> plans = new ArrayList<>();
    for (TdqConfig config : configs) {
      for (RuleConfig ruleConfig : config.getRules()) {
        for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
          try {
            ProfilingSqlParser parser = new ProfilingSqlParser(
                profilerConfig,
                DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString()),
                jdbcEnv
            );
            PhysicalPlan plan = parser.parsePlan();
            plan.validatePlan();
            log.warn("TdqConfigSourceFunction={}", plan);
            plans.add(plan);
          } catch (Exception e) {
            log.warn("profilerConfig[" + profilerConfig + "] validate exception:" + e.getMessage(), e);
          }
        }
      }
    }
    return PhysicalPlans.apply(plans.toArray(new PhysicalPlan[0]));
  }
}
