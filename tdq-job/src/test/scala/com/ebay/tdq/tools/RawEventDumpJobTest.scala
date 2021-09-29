package com.ebay.tdq.tools

import com.ebay.tdq.ProfilingJobIT.setupDB
import com.ebay.tdq.jobs.{ProfilingJob, RawEventDumpJob}
import org.apache.commons.io.IOUtils

/**
 * @author juntzhang
 */
object RawEventDumpJobTest {
  def main(args: Array[String]): Unit = {
    setupDB(IOUtils.toString(
      classOf[ProfilingJob].getResourceAsStream("/metrics/pathfinder/tdq.dev.pathfinder.dump.json")))
    RawEventDumpJob.main(Array[String](
      "--flink.app.name", "tdq.dev.pathfinder.dump",
      "--flink.app.local", "true"
    ))
  }
}
