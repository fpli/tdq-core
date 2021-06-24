package com.ebay.tdq

import java.time.Duration
import java.util.{List => JList}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.functions.RawEventProcessFunction
import com.ebay.tdq.rules.PhysicalPlans
import com.ebay.tdq.utils.{FlinkEnvFactory, JsonUtils, PhysicalPlanFactory, TdqConstant}
import com.google.common.collect.{Lists, Sets}
import org.apache.commons.io.IOUtils
import org.apache.flink.api.common.eventtime.WatermarkStrategy
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
      |  "id": "27", "name": "cfg_27", "rules": [
      |  {
      |    "name": "rule_27", "type": "realtime.rheos.profiler", "config": {"window": "5min"}, "profilers": [
      |    {
      |      "metric-name": "performance_test_27_5min",
      |      "expression": {"operator": "Expr", "config": {"text": "total_cnt"}},
      |      "dimensions": ["domain", "site_id", "app"],
      |      "transformations": [
      |        {"alias": "soj_tag_p", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('p')"}}},
      |        {"alias": "soj_tag_u", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('u')"}}},
      |        {
      |          "alias": "soj_tag_itm",
      |          "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm')"}}
      |        },
      |        {"alias": "soj_tag_dn", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('dn')"}}},
      |        {"alias": "soj_tag_mav", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('mav')"}}},
      |        {"alias": "soj_tag_mos", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('mos')"}}},
      |        {"alias": "soj_tag_osv", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('osv')"}}},
      |        {"alias": "soj_tag_es", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('es')"}}},
      |        {"alias": "soj_tag_t", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('t')"}}},
      |        {"alias": "soj_tag_app", "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('app')"}}},
      |        {
      |          "alias": "soj_tag_duration",
      |          "expression": {"operator": "UDF", "config": {"text": "TAG_EXTRACT('TDuration')"}}
      |        },
      |        {"alias": "total_cnt", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}},
      |        {
      |          "alias": "domain",
      |          "expression": {"operator": "UDF", "config": {"text": "PAGE_FAMILY(CAST(soj_tag_p AS INTEGER))"}}
      |        },
      |        {"alias": "site_id", "expression": {"operator": "UDF", "config": {"text": "CAST(soj_tag_t AS INTEGER)"}}},
      |        {"alias": "app", "expression": {"operator": "Expr", "config": {"text": "soj_tag_app"}}},
      |        {"alias": "page_id", "expression": {"operator": "UDF", "config": {"text": "CAST(soj_tag_p AS INTEGER)"}}},
      |        {
      |          "alias": "duration_sum",
      |          "expression": {"operator": "SUM", "config": {"arg0": "CAST(soj_tag_duration AS DOUBLE)"}}
      |        },
      |        {
      |          "alias": "gm_t_usr_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "ep_site_icr_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when length(soj_tag_t)>0 and length(soj_tag_es)>0 and soj_tag_t = soj_tag_es then 0.0 else 1.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_2_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_2_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn_2_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_2_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_2_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_2_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_3_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_3_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_3_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_3_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_3_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_3_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_4_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_4_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_4_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_4_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_4_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_4_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_5_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_5_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_5_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_5_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_5_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_5_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_6_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_6_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_6_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_6_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_6_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_6_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_7_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_7_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_7_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_7_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_7_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_7_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_8_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_8_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_8_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_8_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_8_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_8_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_9_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_9_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_9_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_9_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_9_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_9_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_usr_10_cnt",
      |          "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}
      |        },
      |        {
      |          "alias": "gm_t_itm_10_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {
      |              "arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"
      |            }
      |          }
      |        },
      |        {
      |          "alias": "gm_t_dn1_10_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mav_10_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_mos_10_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}
      |          }
      |        },
      |        {
      |          "alias": "gm_t_osv_10_cnt",
      |          "expression": {
      |            "operator": "Sum",
      |            "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}
      |          }
      |        }
      |      ]
      |    }
      |  ]
      |  }
      |]
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
    descriptor: MapStateDescriptor[String, PhysicalPlans]): RawEventProcessFunction = {
    new RawEventProcessFunction(descriptor) {
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
        while (true) {
          val sample = getSampleData
          sample.foreach(e => {
            ctx.collect(e)
          })
        }
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
