package com.ebay.tdq

import java.time.Duration
import java.util.{List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.functions.TdqRawEventProcessFunction
import com.ebay.tdq.rules.PhysicalPlans
import com.ebay.tdq.utils.{FlinkEnvFactory, JsonUtils, PhysicalPlanFactory, TdqConstant}
import com.google.common.collect.{Lists, Sets}
import org.apache.commons.io.IOUtils
import org.apache.flink.api.common.eventtime.{SerializableTimestampAssigner, WatermarkStrategy}
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.collection.JavaConverters.{asScalaBufferConverter, mapAsScalaMapConverter}
import scala.util.Random

/**
 * @author juntzhang
 */
object ProfilingJobLocalTest extends ProfilingJob {
  def main(args: Array[String]): Unit = {
    TdqConstant.SINK_TYPES = Sets.newHashSet("console")
    // step0: prepare environment
    val env: StreamExecutionEnvironment = FlinkEnvFactory.create(null, true)
    // step1: build data source
    val rawEventDataStream = buildSource(env)
    // step2: normalize event to metric
    val normalizeOperator = normalizeEvent(env, rawEventDataStream)
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

  val config: String =
    """
      |{
      |  "id": "18",
      |  "name": "cfg_18",
      |  "rules": [
      |    {
      |      "name": "rule_18",
      |      "type": "realtime.rheos.profiler",
      |      "config": {"window": "1min"},
      |      "profilers": [
      |        {
      |          "metric-name": "1min_metrics_aggr_params",
      |          "expression": {"operator": "Expr", "config": {"text": "total_cnt"}},
      |          "dimensions": ["domain", "site_id", "app"],
      |          "transformations": [
      |            {
      |              "alias": "domain",
      |              "expression": {"operator": "UDF", "config": {"text": "PAGE_FAMILY(CAST(TAG_EXTRACT('p') AS INTEGER))"}}
      |            },
      |            {
      |              "alias": "site_id",
      |              "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('t') AS INTEGER)"}}
      |            },
      |            {"alias": "app", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('app')"}}},
      |            {
      |              "alias": "page_id",
      |              "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}}
      |            },
      |            {"alias": "total_cnt", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}},
      |            {
      |              "alias": "duration_sum",
      |              "expression": {"operator": "SUM", "config": {"arg0": "CAST(TAG_EXTRACT('TDuration') AS DOUBLE)"}}
      |            },
      |            {
      |              "alias": "gm_t_usr_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('p')) > 0 then 1.0 else 0.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "gm_t_itm_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {
      |                  "arg0": "case when LENGTH( REGEXP_EXTRACT(TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm'), '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |                }
      |              }
      |            },
      |            {
      |              "alias": "gm_t_dn_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('dn')) > 0 then 1.0 else 0.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "gm_t_mav_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('mav')) > 0 then 1.0 else 0.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "gm_t_mos_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('mos')) > 0 then 1.0 else 0.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "gm_t_osv_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('osv')) > 0 then 1.0 else 0.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "ep_site_icr_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when length(TAG_EXTRACT('t'))>0 and length(TAG_EXTRACT('es'))>0 and TAG_EXTRACT('t') = TAG_EXTRACT('es') then 0.0 else 1.0 end"}
      |              }
      |            },
      |            {
      |              "alias": "sch_t_cpnip_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('cpnip')) > 0 then 1.0 else 0.0 end"}
      |              },
      |              "filter": "page_id in (2047936,2054032,2053742,2045573,2351460,2381081) and LENGTH(TAG_EXTRACT('eactn')) > 0"
      |            },
      |            {
      |              "alias": "sch_t_prof_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('prof')) > 0 then 1.0 else 0.0 end"}
      |              },
      |              "filter": "page_id in (2047936,2054032,2053742,2045573,2351460,2381081) and LENGTH(TAG_EXTRACT('eactn')) > 0"
      |            },
      |            {
      |              "alias": "sch_t_icpp_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('icpp')) > 0 then 1.0 else 0.0 end"}
      |              },
      |              "filter": "page_id in (2047936,2054032,2053742,2045573,2351460,2381081) and LENGTH(TAG_EXTRACT('eactn')) > 0"
      |            },
      |            {
      |              "alias": "sch_t_clktrack_cnt",
      |              "expression": {
      |                "operator": "Sum",
      |                "config": {"arg0": "case when LENGTH(TAG_EXTRACT('clktrack')) > 0 then 1.0 else 0.0 end"}
      |              },
      |              "filter": "page_id in (2047936,2054032,2053742,2045573,2351460,2381081) and LENGTH(TAG_EXTRACT('eactn')) > 0"
      |            }
      |          ]
      |        }
      |      ]
      |    }
      |  ]
      |}
      |""".stripMargin

  override protected def getConfigDS(env: StreamExecutionEnvironment): DataStream[PhysicalPlans] = {
    env.addSource(new RichSourceFunction[PhysicalPlans]() {
      @throws[Exception]
      override def run(ctx: SourceFunction.SourceContext[PhysicalPlans]): Unit = {
        ctx.collectWithTimestamp(ProfilingSqlParserTest.getPhysicalPlan(config), System.currentTimeMillis)
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
    descriptor: MapStateDescriptor[String, PhysicalPlans]): TdqRawEventProcessFunction = {
    new TdqRawEventProcessFunction(descriptor) {
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
          //          SojSerializableTimestampAssigner.getEventTime(event) +
          System.currentTimeMillis() +
            60000L * (Math.abs(new Random().nextInt()) % 5))
        event
      })
    } finally if (is != null) is.close()
  }


  private def buildSource(env: StreamExecutionEnvironment): JList[DataStream[RawEvent]] = {
    Lists.newArrayList(env.addSource(new SourceFunction[RawEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[RawEvent]): Unit = {
        Thread.sleep(1000)
        val sample = getSampleData
        sample.foreach(e => {
          ctx.collect(e)
        })
        Thread.sleep(5000)
        sample.last.setEventTimestamp(
          System.currentTimeMillis() +
            60000L * 40)
        ctx.collect(sample.last)
        Thread.sleep(5000)
        ctx.collect(sample.last)
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
