package com.ebay.sojourner.common.zookeeper;

import com.ebay.sojourner.common.util.Constants;
import com.google.common.base.Charsets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

@Slf4j
@Getter
public class ZkClient {

  private CuratorFramework client;

  public ZkClient() {
    init();
  }

  private void init() {
    // create client
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(Constants.ZK_BASE_SLEEP_TIME,
        Constants.ZK_RETRY_TIMES);
    client = CuratorFrameworkFactory.builder()
        .connectString(Constants.ZOOKEEPER_CONNECTION_STRING)
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(Constants.ZK_SESSION_TIME_OUT)
        .connectionTimeoutMs(Constants.ZK_CONNECTION_TIME_OUT)
        .namespace(Constants.ZK_NAMESPACE)
        .authorization("digest", "sojourner:sojourner".getBytes(Charsets.UTF_8))
        .build();
    // start client
    client.start();
  }

  public void stop() {
    // close client
    client.close();
  }
}
