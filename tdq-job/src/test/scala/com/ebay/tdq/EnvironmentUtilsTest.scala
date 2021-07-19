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


  @Test
  def testKafkaSourceEnv(): Unit = {
    TdqEnv.load(Array("--tdq-profile", "tdq-test", "--flink.app.source.from-timestamp", "1626678000000"))
    val tdqEnv = new TdqEnv()
    Assert.assertTrue(tdqEnv.getKafkaSourceEnv.isTimestampBefore(1626677759999L))
  }
}
