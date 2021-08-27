package com.ebay.tdq.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.connector.kafka.schema.RheosEventSerdeFactory;
import com.ebay.tdq.planner.Refreshable;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.rules.Transformation;
import com.google.common.collect.Lists;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * @author juntzhang
 */
@Slf4j
public class TdqConfigManager implements Refreshable {

  private static volatile TdqConfigManager configManager;
  private static volatile ScheduledThreadPoolExecutor poolExecutor;
  private static final AtomicBoolean scheduled = new AtomicBoolean(false);

  private TdqEnv tdqEnv;
  private List<PhysicalPlan> physicalPlans = new CopyOnWriteArrayList<>();
  private Map<String, Map<String, String>> metricNameAggrExpressMap = new CopyOnWriteHashMap<>();

  private TdqConfigManager(TdqEnv tdqEnv) {
    log.info("init {}", tdqEnv.getId());
    this.tdqEnv = tdqEnv;
    start();
  }

  public static TdqConfigManager getInstance(TdqEnv tdqEnv) {
    if (configManager == null) {
      synchronized (TdqConfigManager.class) {
        if (configManager == null) {
          configManager = new TdqConfigManager(tdqEnv);
        }
      }
    } else {
      // for unit test
      if (tdqEnv.isLocal() && configManager.tdqEnv != tdqEnv) {
        synchronized (TdqConfigManager.class) {
          configManager.tdqEnv = tdqEnv;
        }
      }
    }
    return configManager;
  }

  public void start() {
    if (scheduled.compareAndSet(false, true)) {
      log.info("{} started!", this.tdqEnv.getId());
      refresh();
      poolExecutor = new ScheduledThreadPoolExecutor(1, r -> {
        Thread t = new Thread(r, tdqEnv.getId() + ".tdq_config");
        t.setDaemon(true);
        return t;
      });
      final Refreshable refreshable = this;
      try {
        poolExecutor.scheduleAtFixedRate(refreshable::refresh, 1, 1, TimeUnit.MINUTES);
      } catch (RejectedExecutionException e) {
        log.warn("pool already stopped!");
      }
    }
  }

  public void stop() {
    if (scheduled.compareAndSet(true, false)) {
      poolExecutor.shutdown();
      log.info("{} stopped!", this.tdqEnv.getId());
    }
  }

  public void refresh() {
    try {
      freshTdqConfigs();
      log.info("{}:refresh success!", this.tdqEnv.getId());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
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
    log.warn("{} getTdqConfigs={}", tdqEnv.getId(), json);
    conn.close();
    return TdqConfig.builder()
        .id(id)
        .name(name)
        .sources(c.getSources())
        .rules(c.getRules())
        .sinks(c.getSinks() == null ? Lists.newArrayList() : c.getSinks())
        .env(c.getEnv())
        .build();
  }

  public void freshTdqConfigs() {
    try {
      List<TdqConfig> tdqConfigList = new CopyOnWriteArrayList<>();
      tdqConfigList.add(getTdqConfig(tdqEnv));

      List<PhysicalPlan> plans = new CopyOnWriteArrayList<>();
      for (TdqConfig config : tdqConfigList) {
        Schema schema = null;
        if (CollectionUtils.isNotEmpty(config.getSources())) {
          Map<String, Object> cfgMap = config.getSources().get(0).getConfig();
          if (MapUtils.isNotEmpty(cfgMap)) {
            Object schemaSubject = cfgMap.get("schema-subject");
            Object rheosServicesUrls = cfgMap.get("rheos-services-urls");
            if (schemaSubject != null && rheosServicesUrls != null) {
              schema = RheosEventSerdeFactory.getSchema((String) schemaSubject, (String) rheosServicesUrls);
            }
          }
        }
        for (RuleConfig ruleConfig : config.getRules()) {
          for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
            try {
              ProfilingSqlParser parser = new ProfilingSqlParser(
                  profilerConfig,
                  DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString()),
                  tdqEnv,
                  schema);
              PhysicalPlan plan = parser.parsePlan();
              plan.validatePlan();
              plans.add(plan);
            } catch (Exception e) {
              log.warn("profilerConfig[" + profilerConfig + "] validate exception:" + e.getMessage(), e);
            }
          }
        }
      }

      this.physicalPlans = plans;

      Map<String, Map<String, String>> metricNameAggrExpressMap = new CopyOnWriteHashMap<>();
      plans.forEach(p -> {
        Map<String, String> t = new HashMap<>();
        for (Transformation tr : p.aggregations()) {
          t.put(tr.name(), tr.expr().simpleName());
        }
        metricNameAggrExpressMap.put(p.metricName(), t);
      });
      this.metricNameAggrExpressMap = metricNameAggrExpressMap;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<PhysicalPlan> getPhysicalPlans() {
    return physicalPlans;
  }

  public Map<String, Map<String, String>> getMetricNameAggrExpressMap() {
    return metricNameAggrExpressMap;
  }

}
