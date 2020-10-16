package com.ebay.sojourner.rt.operator.session;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.SojSession;
import com.ebay.sojourner.common.model.UbiSession;
import com.ebay.sojourner.flink.common.OutputTagConstants;
import java.util.List;
import org.apache.flink.streaming.util.OneInputStreamOperatorTestHarness;
import org.apache.flink.streaming.util.ProcessFunctionTestHarnesses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UbiSessionToSojSessionProcessFunctionTest {

  UbiSessionToSojSessionProcessFunction processFunction;

  @BeforeEach
  void setUp() {
    processFunction = new UbiSessionToSojSessionProcessFunction(OutputTagConstants.botSessionOutputTag);
  }

  @Test
  void processElement() throws Exception {
    // wrap user defined function into a the corresponding operator
    OneInputStreamOperatorTestHarness<UbiSession, SojSession> harness = ProcessFunctionTestHarnesses
        .forProcessFunction(processFunction);

    harness.processElement(new UbiSession(), 10);
    List<SojSession> sojSessions = harness.extractOutputValues();
    assertThat(sojSessions.size()).isEqualTo(1);
  }
}