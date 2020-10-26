package com.ebay.sojourner.flink.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.junit.jupiter.api.Test;

class MapStateDescTest {

  @Test
  void attributeSignatureDesc() {
    MapStateDescriptor<String, Map<String, Map<Integer, Long[]>>> attributeSignatureDesc = MapStateDesc.attributeSignatureDesc;
    assertThat(attributeSignatureDesc.getName()).isEqualTo("broadcast-attributeSignature-state");
  }
}