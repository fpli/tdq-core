package com.ebay.tdq.planner.utils;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.TdqEnv;
import com.ebay.tdq.utils.JsonUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author juntzhang
 */
public class ConfigService {

  public static void setConfig(TdqEnv tdqEnv, String name, String value) throws Exception {
    JdbcEnv jdbc = tdqEnv.getJdbcEnv();
    Class.forName(jdbc.getDriverClassName());
    Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());
    Statement stat = conn.createStatement();
    stat.execute("delete from rhs_daemon where id='" + tdqEnv.getId() + "'");
    stat.close();

    PreparedStatement ps = conn.prepareStatement("INSERT INTO rhs_daemon (id,name,value) VALUES (?,?,?)");
    ps.setString(1, tdqEnv.getId());
    ps.setString(2, name);
    ps.setString(3, value);
    ps.addBatch();

    ps.executeBatch();
    ps.close();
    conn.close();
  }

  public static void register(TdqEnv tdqEnv) throws Exception {
    setConfig(tdqEnv, "tdq_config", JsonUtils.toJSONString(tdqEnv));
  }
}
