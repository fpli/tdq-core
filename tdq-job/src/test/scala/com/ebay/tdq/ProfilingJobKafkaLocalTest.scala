package com.ebay.tdq

import com.ebay.tdq.jobs.ProfilingJob
import org.apache.commons.io.IOUtils

/**
 * @author juntzhang
 */
case class ProfilingJobKafkaLocalTest() extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    super.setup(args)
    setupDB(IOUtils.toString(
      classOf[ProfilingJob].getResourceAsStream("/metrics/tdq.local.daf_checkout.json")))
  }
}

object ProfilingJobKafkaLocalTest {
  def main(args: Array[String]): Unit = {
    ProfilingJobKafkaLocalTest().submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.types.normal-metric", "console",
      "--flink.app.name", "tdq.local.daf_checkout"
    ))
  }
}

