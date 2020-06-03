package com.ebay.sojourner.business.ubd.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.zookeeper.ZkClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private final RuleFetcher ruleFetcher = new RuleFetcher();
  // private final ZkClient zkClient;
  // private final ExecutorService zkExecutor;
  private final ScheduledExecutorService schedulingExecutor =
      Executors.newSingleThreadScheduledExecutor();
  private final List<RuleChangeEventListener<RuleChangeEvent>> listeners = Lists.newLinkedList();

  @Getter
  private Set<RuleDefinition> ruleDefinitions = Sets.newHashSet();

  private RuleManager() {
    // initRules();

    // TODO(Jason) disable zk for now
    // 1. init zk listener
    // zkClient = new ZkClient();
    // zkExecutor = Executors.newSingleThreadExecutor();
    // initZkListener();

    // 2. init scheduling
    // initScheduler(30L, 15L);

  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public void addListener(RuleChangeEventListener<RuleChangeEvent> listener) {
    this.listeners.add(listener);
    RuleChangeEvent ruleChangeEvent = new RuleChangeEvent();
    ruleChangeEvent.setLocalDateTime(LocalDateTime.now());
    ruleChangeEvent.setRules(this.ruleDefinitions);
    listener.onChange(ruleChangeEvent);
  }

  private void notifyListeners(List<RuleDefinition> ruleDefinitionList) {
    log.info("Rule Changed, notifying listeners");
    RuleChangeEvent ruleChangeEvent = new RuleChangeEvent();
    ruleChangeEvent.setLocalDateTime(LocalDateTime.now());
    ruleChangeEvent.setRules(Sets.newHashSet(ruleDefinitionList));

    for (RuleChangeEventListener<RuleChangeEvent> listener : listeners) {
      listener.onChange(ruleChangeEvent);
    }
  }

  public void initRules() {
    log.info("Init all rules");
    List<RuleDefinition> ruleDefinitionList = ruleFetcher.fetchAllRules();
    this.ruleDefinitions = Sets.newHashSet(ruleDefinitionList);
    log.info("Init rules count: {}", this.ruleDefinitions.size());
  }

  private void initZkListener(ZkClient zkClient, ExecutorService zkExecutor)    {
    CuratorFramework client = zkClient.getClient();
    PathChildrenCache cache = new PathChildrenCache(client, Constants.ZK_NODE_PATH, true);
    // add listener
    cache.getListenable()
        .addListener((c, event) -> {
          if (Type.CHILD_ADDED.equals(event.getType()) ||
              Type.CHILD_UPDATED.equals(event.getType())) {
            log.info("ZooKeeper Event: {}", event.getType());
            if (null != event.getData()) {
              log.info("ZooKeeper Node Data: {} = {}",
                  event.getData()
                      .getPath(), new String(event.getData()
                      .getData()));
            }
          }
        }, zkExecutor);
    try {
      cache.start();
    } catch (Exception e) {
      log.error("Cannot init zk PathChildrenCache listener", e);
    }
  }

  private void initScheduler(Long initDelayInSeconds, Long delayInSeconds) {
    schedulingExecutor.scheduleWithFixedDelay(() -> {
      log.info("Scheduled to fetch rules");
      List<RuleDefinition> ruleDefinitionList = ruleFetcher.fetchAllRules();

      if (ruleDefinitionList.size() != ruleDefinitions.size()) {
        notifyListeners(ruleDefinitionList);
      }

      for (RuleDefinition ruleDefinition : ruleDefinitionList) {
        if (!ruleDefinitions.contains(ruleDefinition)) {
          notifyListeners(ruleDefinitionList);
          break;
        }
      }

    }, initDelayInSeconds, delayInSeconds, TimeUnit.SECONDS);
  }

  public void close() {
    // zkClient.stop();
    // zkExecutor.shutdown();
    schedulingExecutor.shutdown();
  }

  public static void main(String[] args) throws Exception {
    RuleManager instance = RuleManager.getInstance();
    Thread.sleep(10 * 60 * 1000);
    instance.close();
  }

}
