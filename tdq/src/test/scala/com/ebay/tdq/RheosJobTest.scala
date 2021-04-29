package com.ebay.tdq

import java.time.Duration
import java.util.{List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.sojourner.flink.connector.kafka.SojSerializableTimestampAssigner
import com.ebay.tdq.rules.{PhysicalPlan, TdqMetric}
import com.ebay.tdq.sinks.MemorySink
import com.ebay.tdq.utils.FlinkEnvFactory
import com.google.common.collect.Lists
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.junit.Assert

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
case class RheosJobTest(id: String, config: String,
  events: List[RawEvent], expects: List[TdqMetric]) extends Application {

  def submit(): Unit = {
    // step0: prepare environment
    val env: StreamExecutionEnvironment = FlinkEnvFactory.create(null, true)
    // step1: build data source
    val rawEventDataStream = buildSource(env)
    // step2: normalize event to metric
    val normalizeOperator: DataStream[TdqMetric] = normalizeEvent(env, rawEventDataStream)
    // step3: aggregate metric by key and window
    val outputTags = reduceMetric(normalizeOperator)
    // step4: output metric by window
    outputMetricByWindow(outputTags)
    val collect = new MemorySink(id)
    outputTags.values.iterator.next.addSink(collect).name("test result")
    env.execute("Tdq Job [id=" + id + "]")
    Assert.assertTrue(collect.check(expects.asJava))
  }

  private def buildSource(env: StreamExecutionEnvironment): JList[DataStream[RawEvent]] = {
    Lists.newArrayList(env.addSource(new SourceFunction[RawEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[RawEvent]): Unit = {
        Thread.sleep(1000)
        events.foreach(ctx.collect)
      }

      override def cancel(): Unit = {}
    }).name("Raw Event Src1")
      .uid("raw-event-src1")
      .slotSharingGroup("src1")
      .assignTimestampsAndWatermarks(
        WatermarkStrategy
          .forBoundedOutOfOrderness[RawEvent](Duration.ofSeconds(0))
          .withTimestampAssigner(new SojSerializableTimestampAssigner[RawEvent])
          .withIdleness(Duration.ofSeconds(1))
      )
      .slotSharingGroup("src1")
      .name("Raw Event Watermark Src1")
      .uid("raw-event-watermark-src1")
      .slotSharingGroup("src1"))
  }

  override protected def getConfigDS(env: StreamExecutionEnvironment): DataStream[PhysicalPlan] = {
    env.addSource(new RichSourceFunction[PhysicalPlan]() {
      @throws[Exception]
      override def run(ctx: SourceFunction.SourceContext[PhysicalPlan]): Unit = {
        ctx.collectWithTimestamp(ProfilingSqlParserTest.getPhysicalPlan(config), System.currentTimeMillis)
      }

      override def cancel(): Unit = {}
    }).name("Tdq Config Source")
      .uid("tdq-config-source")
      .assignTimestampsAndWatermarks(
        WatermarkStrategy.forBoundedOutOfOrderness[PhysicalPlan](Duration.ofMinutes(0))
          .withIdleness(Duration.ofSeconds(1))
      )
      .setParallelism(1)
      .name("Tdq Config Watermark Source")
      .uid("tdq-config-watermark-source")
  }
}
