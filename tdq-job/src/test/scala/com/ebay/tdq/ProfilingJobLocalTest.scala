package com.ebay.tdq

import java.io.File
import java.net.URLEncoder
import java.time.Duration
import java.util.{List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.functions.RawEventProcessFunction
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.rules.PhysicalPlans
import com.ebay.tdq.utils._
import com.google.common.collect.{Lists, Sets}
import org.apache.commons.io.{FileUtils, IOUtils}
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
object ProfilingJobLocalTest extends ProfilingJob {
  def main(args: Array[String]): Unit = {
    // step0: prepare environment
    env = FlinkEnvFactory.create(null, true)
    tdqEnv = new TdqEnv

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

  val config: String = IOUtils.toString(new File("/Users/juntzhang/src/TDQ/tdq-parent/tdq-job/src/test/resources/metrics/q2/Qualification Age - NQT.json").toURI)

  override protected def getConfigDS(env: StreamExecutionEnvironment): DataStream[PhysicalPlans] = {
    env.addSource(new RichSourceFunction[PhysicalPlans]() {
      @throws[Exception]
      override def run(ctx: SourceFunction.SourceContext[PhysicalPlans]): Unit = {
         val plans = ProfilingSqlParserTest.getPhysicalPlans(config)
        ctx.collectWithTimestamp(plans, System.currentTimeMillis)
      }

      override def cancel(): Unit = {}
    }).name("Tdq Config Source")
      .uid("tdq-config-source")
      .assignTimestampsAndWatermarks(
        WatermarkStrategy.forBoundedOutOfOrderness[PhysicalPlans](Duration.ofMinutes(0))
          .withIdleness(Duration.ofSeconds(1))
      )
      .setParallelism(1)
      .name("Tdq Config Watermark Source")
      .uid("tdq-config-watermark-source")
  }

  override def getTdqRawEventProcessFunction(
    descriptor: MapStateDescriptor[String, PhysicalPlans]): RawEventProcessFunction = {
    new RawEventProcessFunction(descriptor, new TdqEnv()) {
      override protected def getPhysicalPlans: PhysicalPlans = PhysicalPlanFactory.getPhysicalPlans(
        Lists.newArrayList(JsonUtils.parseObject(config, classOf[TdqConfig]))
      )
    }
  }

  def getSampleData: Seq[RawEvent] = try {
    val is = classOf[ProfilingJob].getResourceAsStream("/pathfinder_raw_event.txt")
    try {
      IOUtils.readLines(is).asScala.map(json => {
        val event = JsonUtils.parseObject(json, classOf[RawEvent])
        event.setEventTimestamp(
          SojSerializableTimestampAssigner.getEventTime(event))
        //          System.currentTimeMillis() +
        //            60000L * (Math.abs(new Random().nextInt()) % 5))
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
