package com.ebay.sojourner.rt.operator.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import org.apache.flink.types.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetectableEventMapFunctionTest {

  DetectableEventMapFunction mapFunction;
  UbiEvent ubiEvent;

  @BeforeEach
  void setUp() {
    mapFunction = new DetectableEventMapFunction();
    ubiEvent = new UbiEvent();
  }

  @Test
  void map() throws Exception {
    ubiEvent.setGuid("123");
    Either<UbiEvent, UbiSession> either = mapFunction.map(ubiEvent);
    assertThat(either.isLeft()).isTrue();
    assertThat(either.left().getGuid()).isEqualTo("123");
  }
}