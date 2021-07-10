package com.ebay.tdq.utils;

import java.io.Serializable;
import java.util.StringJoiner;
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
    this.user            = getString("mysql.jdbc.u");
    this.password        = getString("mysql.jdbc.p");
    this.driverClassName = getString("mysql.jdbc.driver-class-name");
    this.url             = getString("mysql.jdbc.url");
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", JdbcConfig.class.getSimpleName() + "[", "]")
        .add("user='" + user + "'")
        .add("password='******'")
        .add("driverClassName='" + driverClassName + "'")
        .add("url='" + url + "'")
        .toString();
  }
}
