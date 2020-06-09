package com.ebay.sojourner.common.zookeeper;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
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

  public ZkClient(ZkConfig config) {
    init(config);
  }

  private void init(ZkConfig config) {
    Preconditions.checkNotNull(config);
    Preconditions.checkNotNull(config.getServer());

    // create client
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(
        config.getBaseSleepTimeMs(),
        config.getMaxRetries());

    client = CuratorFrameworkFactory.builder()
        .connectString(config.getServer())
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(config.getSessionTimeoutMs())
        .connectionTimeoutMs(config.getConnectionTimeoutMs())
        .namespace(config.getNamespace())
        .authorization("digest", "sojourner:sojourner".getBytes(Charsets.UTF_8))
        .build();

    // start client
    client.start();
  }

  public void close() {
    // close client
    client.close();
  }
}
