package com.ebay.sojourner.common.zookeeper;

import com.ebay.sojourner.common.env.EnvironmentUtils;
import com.ebay.sojourner.common.util.Property;
import com.google.common.base.Charsets;
import java.util.ArrayList;
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
    ArrayList<String> zkServerList = EnvironmentUtils
        .get(Property.ZOOKEEPER_SERVER, ArrayList.class);

    RetryPolicy retryPolicy = new ExponentialBackoffRetry(
        EnvironmentUtils.get(Property.ZOOKEEPER_BASE_SLEEP_TIME_MS, Integer.class),
        EnvironmentUtils.get(Property.ZOOKEEPER_MAX_RETRIES, Integer.class));
    client = CuratorFrameworkFactory.builder()
        .connectString(String.join(",", zkServerList))
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(
            EnvironmentUtils.get(Property.ZOOKEEPER_SESSION_TIMEOUT_MS, Integer.class))
        .connectionTimeoutMs(
            EnvironmentUtils.get(Property.ZOOKEEPER_CONNECTION_TIMEOUT_MS, Integer.class))
        .namespace(EnvironmentUtils.get(Property.ZOOKEEPER_NAMESPACE))
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
