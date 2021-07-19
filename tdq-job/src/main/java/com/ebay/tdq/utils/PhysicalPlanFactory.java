package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.PhysicalPlans;
import com.ebay.tdq.rules.ProfilingSqlParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntzhang
 */
@Slf4j
public class PhysicalPlanFactory {
  public static PhysicalPlans getPhysicalPlans(List<TdqConfig> configs) {
    List<PhysicalPlan> plans = new ArrayList<>();
    for (TdqConfig config : configs) {
      for (RuleConfig ruleConfig : config.getRules()) {
        for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
          try {
            ProfilingSqlParser parser = new ProfilingSqlParser(
                profilerConfig,
                DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString())
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

  public static List<TdqConfig> getTdqConfigs(JdbcEnv jdbc) {
    try {
      List<TdqConfig> tdqConfigList = new ArrayList<>();
      Class.forName(jdbc.getDriverClassName());
      Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
      ResultSet rs = conn.createStatement().executeQuery("select config from rules_rhs_config where status='ACTIVE'");
      while (rs.next()) {
        String json = rs.getString("config");
        log.warn("getTdqConfigs={}", json);
        tdqConfigList.add(JsonUtils.parseObject(json, TdqConfig.class));
      }
      conn.close();
      return tdqConfigList;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
