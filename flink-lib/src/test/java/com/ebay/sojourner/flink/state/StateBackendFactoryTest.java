package com.ebay.sojourner.flink.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.junit.jupiter.api.Test;

class StateBackendFactoryTest {

  @Test
  void getStateBackend_fs() {
    StateBackend fs = StateBackendFactory.getStateBackend("FS");
    assertThat(fs).isInstanceOf(FsStateBackend.class);
  }

  @Test
  void getStateBackend_rocksdb() {
    StateBackend fs = StateBackendFactory.getStateBackend("ROCKSDB");
    assertThat(fs).isInstanceOf(RocksDBStateBackend.class);
  }

  @Test
  void getStateBackend_error() {
    assertThrows(RuntimeException.class, () -> StateBackendFactory.getStateBackend("OTHER"));
  }
}