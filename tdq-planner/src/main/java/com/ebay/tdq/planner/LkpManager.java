package com.ebay.tdq.planner;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.TdqEnv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntzhang
 */
@Slf4j
public class LkpManager implements Refreshable {

  private static volatile LkpManager lkpManager;
  private static volatile ScheduledThreadPoolExecutor poolExecutor;
  private static final AtomicBoolean scheduled = new AtomicBoolean(false);

  private final TdqEnv tdqEnv;
  private Set<Integer> bbwoaPagesWithItm = new CopyOnWriteArraySet<>();
  private Map<Integer, String> pageFmlyAllMap = new ConcurrentHashMap<>();

  private LkpManager(TdqEnv tdqEnv) {
    log.info("init {}", tdqEnv.getId());
    this.tdqEnv = tdqEnv;
    start();
  }

  public static LkpManager getInstance(TdqEnv tdqEnv) {
    if (lkpManager == null) {
      synchronized (LkpManager.class) {
        if (lkpManager == null) {
          lkpManager = new LkpManager(tdqEnv);
        }
      }
    }
    return lkpManager;
  }

  public void start() {
    if (scheduled.compareAndSet(false, true)) {
      log.info("{} started!", tdqEnv.getId());
      refresh();
      poolExecutor = new ScheduledThreadPoolExecutor(2, r -> {
        Thread t = new Thread(r, tdqEnv.getId() + ".lkp");
        t.setDaemon(true);
        return t;
      });
      try {
        poolExecutor.scheduleAtFixedRate(this::refresh, 1, 1, TimeUnit.MINUTES);
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

  // todo currently flink not support
  public static boolean isActive(TdqEnv tdqEnv, String name) {
    try {
      JdbcEnv jdbc = tdqEnv.getJdbcEnv();
      Class.forName(jdbc.getDriverClassName());
      Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
      ResultSet rs = conn.createStatement().executeQuery("SELECT value FROM rhs_daemon "
          + "where id='" + tdqEnv.getId() + "' and name='" + name + "'");
      boolean v = false;
      while (rs.next()) {
        v = Boolean.parseBoolean(rs.getString("value"));
      }
      conn.close();
      return v;
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      return true;
    }
  }

  private void stop0() {
    if (!isActive(tdqEnv, "lkp_daemon")) {
      stop();
    }
  }

  public void refresh() {
    try {
      Map<String, String> lkp = getLkpTable();
      refreshBBWOAPagesWithItm(lkp);
      refreshPageFamily(lkp);
      stop0();
      log.info("{}: refresh success!", this.tdqEnv.getId());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
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
      JdbcEnv jdbc = tdqEnv.getJdbcEnv();
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

  public boolean isBBWOAPagesWithItm(Integer pageId) {
    return pageId != null && bbwoaPagesWithItm.contains(pageId);
  }

  public String getPageFmlyByPageId(Integer pageId) {
    String v = pageFmlyAllMap.get(pageId);
    if (v == null || v.equalsIgnoreCase("NULL")) {
      return "Others";
    } else {
      return v;
    }
  }

  public static void register(TdqEnv tdqEnv) throws Exception {
    JdbcEnv jdbc = tdqEnv.getJdbcEnv();
    Class.forName(jdbc.getDriverClassName());
    Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
    Statement stat = conn.createStatement();
    stat.execute("delete from rhs_daemon where id='" + tdqEnv.getId() + "'");
    stat.close();

    PreparedStatement ps = conn.prepareStatement("INSERT INTO rhs_daemon (id,name,value) VALUES (?,?,?)");
    ps.setString(1, tdqEnv.getId());
    ps.setString(2, "tdq_config_daemon");
    ps.setString(3, String.valueOf(true));
    ps.addBatch();

    ps.setString(1, tdqEnv.getId());
    ps.setString(2, "lkp_daemon");
    ps.setString(3, String.valueOf(true));
    ps.addBatch();

    ps.executeBatch();
    ps.close();
    conn.close();
  }

}
