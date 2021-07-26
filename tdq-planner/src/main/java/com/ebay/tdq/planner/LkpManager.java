package com.ebay.tdq.planner;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.config.ProfilerConfig;
import com.ebay.tdq.config.RuleConfig;
import com.ebay.tdq.config.TdqConfig;
import com.ebay.tdq.rules.PhysicalPlan;
import com.ebay.tdq.rules.ProfilingSqlParser;
import com.ebay.tdq.utils.DateUtils;
import com.ebay.tdq.utils.JsonUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @author juntzhang
 */
@Slf4j
public class LkpManager {

  private static volatile LkpManager lkpManager;
  private final LkpRefreshTimeTask lkpRefreshTimeTask;
  private final JdbcEnv jdbc;
  private Set<Integer> bbwoaPagesWithItm = new CopyOnWriteArraySet<>();
  private Map<Integer, String> pageFmlyAllMap = new ConcurrentHashMap<>();
  private List<TdqConfig> tdqConfigs = new CopyOnWriteArrayList<>();
  private List<PhysicalPlan> physicalPlans = new CopyOnWriteArrayList<>();

  private LkpManager(TimeUnit timeUnit, JdbcEnv jdbc) {
    this.jdbc = jdbc;
    this.lkpRefreshTimeTask = new LkpRefreshTimeTask(this, timeUnit);
    refresh();
  }

  private LkpManager(JdbcEnv jdbc) {
    this(TimeUnit.MINUTES, jdbc);
  }

  public static LkpManager getInstance(JdbcEnv jdbc) {
    if (lkpManager == null) {
      synchronized (LkpManager.class) {
        if (lkpManager == null) {
          lkpManager = new LkpManager(jdbc);
        }
      }
    }
    return lkpManager;
  }

  public void refresh() {
    Map<String, String> lkp = getLkpTable();
    refreshBBWOAPagesWithItm(lkp);
    refreshPageFamily(lkp);
    freshTdqConfigs();
    log.info("refresh success!");
  }

  private void refreshBBWOAPagesWithItm(Map<String, String> lkp) {
    bbwoaPagesWithItm = Arrays
        .stream(lkp.get("BBWOA_PAGES_WITH_ITM").split(","))
        .map(String::trim)
        .map(Integer::valueOf)
        .collect(Collectors.toCollection(CopyOnWriteArraySet::new));
  }

  private void refreshPageFamily(Map<String, String> lkp) {
    Map<Integer, String> map = new ConcurrentHashMap<>();
    Arrays
        .stream(lkp.get("PAGE_FAMILY").split("\n"))
        .forEach(s -> {
          String[] arr = s.split(",");
          map.put(Integer.parseInt(arr[0].trim()), arr[1].trim());
        });
    this.pageFmlyAllMap = map;
  }

  private Map<String, String> getLkpTable() {
    try {
      Map<String, String> map = new HashMap<>();
      Class.forName(jdbc.getDriverClassName());
      Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
      ResultSet rs = conn.createStatement().executeQuery(
          "SELECT t1.name,t1.value"
              + " FROM "
              + "     rhs_lkp_table t1"
              + " JOIN ("
              + "     SELECT NAME,MAX(VERSION) AS VERSION"
              + "     FROM"
              + "         rhs_lkp_table"
              + "     GROUP BY NAME"
              + " ) t2 ON t1.name = t2.name AND t1.version = t2.version");
      while (rs.next()) {
        String key = rs.getString("name");
        String value = rs.getString("value");
        map.put(key, value);
      }
      conn.close();
      return map;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void freshTdqConfigs() {
    try {
      List<TdqConfig> tdqConfigList = new CopyOnWriteArrayList<>();
      Class.forName(jdbc.getDriverClassName());
      Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
      ResultSet rs = conn.createStatement().executeQuery("select config from rhs_config where status='ACTIVE'");
      while (rs.next()) {
        String json = rs.getString("config");
        log.warn("getTdqConfigs={}", json);
        tdqConfigList.add(JsonUtils.parseObject(json, TdqConfig.class));
      }
      conn.close();
      this.tdqConfigs = tdqConfigList;

      List<PhysicalPlan> plans = new CopyOnWriteArrayList<>();
      for (TdqConfig config : tdqConfigList) {
        for (RuleConfig ruleConfig : config.getRules()) {
          for (ProfilerConfig profilerConfig : ruleConfig.getProfilers()) {
            try {
              ProfilingSqlParser parser = new ProfilingSqlParser(
                  profilerConfig,
                  DateUtils.toSeconds(ruleConfig.getConfig().get("window").toString()),
                  jdbc
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

      this.physicalPlans = plans;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() {
    this.lkpRefreshTimeTask.cancel();
  }

  public boolean isBBWOAPagesWithItm(Integer pageId) {
    return pageId != null && bbwoaPagesWithItm.contains(pageId);
  }

  public String getPageFmlyByPageId(Integer pageId) {
    return pageFmlyAllMap.get(pageId);
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

  static class LkpRefreshTimeTask extends TimerTask {

    private final LkpManager lkpManager;

    private LkpRefreshTimeTask(LkpManager lkpManager, TimeUnit timeUnit) {
      this.lkpManager = lkpManager;
      ScheduledThreadPoolExecutor scheduledThreadPoolExecutor
          = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(@NotNull Runnable r) {
          Thread t = new Thread(r, "tdq-lkp-refresh-thread");
          t.setDaemon(true);
          return t;
        }
      });
      scheduledThreadPoolExecutor.scheduleAtFixedRate(this, 0, 1, timeUnit);
    }

    @Override
    public void run() {
      try {
        lkpManager.refresh();
      } catch (Exception e) {
        log.warn(System.currentTimeMillis() + "refresh lkp file failed");
      }
    }
  }
}
