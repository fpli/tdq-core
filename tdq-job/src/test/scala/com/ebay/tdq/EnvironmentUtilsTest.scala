package com.ebay.tdq

import com.ebay.sojourner.common.env.EnvironmentUtils
import com.ebay.tdq.utils.TdqEnv
import org.junit.{Assert, Test}

/**
 * @author juntzhang
 */
class EnvironmentUtilsTest {
  @Test
  def test(): Unit = {
    TdqEnv.load(Array("--tdq-profile", "tdq-test"))
    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.source.hdfs."
        + com.ebay.tdq.common.env.TdqConstant.NORMAL_METRIC) == "target/test/metric/normal")

    Assert.assertTrue(
      EnvironmentUtils.getStringWithPattern("flink.app.checkpoint.data-dir") == "/tmp/tdq-test/checkpoint")
  }

}
