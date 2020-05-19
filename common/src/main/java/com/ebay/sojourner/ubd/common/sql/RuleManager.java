package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.zookeeper.ZkClient;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;

@Slf4j
public class RuleManager {

  private static final RuleManager INSTANCE = new RuleManager();
  private final RuleFetcher ruleFetcher;
  // private final ZkClient zkClient;
  // private final ExecutorService zkExecutor;
  private final ScheduledExecutorService schedulingExecutor;

  @Getter
  private Set<SqlEventRule> sqlEventRuleSet = new CopyOnWriteArraySet<>();

  private RuleManager() {

    ruleFetcher = new RuleFetcher();
    schedulingExecutor = Executors.newSingleThreadScheduledExecutor();

    // TODO(Jason) disable zk for now
    // 1. init zk listener
    // zkClient = new ZkClient();
    // zkExecutor = Executors.newSingleThreadExecutor();
    // initZkListener();

    // 2. init scheduling
    initScheduling(15L, 15L);

  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  public void initRules() {
    log.info("init all rules");
    updateRules(ruleFetcher.fetchAllRules());
  }

  private void initZkListener(ZkClient zkClient, ExecutorService zkExecutor) {
    CuratorFramework client = zkClient.getClient();
    PathChildrenCache cache = new PathChildrenCache(client, Constants.ZK_NODE_PATH, true);
    // add listener
    cache.getListenable().addListener((c, event) -> {
      if (Type.CHILD_ADDED.equals(event.getType()) ||
          Type.CHILD_UPDATED.equals(event.getType())) {
        log.info("ZooKeeper Event: {}", event.getType());
        if (null != event.getData()) {
          log.info("ZooKeeper Node Data: {} = {}",
              event.getData().getPath(), new String(event.getData().getData()));

          String nodeValue = new String(event.getData().getData());
          String newVersion = nodeValue.split(":")[0];
          String ruleId = nodeValue.split(":")[1];
          RuleDefinition ruleDefinition = ruleFetcher.fetchRuleById(ruleId);
          if (ruleDefinition.getVersion() != Integer.parseInt(newVersion)) {
            throw new RuntimeException("Expect to fetch version: " + newVersion +
                ", but got " + ruleDefinition.getVersion());
          }
          updateRule(ruleDefinition);
        }
      }
    }, zkExecutor);
    try {
      cache.start();
    } catch (Exception e) {
      log.error("Cannot init zk PathChildrenCache listener", e);
    }
  }

  private void initScheduling(Long initDelayInSeconds, Long delayInSeconds) {
    schedulingExecutor.scheduleWithFixedDelay(() -> {
      updateRules(ruleFetcher.fetchAllRules());
    }, initDelayInSeconds, delayInSeconds, TimeUnit.SECONDS);
  }

  private void updateRules(List<RuleDefinition> ruleDefinitions) {
    if (CollectionUtils.isNotEmpty(ruleDefinitions)) {
      sqlEventRuleSet = ruleDefinitions
          .stream()
          .filter(RuleDefinition::getIsActive)
          .map(rule -> SqlEventRule
              .of(rule.getContent(), rule.getBizId(), rule.getVersion(), rule.getCategory()))
          .collect(Collectors.toSet());
    }
    log.info("Rules deployed: " + this.sqlEventRuleSet.size());
    log.info("rule set" + sqlEventRuleSet.size());
  }

  private void updateRule(RuleDefinition ruleDefinition) {

    if (ruleDefinition != null && ruleDefinition.getIsActive()) {
      SqlEventRule sqlEventRule = SqlEventRule
          .of(ruleDefinition.getContent(), ruleDefinition.getBizId(), ruleDefinition.getVersion(),
              ruleDefinition.getCategory());
      sqlEventRuleSet.add(sqlEventRule);
    } else if (ruleDefinition != null && !ruleDefinition.getIsActive()) {
      if (getRuleIdSet(sqlEventRuleSet).contains(ruleDefinition.getBizId())) {
        sqlEventRuleSet.removeIf(rule -> rule.getRuleId() == ruleDefinition.getBizId());
      }
    }
  }

  private Set<Long> getRuleIdSet(Set<SqlEventRule> sqlEventRules) {
    return sqlEventRules
        .stream()
        .map(SqlEventRule::getRuleId)
        .collect(Collectors.toSet());
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
