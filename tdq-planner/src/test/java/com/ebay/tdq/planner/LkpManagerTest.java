package com.ebay.tdq.planner;

import com.ebay.tdq.common.env.JdbcEnv;
import com.ebay.tdq.common.env.TdqEnv;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author juntzhang
 */
public class LkpManagerTest {

  static TdqEnv tdqEnv = new TdqEnv();

  public static void init() throws Exception {
    JdbcEnv jdbc = new JdbcEnv();
    Class.forName(jdbc.getDriverClassName());
    Connection conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(), jdbc.getPassword());

    Statement st = conn.createStatement();
    try (InputStream is = LkpManagerTest.class.getResourceAsStream("/rhs_table.sql")) {
      String sql = IOUtils.toString(is);
      for (String s : sql.split(";")) {
        if (StringUtils.isNotBlank(s)) {
          st.execute(s);
        }
      }
    }

    st.close();

    LkpManager.register(tdqEnv);
  }

  @Test
  public void test() throws Exception {
    init();
    Assert.assertTrue(LkpManager.getInstance(tdqEnv).isBBWOAPagesWithItm(167));
    Assert.assertFalse(LkpManager.getInstance(tdqEnv).isBBWOAPagesWithItm(1111111111));
    Assert.assertEquals("OFFER", LkpManager.getInstance(tdqEnv).getPageFmlyByPageId(1596440));
  }
}
