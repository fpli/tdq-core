package com.ebay.tdq

import com.ebay.sojourner.common.env.EnvironmentUtils
import com.ebay.sojourner.flink.common.FlinkEnvUtils
import com.ebay.tdq.utils.TdqEnv.NORMAL_METRIC
import org.junit.{Assert, Test}

/**
 * @author juntzhang
 */
class EnvironmentUtilsTest {
  @Test
  def test(): Unit = {
    FlinkEnvUtils.load(Array("--tdq-profile", "tdq-test"))
    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs." + NORMAL_METRIC) == "target/test/metric/normal")

    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.checkpoint.data-dir") == "/tmp/tdq-test/checkpoint")
  }
}
