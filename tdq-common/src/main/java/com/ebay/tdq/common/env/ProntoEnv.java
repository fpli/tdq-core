package com.ebay.tdq.common.env;

import static com.ebay.sojourner.common.env.EnvironmentUtils.get;
import static com.ebay.sojourner.common.env.EnvironmentUtils.getInteger;

import java.io.Serializable;
import java.util.StringJoiner;
import lombok.Data;

/**
 * @author juntzhang
 */
@Data
public class ProntoEnv implements Serializable {

  private String schema;
  private String hostname;
  private int port;
  private String username;
  private String password;

  public ProntoEnv() {
    this.schema = get("pronto.scheme");
    this.hostname = get("pronto.hostname");
    this.port = getInteger("pronto.port");
    this.username = get("pronto.api-key");
    this.password = get("pronto.api-value");
  }


  @Override
  public String toString() {
    return new StringJoiner(", ", ProntoEnv.class.getSimpleName() + "[", "]")
        .add("schema='" + schema + "'")
        .add("hostname='" + hostname + "'")
        .add("port=" + port)
        .add("username='" + username + "'")
        .add("password='******'")
        .toString();
  }

}
