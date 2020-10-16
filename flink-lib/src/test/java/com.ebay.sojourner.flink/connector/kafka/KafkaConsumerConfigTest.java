package com.ebay.sojourner.flink.connector.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import com.ebay.sojourner.flink.common.DataCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KafkaConsumerConfigTest {

  KafkaConsumerConfig kafkaConsumerConfig;

  @BeforeEach
  void setUp() {
    kafkaConsumerConfig = null;
  }

  @Test
  void ofDC_lvs() {
    kafkaConsumerConfig = KafkaConsumerConfig.ofDC(DataCenter.LVS);
    assertThat(kafkaConsumerConfig).isNotNull();
    assertThat(kafkaConsumerConfig.getGroupId()).isEqualTo("sojourner-pathfinder-realtime");
  }

  @Test
  void ofDC_rno() {
    kafkaConsumerConfig = KafkaConsumerConfig.ofDC(DataCenter.RNO);
    assertThat(kafkaConsumerConfig).isNotNull();
    assertThat(kafkaConsumerConfig.getGroupId()).isEqualTo("sojourner-pathfinder-realtime");
  }

  @Test
  void ofDC_slc() {
    kafkaConsumerConfig = KafkaConsumerConfig.ofDC(DataCenter.SLC);
    assertThat(kafkaConsumerConfig).isNotNull();
    assertThat(kafkaConsumerConfig.getGroupId()).isEqualTo("sojourner-pathfinder-realtime");
  }
}