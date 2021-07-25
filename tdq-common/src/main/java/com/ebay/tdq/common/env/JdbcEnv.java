package com.ebay.tdq.common.env;


import com.ebay.sojourner.common.env.EnvironmentUtils;
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
    if (EnvironmentUtils.contains("mysql.jdbc.url")) {
      this.url = EnvironmentUtils.get("mysql.jdbc.url");
      this.driverClassName = EnvironmentUtils.get("mysql.jdbc.driver-class-name");
      this.user = EnvironmentUtils.get("mysql.jdbc.u");
      this.password = EnvironmentUtils.get("mysql.jdbc.p");
    }
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
