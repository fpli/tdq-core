package com.ebay.sojourner.dsl.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RuleManager.class})
public class RuleManagerTest {

  RuleManager ruleManager;

  @Before
  public void setUp() throws Exception {
    ruleManager = RuleManager.getInstance();
  }

  @Test
  public void getInstance() {
    assertThat(ruleManager).isNotNull();
  }

  @Test
  public void close_isNull() {
    ruleManager.close();
    ExecutorService zkExecutor = Whitebox.getInternalState(ruleManager, "zkExecutor");
    ScheduledExecutorService schedulingExecutor = Whitebox.getInternalState(ruleManager, "schedulingExecutor");
    assertThat(zkExecutor).isNull();
    assertThat(schedulingExecutor).isNull();
  }

  @Test
  public void close_isNotNull() {
    Whitebox.setInternalState(ruleManager, "zkExecutor", Executors.newSingleThreadExecutor());
    Whitebox.setInternalState(ruleManager, "schedulingExecutor", Executors.newSingleThreadScheduledExecutor());
    ruleManager.close();

    ExecutorService zkExecutor = Whitebox.getInternalState(ruleManager, "zkExecutor");
    ScheduledExecutorService schedulingExecutor = Whitebox.getInternalState(ruleManager, "schedulingExecutor");
    assertThat(zkExecutor.isShutdown()).isTrue();
    assertThat(schedulingExecutor.isShutdown()).isTrue();
  }
}