package com.ebay.sojourner.ubd.common.sql;

import com.ebay.sojourner.ubd.common.util.Constants;
import com.ebay.sojourner.ubd.common.zookeeper.ZkClient;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

  //FIXME(Jason): not thread-safe
  private static final RuleManager INSTANCE = new RuleManager();
  private final RuleFetcher ruleFetcher;
  private final ZkClient zkClient;
  private final Executor zkExecutor;

  @Getter
  private Set<SqlEventRule> sqlEventRuleSet = new CopyOnWriteArraySet<>();

  private RuleManager() {

    ruleFetcher = new RuleFetcher();
    zkClient = new ZkClient();
    zkExecutor = Executors.newSingleThreadExecutor();

    // 1. fetch all rules at startup
    initRules();
    // 2. init zk listener
    initZkListener();
    // 3. init scheduling
    initScheduling();

  }

  public static RuleManager getInstance() {
    return INSTANCE;
  }

  private void initRules() {
    log.info("init all rules");
    updateRules(ruleFetcher.fetchAllRules());
  }

  private void initZkListener() {
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

  private void initScheduling() {
    Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
      updateRules(ruleFetcher.fetchAllRules());
    }, 6, 6, TimeUnit.HOURS);
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
  }

  private void updateRule(RuleDefinition ruleDefinition) {
    if (ruleDefinition != null && ruleDefinition.getIsActive()) {
      SqlEventRule sqlEventRule = SqlEventRule
          .of(ruleDefinition.getContent(), ruleDefinition.getBizId(), ruleDefinition.getVersion(),
              ruleDefinition.getCategory());
      sqlEventRuleSet.add(sqlEventRule);
    }
  }

  public void close() {
    zkClient.stop();
  }

  public static void main(String[] args) throws Exception {
    RuleManager instance = RuleManager.getInstance();
    Thread.sleep(10 * 60 * 1000);
    instance.close();
  }

}
