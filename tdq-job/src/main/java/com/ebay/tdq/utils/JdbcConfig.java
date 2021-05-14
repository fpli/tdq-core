package com.ebay.tdq.utils;

import java.io.Serializable;
import lombok.Data;

import static com.ebay.sojourner.flink.common.FlinkEnvUtils.getString;

/**
 * @author juntzhang
 */
@Data
public class JdbcConfig implements Serializable {
  private String user;
  private String password;
  private String driverClassName;
  private String url;

  public JdbcConfig() {
    this.user            = getString("mysql.jdbc.user");
    this.password        = getString("mysql.jdbc.password");
    this.driverClassName = getString("mysql.jdbc.driver-class-name");
    this.url             = getString("mysql.jdbc.url");
  }
}
