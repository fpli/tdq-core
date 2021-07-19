package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;

import java.io.Serializable;
import java.util.StringJoiner;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class JdbcEnv implements Serializable {

  private String user;
  private String password;
  private String driverClassName;
  private String url;

  public JdbcEnv() {
    this.user = get("mysql.jdbc.u");
    this.password = get("mysql.jdbc.p");
    this.driverClassName = get("mysql.jdbc.driver-class-name");
    this.url = get("mysql.jdbc.url");
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", JdbcEnv.class.getSimpleName() + "[", "]")
        .add("user='" + user + "'")
        .add("password='******'")
        .add("driverClassName='" + driverClassName + "'")
        .add("url='" + url + "'")
        .toString();
  }
}
