package com.ebay.sojourner.business.ubd.rule;

import com.ebay.sojourner.common.model.rule.RuleChangeEvent;
import com.ebay.sojourner.common.model.rule.RuleDefinition;
import com.ebay.sojourner.common.zookeeper.ZkClient;
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
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private final RuleFetcher ruleFetcher = new RuleFetcher();
  // private final ZkClient zkClient;
  private final ExecutorService zkExecutor = Executors.newSingleThreadExecutor();
  private final ScheduledExecutorService schedulingExecutor =
      Executors.newSingleThreadScheduledExecutor();
  private final List<RuleChangeEventListener<RuleChangeEvent>> listeners = Lists.newLinkedList();

  private static final String ZK_NODE_PATH = "/rule/publish/notification";

  @Getter
  private Set<RuleDefinition> ruleDefinitions = Sets.newHashSet();

  private RuleManager() {
    // initRules();

    // 1. init zk listener
    /*
    zkClient = new ZkClient(
        ZkConfig.builder()
            .server(EnvironmentUtils.get(ZOOKEEPER_SERVER))
            .baseSleepTimeMs(EnvironmentUtils.getInteger(ZOOKEEPER_BASE_SLEEP_TIME_MS))
            .maxRetries(EnvironmentUtils.getInteger(ZOOKEEPER_MAX_RETRIES))
            .connectionTimeoutMs(EnvironmentUtils.getInteger(ZOOKEEPER_CONNECTION_TIMEOUT_MS))
            .sessionTimeoutMs(EnvironmentUtils.getInteger(ZOOKEEPER_SESSION_TIMEOUT_MS))
            .namespace(EnvironmentUtils.get(ZOOKEEPER_NAMESPACE))
            .build()
    );
    */

    // initZkListener(zkClient, zkExecutor);

    // 2. init scheduling
    // initScheduler(schedulingExecutor, 60L * 60 * 6, 60L * 60 * 6);
  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public void addListener(RuleChangeEventListener<RuleChangeEvent> listener) {
    this.listeners.add(listener);
    RuleChangeEvent ruleChangeEvent = new RuleChangeEvent();
    ruleChangeEvent.setLocalDateTime(LocalDateTime.now());
    ruleChangeEvent.setRules(this.ruleDefinitions);
    // invoke onChange right away
    listener.onChange(ruleChangeEvent);
  }

  private void notifyListeners() {
    log.info("Rule Changed, notifying listeners");
    RuleChangeEvent ruleChangeEvent = new RuleChangeEvent();
    ruleChangeEvent.setLocalDateTime(LocalDateTime.now());
    ruleChangeEvent.setRules(this.ruleDefinitions);

    for (RuleChangeEventListener<RuleChangeEvent> listener : listeners) {
      listener.onChange(ruleChangeEvent);
    }
  }

  private boolean checkIfRuleHasChanges(Set<RuleDefinition> rules) {
    if (this.ruleDefinitions.size() != rules.size()) {
      return true;
    }

    for (RuleDefinition rule : rules) {
      if (!this.ruleDefinitions.contains(rule)) {
        return true;
      }
    }
    return false;
  }

  public void initRules() {
    // TODO(Jason): fetch rules from zk instead of rest api
    log.info("Init all rules");
    List<RuleDefinition> ruleDefinitionList = ruleFetcher.fetchAllRules();
    this.ruleDefinitions = Sets.newHashSet(ruleDefinitionList);
    log.info("Init rules count: {}", this.ruleDefinitions.size());
  }

  private void initZkListener(ZkClient zkClient, ExecutorService zkExecutor)    {
    CuratorFramework client = zkClient.getClient();
    PathChildrenCache cache = new PathChildrenCache(client, ZK_NODE_PATH, true);
    // add listener
    cache.getListenable()
        .addListener((c, event) -> {
          if (Type.CHILD_ADDED.equals(event.getType()) ||
              Type.CHILD_UPDATED.equals(event.getType())) {
            log.info("ZooKeeper Event: {}", event.getType());
            ChildData eventData = event.getData();
            if (null != eventData) {
              log.info("ZooKeeper Node Data: {} = {}", eventData.getPath(),
                  new String(eventData.getData()));
              String nodeValue = new String(eventData.getData());
              String ruleId = nodeValue.split(":")[1];
              RuleDefinition ruleDefinition = ruleFetcher.fetchRuleById(ruleId);
              this.ruleDefinitions.add(ruleDefinition);
              notifyListeners();
            }
          }
        }, zkExecutor);
    try {
      cache.start();
    } catch (Exception e) {
      log.error("Cannot init zk PathChildrenCache listener", e);
    }
  }

  private void initScheduler(ScheduledExecutorService schedulingExecutor,
                             Long initDelayInSeconds, Long delayInSeconds) {
    schedulingExecutor.scheduleWithFixedDelay(() -> {
      log.info("Scheduled to update all rules");
      Set<RuleDefinition> rules = Sets.newHashSet(ruleFetcher.fetchAllRules());
      if (checkIfRuleHasChanges(rules)) {
        this.ruleDefinitions = rules;
        notifyListeners();
      }
    }, initDelayInSeconds, delayInSeconds, TimeUnit.SECONDS);
  }

  public void close() {
    // zkClient.close();
    zkExecutor.shutdown();
    schedulingExecutor.shutdown();
  }

}
