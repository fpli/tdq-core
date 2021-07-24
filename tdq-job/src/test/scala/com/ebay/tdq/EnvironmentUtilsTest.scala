package com.ebay.tdq

import com.ebay.sojourner.common.env.EnvironmentUtils
import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.utils.TdqContext
import org.junit.{Assert, Test}

/**
 * @author juntzhang
 */
class EnvironmentUtilsTest {
  @Test
  def test(): Unit = {
    new TdqContext(Array("--tdq-profile", "tdq-test"))
    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.sink.normal-metric.hdfs-path") == "target/test/metric/normal")

    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.checkpoint.data-dir") == "/tmp/tdq-test/checkpoint")
  }

}
