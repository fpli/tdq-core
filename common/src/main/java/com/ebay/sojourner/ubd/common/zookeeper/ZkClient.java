package com.ebay.sojourner.ubd.common.zookeeper;

import com.ebay.sojourner.ubd.common.sql.RuleFetcher;
import com.ebay.sojourner.ubd.common.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

@Slf4j
public class ZkClient {

  private CuratorFramework client;

  public void init(RuleFetcher ruleFetcher) throws Exception {
    // create client
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(Constants.ZK_BASE_SLEEP_TIME,
        Constants.ZK_RETRY_TIMES);
    client = CuratorFrameworkFactory.builder()
        .connectString(Constants.ZOOKEEPER_CONNECTION_STRING)
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(Constants.ZK_SESSION_TIME_OUT)
        .connectionTimeoutMs(Constants.ZK_CONNECTION_TIME_OUT)
        .namespace(Constants.ZK_NAMESPACE)
        .build();
    // start client
    client.start();

    // create node if not exist
    if (client.checkExists().forPath(Constants.ZK_NODE_PATH) == null) {
      client.create()
          .creatingParentContainersIfNeeded()
          .forPath(Constants.ZK_NODE_PATH);
    }

    PathChildrenCache cache = new PathChildrenCache(client, Constants.ZK_NODE_PATH, true);
    // add listener
    cache.getListenable().addListener((client, event) -> {
      log.info("ZooKeeper Event: {}", event.getType());
      if (event.getData() != null && StringUtils.isNotBlank(event.getData().getPath())) {
        ruleFetcher.fetchRulesById(Long.parseLong(
            event.getData().getPath()
                .substring(Constants.ZK_NAMESPACE.length() + Constants.ZK_NODE_PATH.length())));
        log.info("ZooKeeper Node Data: {} = {}",
            event.getData().getPath(), new String(event.getData().getData()));
      }
    });
    cache.start();
  }

  public void stop() {
    // close client
    client.close();
  }
}
