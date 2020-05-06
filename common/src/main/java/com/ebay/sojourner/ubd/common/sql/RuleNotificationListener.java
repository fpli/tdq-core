package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.zookeeper.ZkClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RuleNotificationListener {

  private RuleFetcher ruleFetcher;
  private ZkClient zkClient;

  public RuleNotificationListener(RuleFetcher ruleFetcher) {
    this.ruleFetcher = ruleFetcher;
    this.zkClient = new ZkClient();

  }

  public void listen() {
    try {
      log.info("start init zk client");
      zkClient.init(ruleFetcher);
    } catch (Exception e) {
      log.warn("rule fetch failed",e);
    }

  }

  public void close() {
    zkClient.stop();
  }

  public static void example1() {
    new RuleNotificationListener(null).listen();
  }

  public static void main(String[] args) throws Exception {
    example1();
  }

}
