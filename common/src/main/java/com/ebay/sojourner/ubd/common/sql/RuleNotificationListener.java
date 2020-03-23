package com.ebay.sojourner.ubd.common.sql;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

public class RuleNotificationListener {

  protected static final Logger LOGGER = Logger.getLogger(RuleNotificationListener.class);
  public static final String ZOOKEEPER_CONNECTION_STRING = "localhost:2181";
  public static final String ZOOKEEPER_PATH_LEADER = "/sojourner/rule_management";
  public static final String REST_SERVER_LEADER_PATH = "/rest_server_lock";

  private RuleManager ruleManager;

  public RuleNotificationListener(RuleManager ruleManager) {
    this.ruleManager = ruleManager;
  }

  public void listen() {
    try {
      CuratorFramework client = CuratorFrameworkFactory.builder()
          .connectString(ZOOKEEPER_CONNECTION_STRING)
          .retryPolicy(new ExponentialBackoffRetry(1000, 3))
          .build();
      client.start();
      String retrievalPath = ZOOKEEPER_PATH_LEADER + REST_SERVER_LEADER_PATH;
      final NodeCache cache = new NodeCache(client, retrievalPath);
      cache.start();
      cache.getListenable().addListener(() -> {
        try {
          System.out.println("Fetch rules on demand");
          ruleManager.fetchRules();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.warn("Error from RuleNotificationListener thread.", e);
    }
  }

  public static void example1() {
    new RuleNotificationListener(null).listen();
  }

  public static void main(String[] args) throws Exception {
    example1();
  }

}
