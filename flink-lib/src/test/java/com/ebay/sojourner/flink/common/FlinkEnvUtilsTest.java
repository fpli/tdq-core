package com.ebay.sojourner.flink.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlinkEnvUtilsTest {

  @BeforeEach
  void setUp() {
    FlinkEnvUtils.prepare(new String[]{"--profile", "qa"});
  }

  @Test
  void prepare() {
    assertThat(FlinkEnvUtils.getString("profile")).isEqualTo("qa");
  }

  @Test
  void getString() {
    assertThat(FlinkEnvUtils.getString("flink.app.name")).isEqualTo("Sojourner RT Pipeline");
  }

  @Test
  void getInteger() {
    assertThat(FlinkEnvUtils.getInteger("flink.app.checkpoint.interval-ms")).isEqualTo(300000);
  }

  @Test
  void getBoolean() {
    assertThat(FlinkEnvUtils.getBoolean("flink.app.hot-deploy")).isFalse();
  }

  @Test
  void getListString() {
    assertThat(FlinkEnvUtils.getListString("kafka.consumer.bootstrap-servers.rno")).isEqualTo("rhs-glrvkiaa-kfk-rno-1.rheos-streaming-prod.svc.25.tess.io:9092,rhs-glrvkiaa-kfk-rno-2.rheos-streaming-prod.svc.25.tess.io:9092");
  }

  @Test
  void getSet() {
    assertThat(FlinkEnvUtils.getSet("kafka.consumer.bootstrap-servers.lvs").size()).isEqualTo(1);
    assertThat(FlinkEnvUtils.getSet("kafka.consumer.bootstrap-servers.lvs")).contains("rhs-swsvkiaa-kfk-lvs-1.rheos-streaming-prod.svc.38.tess.io:9092");
  }

  @Test
  void getList() {
    assertThat(FlinkEnvUtils.getList("kafka.consumer.bootstrap-servers.rno").size()).isEqualTo(2);
    assertThat(FlinkEnvUtils.getList("kafka.consumer.bootstrap-servers.rno")).contains("rhs-glrvkiaa-kfk-rno-1.rheos-streaming-prod.svc.25.tess.io:9092", "rhs-glrvkiaa-kfk-rno-2.rheos-streaming-prod.svc.25.tess.io:9092");
  }
}