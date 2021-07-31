package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.config.KafkaSourceConfig;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.RheosEventSerdeFactory;
import com.ebay.tdq.planner.LkpRefreshTimeTask;
import com.ebay.tdq.planner.Refreshable;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigManager implements Refreshable {

  private static volatile TdqConfigManager lkpManager;
  private final LkpRefreshTimeTask lkpRefreshTimeTask;
  private TdqEnv tdqEnv;
  private List<TdqConfig> tdqConfigs = new CopyOnWriteArrayList<>();
  private List<PhysicalPlan> physicalPlans = new CopyOnWriteArrayList<>();

  private TdqConfigManager(TimeUnit timeUnit, TdqEnv tdqEnv) {
    this.tdqEnv = tdqEnv;
    this.lkpRefreshTimeTask = new LkpRefreshTimeTask(this, timeUnit);
    refresh();
  }

  private TdqConfigManager(TdqEnv tdqEnv) {
    this(TimeUnit.MINUTES, tdqEnv);
  }

  public static TdqConfigManager getInstance(TdqEnv tdqEnv) {
    if (lkpManager == null) {
      synchronized (TdqConfigManager.class) {
        if (lkpManager == null) {
          lkpManager = new TdqConfigManager(tdqEnv);
        }
      }
    } else {
      // for unit test
      if (tdqEnv.isLocal() && lkpManager.tdqEnv != tdqEnv) {
        synchronized (TdqConfigManager.class) {
          lkpManager.tdqEnv = tdqEnv;
        }
      }
    }
    return lkpManager;
  }

  public void refresh() {
    freshTdqConfigs();
    log.info(lkpRefreshTimeTask.getId() + ":refresh success!");
  }

  public static TdqConfig getTdqConfig(TdqEnv tdqEnv) throws Exception {
    JdbcEnv jdbc = tdqEnv.getJdbcEnv();
    Class.forName(jdbc.getDriverClassName());
    Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
    ResultSet rs = conn.createStatement()
        .executeQuery(
            "select id, config from rhs_config where status='ACTIVE' and name='" + tdqEnv.getJobName() + "'");
    rs.next();
    String json = rs.getString("config");
    String id = String.valueOf(rs.getInt("id"));
    String name = tdqEnv.getJobName();
    TdqConfig c = JsonUtils.parseObject(json, TdqConfig.class);
    // log.warn("getTdqConfigs={}", json);
    log.warn("getTdqConfigs={}", DigestUtils.md5Hex(json));
    conn.close();
    return TdqConfig.builder()
        .id(id)
        .name(name)
        .sources(c.getSources())
        .rules(c.getRules())
        .sinks(c.getSinks() == null ? Lists.newArrayList() : c.getSinks())
        .build();
  }

  public void freshTdqConfigs() {
    try {
      JdbcEnv jdbc = tdqEnv.getJdbcEnv();
      List<TdqConfig> tdqConfigList = new CopyOnWriteArrayList<>();
      tdqConfigList.add(getTdqConfig(tdqEnv));
      this.tdqConfigs = tdqConfigList;

      List<PhysicalPlan> plans = new CopyOnWriteArrayList<>();
      for (TdqConfig config : tdqConfigList) {
        Schema schema = null;
        if (CollectionUtils.isNotEmpty(config.getSources()) &&
            config.getSources().get(0).getType().equals("realtime.kafka")) {
          // tdq config must have same schema
          KafkaSourceConfig ksc = KafkaSourceConfig.build(config.getSources().get(0), tdqEnv);
          if (ksc.getDeserializer().equals(
              "com.ebay.tdq.connector.kafka.schema.RheosEventDeserializationSchema")) {
            schema = RheosEventSerdeFactory.getSchema(ksc.getSchemaSubject(), ksc.getRheosServicesUrls());
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
              // log.debug("TdqConfigSourceFunction={}", plan);
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

  public List<PhysicalPlan> getPhysicalPlans() {
    return physicalPlans;
  }
}
