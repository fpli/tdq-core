package com.ebay.tdq

import java.util.{List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.utils._
import com.google.common.collect.Lists
import org.apache.commons.io.IOUtils
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.SourceFunction

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
object ProfilingJobLocalTest extends ProfilingJob {
  def main(args: Array[String]): Unit = {
    // step0: prepare environment
    tdqEnv = new TdqEnv
    env = FlinkEnvFactory.create(null, true)
    ProfilingJobIT.setupDB(IOUtils.toString(
      classOf[ProfilingJob].getResourceAsStream("/metrics/q2/Qualification Age - NQT.json")))
    // step1: build data source
    val rawEventDataStream = buildSource(env)
    // step2: normalize event to metric
    val normalizeOperator = normalizeMetric(env, rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    print(outputTags)
    outputTags.asScala.foreach { case (k, v) =>
      val uid = "console_out_" + k
      v.print()
        .name(uid)
        .uid(uid)
    }
    env.execute("Tdq Job")
  }

  def getSampleData: Seq[RawEvent] = try {
    val is = classOf[ProfilingJob].getResourceAsStream("/pathfinder_raw_event.txt")
    try {
      IOUtils.readLines(is).asScala.map(json => {
        val event = JsonUtils.parseObject(json, classOf[RawEvent])
        event
      })
    } finally if (is != null) is.close()
  }


  private def buildSource(env: StreamExecutionEnvironment): JList[DataStream[RawEvent]] = {
    Lists.newArrayList(env.addSource(new SourceFunction[RawEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[RawEvent]): Unit = {
        val sample = getSampleData
        //        while (true) {
        sample.foreach(e => {
          ctx.collect(e)
        })
        //        }
        //        Thread.sleep(10000000)
      }

      override def cancel(): Unit = {}
    }).name("Raw Event Src1")
      .uid("raw-event-src1")
      .slotSharingGroup("src1")
      //      .assignTimestampsAndWatermarks(
      //        WatermarkStrategy
      //          .forBoundedOutOfOrderness[RawEvent](Duration.ofMinutes(5))
      //          .withTimestampAssigner(new SerializableTimestampAssigner[RawEvent] {
      //            override def extractTimestamp(element: RawEvent, recordTimestamp: Long): Long = {
      //              element.getEventTimestamp
      //            }
      //          })
      //      )
      //      .slotSharingGroup("src1")
      //      .name("Raw Event Watermark Src1")
      //      .uid("raw-event-watermark-src1")
      //      .slotSharingGroup("src1")
    )
  }
}
