package com.ebay.sojourner.business.ubd.metrics;

import com.ebay.sojourner.common.model.SessionAccumulator;
import com.ebay.sojourner.common.model.UbiSession;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentIPMetricsTest extends BaseMetricsTest {

  private AgentIPMetrics agentIPMetrics;

  @BeforeEach
  public void setup() throws Exception {
    agentIPMetrics = new AgentIPMetrics();
    yaml = loadTestCasesYaml("AgentIPMetricsTest.yaml");
  }

  @Disabled
  @TestFactory
  public Collection<DynamicTest> test_feed() throws Exception {
    return generateDynamicTests(yaml, agentIPMetrics);
  }

  @Test
  void test_start() {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setFindFirst(true);
    ubiSession.setInternalIp("internal IP");
    ubiSession.setExternalIp("external IP");
    ubiSession.setExternalIp2("external IP2");

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    agentIPMetrics.start(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().isFindFirst()).isFalse();
    Assertions.assertThat(sessionAccumulator.getUbiSession().getInternalIp()).isNull();
    Assertions.assertThat(sessionAccumulator.getUbiSession().getExternalIp()).isNull();
    Assertions.assertThat(sessionAccumulator.getUbiSession().getExternalIp2()).isNull();
  }

  @Test
  void test_end() {
    UbiSession ubiSession = new UbiSession();
    ubiSession.setAgentInfo("my-agent");
    ubiSession.setClientIp("1.1.1.1");
    ubiSession.setInternalIp("0.0.0.0");

    SessionAccumulator sessionAccumulator = new SessionAccumulator(ubiSession);
    agentIPMetrics.end(sessionAccumulator);

    Assertions.assertThat(sessionAccumulator.getUbiSession().getUserAgent()).isEqualTo("my-agent");
    Assertions.assertThat(sessionAccumulator.getUbiSession().getClientIp()).isEqualTo("1.1.1.1");
    Assertions.assertThat(sessionAccumulator.getUbiSession().getExInternalIp())
        .isEqualTo("0.0.0.0");
  }
}
