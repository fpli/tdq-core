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
    assertThat(kafkaConsumerConfig.getBrokers()).contains(
        "rhs-swsvkiaa-kfk-lvs-1.rheos-streaming-prod.svc.38.tess.io:9092",
        "rhs-swsvkiaa-kfk-lvs-2.rheos-streaming-prod.svc.38.tess.io:9092",
        "rhs-swsvkiaa-kfk-lvs-3.rheos-streaming-prod.svc.38.tess.io:9092",
        "rhs-swsvkiaa-kfk-lvs-4.rheos-streaming-prod.svc.38.tess.io:9092",
        "rhs-swsvkiaa-kfk-lvs-5.rheos-streaming-prod.svc.38.tess.io:9092");
  }

  @Test
  void ofDC_rno() {
    kafkaConsumerConfig = KafkaConsumerConfig.ofDC(DataCenter.RNO);
    assertThat(kafkaConsumerConfig).isNotNull();
    assertThat(kafkaConsumerConfig.getGroupId()).isEqualTo("sojourner-pathfinder-realtime");
    assertThat(kafkaConsumerConfig.getBrokers()).contains(
        "rhs-glrvkiaa-kfk-rno-1.rheos-streaming-prod.svc.25.tess.io:9092",
        "rhs-glrvkiaa-kfk-rno-2.rheos-streaming-prod.svc.25.tess.io:9092",
        "rhs-glrvkiaa-kfk-rno-3.rheos-streaming-prod.svc.25.tess.io:9092",
        "rhs-glrvkiaa-kfk-rno-4.rheos-streaming-prod.svc.25.tess.io:9092",
        "rhs-glrvkiaa-kfk-rno-5.rheos-streaming-prod.svc.25.tess.io:9092");
  }

  @Test
  void ofDC_slc() {
    kafkaConsumerConfig = KafkaConsumerConfig.ofDC(DataCenter.SLC);
    assertThat(kafkaConsumerConfig).isNotNull();
    assertThat(kafkaConsumerConfig.getGroupId()).isEqualTo("sojourner-pathfinder-realtime");
    assertThat(kafkaConsumerConfig.getBrokers()).contains(
        "rhs-mwsvkiaa-kfk-slc-1.rheos-streaming-prod.svc.45.tess.io:9092",
        "rhs-mwsvkiaa-kfk-slc-2.rheos-streaming-prod.svc.45.tess.io:9092",
        "rhs-mwsvkiaa-kfk-slc-3.rheos-streaming-prod.svc.45.tess.io:9092",
        "rhs-mwsvkiaa-kfk-slc-4.rheos-streaming-prod.svc.45.tess.io:9092",
        "rhs-mwsvkiaa-kfk-slc-5.rheos-streaming-prod.svc.45.tess.io:9092");
  }
}