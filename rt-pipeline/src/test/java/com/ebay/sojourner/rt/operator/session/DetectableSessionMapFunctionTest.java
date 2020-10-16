package com.ebay.sojourner.rt.operator.session;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.common.model.UbiEvent;
import com.ebay.sojourner.common.model.UbiSession;
import org.apache.flink.types.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetectableSessionMapFunctionTest {

  DetectableSessionMapFunction mapFunction;
  UbiSession ubiSession;

  @BeforeEach
  void setUp() {
    mapFunction = new DetectableSessionMapFunction();
    ubiSession = new UbiSession();
  }

  @Test
  void map() throws Exception {
    ubiSession.setGuid("123");
    Either<UbiEvent, UbiSession> either = mapFunction.map(ubiSession);
    assertThat(either.isRight()).isTrue();
    assertThat(either.right().getGuid()).isEqualTo("123");
  }
}