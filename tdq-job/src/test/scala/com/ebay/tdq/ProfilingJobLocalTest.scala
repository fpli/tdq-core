package com.ebay.tdq

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.sources.MemorySourceFactory
import com.ebay.tdq.utils._
import org.apache.commons.io.IOUtils
import org.apache.flink.streaming.api.functions.source.SourceFunction

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
case class ProfilingJobLocalTest() extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    super.setup(args)
    setupDB(IOUtils.toString(
      classOf[ProfilingJob].getResourceAsStream("/metrics/tdq.local.memory.json")))

    MemorySourceFactory.setSourceFunction(new SourceFunction[TdqEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[TdqEvent]): Unit = {
        val sample = getSampleData
        //        while (true) {
        sample.foreach(e => {
          ctx.collect(new TdqEvent(e))
        })
        //        }
        //        Thread.sleep(10000000)
      }

      override def cancel(): Unit = {}
    })
  }

  def getSampleData: Seq[RawEvent] = try {
    val is = classOf[ProfilingJob].getResourceAsStream("/pathfinder_raw_event.txt")
    try {
      IOUtils.readLines(is).asScala.map(json => {
        val event = JsonUtils.parseObject(json, classOf[RawEvent])
        event
      })
    } finally {
      if (is != null) is.close()
    }
  }
}

object ProfilingJobLocalTest {
  def main(args: Array[String]): Unit = {
    ProfilingJobLocalTest().submit(Array[String](
      "--flink.app.local", "true",
      "--flink.app.sink.types.normal-metric", "console",
      "--flink.app.name", "tdq.local.memory"
    ))
  }
}