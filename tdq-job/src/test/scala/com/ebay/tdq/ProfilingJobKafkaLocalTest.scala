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
      "--flink.app.name", "tdq.local.daf_checkout",
      "--flink.app.local", "true",
      "--flink.app.window.supports", "1min",
      "--flink.app.sink.normal-metric.std-name", "NOR",
      "--flink.app.sink.latency-metric.std-name", "LAT",
      "--flink.app.sink.sample-log.std-name", "SAM",
      "--flink.app.name", "tdq.local.daf_checkout"
    ))

    //    val args = Array[String](
    //      "--flink.app.name", "tdq.local.daf_checkout",
    //      "--flink.app.local", "true",
    //      "--flink.app.sink.raw-data.hdfs-path", "target/test/raw-data"
    //    )
    //    ProfilingJobKafkaLocalTest().setup(args)
    //    new SojEventDumpJob().submit(args)
  }
}

