package com.ebay.tdq.planner;

import com.ebay.tdq.common.env.JdbcEnv;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
public class LkpManagerTest {

  JdbcEnv jdbc = new JdbcEnv();

  public static void init() throws Exception {
    JdbcEnv jdbc = new JdbcEnv();
    Class.forName(jdbc.getDriverClassName());
    Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());

    Statement st = conn.createStatement();
    for (String name : Lists.newArrayList("rhs_config", "rhs_lkp_table")) {
      try (InputStream is = LkpManagerTest.class.getResourceAsStream("/" + name + ".sql")) {
        String sql = IOUtils.toString(is);
        for (String s : sql.split(";")) {
          System.out.println(s);
          if (StringUtils.isNotBlank(s)) {
            st.execute(s);
          }
        }
      }
    }

    try (InputStream is = LkpManagerTest.class.getResourceAsStream("/rhs_config.dat")) {
      PreparedStatement ps = conn.prepareStatement("INSERT INTO rhs_config (config) VALUES (?)");
      String sql = IOUtils.toString(is);
      for (String s : sql.split("\n")) {
        if (StringUtils.isNotBlank(s)) {
          ps.setString(1, s);
          ps.addBatch();
        }
      }
      ps.executeBatch();
      ps.close();
    }

    st.close();
  }

  @Test
  public void test() throws Exception {
    init();
    Assert.assertTrue(LkpManager.getInstance(jdbc).isBBWOAPagesWithItm(167));
    Assert.assertFalse(LkpManager.getInstance(jdbc).isBBWOAPagesWithItm(1111111111));
    Assert.assertEquals("OFFER", LkpManager.getInstance(jdbc).getPageFmlyByPageId(1596440));

    Assert.assertTrue(LkpManager.getInstance(jdbc).getTdqConfigs().size() > 0);
  }
}
