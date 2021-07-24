package com.ebay.tdq

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.rules.{PhysicalPlan, TdqMetric}
import com.ebay.tdq.utils.{LocalCache, TdqMetricGroup}
import org.apache.commons.lang.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test

import scala.collection.mutable
import scala.util.Random

/**
 * @author juntzhang
 */
class LocalCacheTest {

  class TdqMetricGroupMock extends TdqMetricGroup {
    var counterMap = new mutable.HashMap[String, Long]
    private var tdqProcessEventsMeter = 0L

    def get(): Map[String, Long] = {
      counterMap.toMap
    }

    override def markEvent(): Unit = {
      tdqProcessEventsMeter += 1
    }

    override def inc(key: String): Unit = {
      inc(key, 1)
    }

    override def inc(key: String, v: Long): Unit = {
      if (counterMap.isDefinedAt(key)) {
        counterMap.put(key, counterMap(key) + v)
      } else {
        counterMap.put(key, v)
      }
    }
  }

  val config: String =
    """
      |{
      |  "id": "10",
      |  "name": "cfg_10",
      |  "rules": [
      |    {
      |      "name": "rule_10",
      |      "type": "realtime.rheos.profiler",
      |      "config": {"window": "1min"},
      |      "profilers": [
      |        {
      |          "metric-name": "global_cnt_by_1min",
      |          "expression": {"operator": "Expr", "config": {"text": "p1"}},
      |          "transformations": [{"alias": "p1", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}}]
      |        }
      |      ]
      |    },
      |    {
      |      "name": "rule_6",
      |      "type": "realtime.rheos.profiler",
      |      "config": {"window": "5min"},
      |      "profilers": [
      |        {
      |          "metric-name": "global_cnt_by_5min",
      |          "expression": {"operator": "Expr", "config": {"text": "p1"}},
      |          "transformations": [{"alias": "p1", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}}]
      |        }
      |      ]
      |    },
      |    {
      |      "name": "rule_8",
      |      "type": "realtime.rheos.profiler",
      |      "config": {"window": "5min"},
      |      "profilers": [
      |        {
      |          "metric-name": "global_cnt_by_10min",
      |          "expression": {"operator": "Expr", "config": {"text": "p1"}},
      |          "transformations": [{"alias": "p1", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}}]
      |        }
      |      ]
      |    }
      |  ]
      |}
      |""".stripMargin
  val physicalPlan: PhysicalPlan = PhysicalPlanFactory.getPhysicalPlan(config)

  @Test
  def test(): Unit = {
    def format(t: Long): String = {
      DateFormatUtils.format(t / 300000L * 300000L, "yyyy-MM-dd HH:mm")
    }

    val mock = new TdqMetricGroupMock
    val env = new TdqEnv()
    env.getLocalCacheEnv.setLocalCombineQueueSize(12)
    env.getLocalCacheEnv.setLocalCombineFlushTimeout(1000)
    val cache = new LocalCache(env, mock)
    val rawData = new mutable.HashMap[String, Double]()
    val mergeData = new mutable.HashMap[String, Double]()

    def run(metric: TdqMetric): Unit = {
      metric.setMetricKey(physicalPlan.metricKey)
      metric.genUID()
      cache.flush(physicalPlan, metric, new org.apache.flink.util.Collector[TdqMetric] {
        override def collect(record: TdqMetric): Unit = {
          val k = physicalPlan.uuid() + format(record.getEventTime)
          val old = mergeData.get(k)
          if (old.isDefined) {
            mergeData.put(k, old.get + record.getValues.get("p1").asInstanceOf[Double])
          } else {
            mergeData.put(k, record.getValues.get("p1").asInstanceOf[Double])
          }
        }

        override def close(): Unit = {
        }
      })
    }

    (1 to 100000).foreach { i =>
      val m = new TdqMetric("a", DateUtils.parseDate("2021-05-06 12:05:50", "yyyy-MM-dd HH:mm:ss").getTime + 60000L * (Math.abs(new Random().nextInt()) % 10))
        .putExpr("p1", 1d)
        .genUID()
      run(m)

      val k = m.getTagId + format(m.getEventTime)
      val old = rawData.get(k)
      if (old.isDefined) {
        rawData.put(k, old.get + 1d)
      } else {
        rawData.put(k, 1d)
      }
    }
    Thread.sleep(1000)
    run(new TdqMetric("a", DateUtils.parseDate("2021-05-06 12:05:50", "yyyy-MM-dd HH:mm:ss").getTime + 3600000L)
      .putExpr("p1", 1d)
      .genUID())
    println("=raw_data=")
    rawData.foreach(println)
    println("=merge_data=")
    mergeData.foreach(println)
    println("=MetricGroup=")
    mock.get().foreach(println)

  }
}
