package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.RheosEventDeserializationSchema;
import com.ebay.tdq.planner.LkpRefreshTimeTask;
import com.ebay.tdq.planner.Refreshable;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigManager implements Refreshable {

  private static volatile TdqConfigManager lkpManager;
  private final LkpRefreshTimeTask lkpRefreshTimeTask;
  private final JdbcEnv jdbc;
  private List<TdqConfig> tdqConfigs = new CopyOnWriteArrayList<>();
  private List<PhysicalPlan> physicalPlans = new CopyOnWriteArrayList<>();

  private TdqConfigManager(TimeUnit timeUnit, JdbcEnv jdbc) {
    this.jdbc = jdbc;
    this.lkpRefreshTimeTask = new LkpRefreshTimeTask(this, timeUnit);
    refresh();
  }

  private TdqConfigManager(JdbcEnv jdbc) {
    this(TimeUnit.MINUTES, jdbc);
  }

  public static TdqConfigManager getInstance(JdbcEnv jdbc) {
    if (lkpManager == null) {
      synchronized (TdqConfigManager.class) {
        if (lkpManager == null) {
          lkpManager = new TdqConfigManager(jdbc);
        }
      }
    }
    return lkpManager;
  }

  public void refresh() {
    freshTdqConfigs();
    log.info("refresh success!");
  }

  public void freshTdqConfigs() {
    try {
      List<TdqConfig> tdqConfigList = new CopyOnWriteArrayList<>();
      Class.forName(jdbc.getDriverClassName());
      Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
      ResultSet rs = conn.createStatement().executeQuery("select config from rhs_config where status='ACTIVE'");
      while (rs.next()) {
        String json = rs.getString("config");
        log.warn("getTdqConfigs={}", json.replace("\n", ""));
        tdqConfigList.add(JsonUtils.parseObject(json, TdqConfig.class));
      }
      conn.close();
      this.tdqConfigs = tdqConfigList;

      List<PhysicalPlan> plans = new CopyOnWriteArrayList<>();
      for (TdqConfig config : tdqConfigList) {
        Schema schema = null;
        if (CollectionUtils.isNotEmpty(config.getSources()) &&
            config.getSources().get(0).getType().equals("realtime.kafka")) {
          // tdq config must have same schema
          KafkaSourceConfig ksc = KafkaSourceConfig.build(config.getSources().get(0));
          if (ksc.getDeserializer().equals(
              "com.ebay.tdq.connector.kafka.schema.RheosEventDeserializationSchema")) {
            RheosEventDeserializationSchema deserializer = new RheosEventDeserializationSchema(
                ksc.getRheosServicesUrls(), ksc.getEndOfStreamTimestamp(),
                ksc.getEventTimeField(), ksc.getSchemaSubject());
            schema = deserializer.getSchema();
          }
        }

        for (RuleConfig ruleConfig : config.getRules()) {
          for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
            try {
              ProfilingSqlParser parser = new ProfilingSqlParser(
                  profilerConfig,
                  DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString()),
                  jdbc,
                  schema);
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

      this.physicalPlans = plans;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() {
    this.lkpRefreshTimeTask.cancel();
  }

  public TdqConfig findTdqConfig(String name) {
    for (TdqConfig c : tdqConfigs) {
      if (c.getName() != null && c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

  public List<TdqConfig> getTdqConfigs() {
    return tdqConfigs;
  }

  public List<PhysicalPlan> getPhysicalPlans() {
    return physicalPlans;
  }
}
