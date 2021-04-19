package com.ebay.tdq.rules.physical

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.rules.TdqMetric
import com.ebay.tdq.rules.expressions.{DebugEvent, Expression, InternalRow, TimeCost}

import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * @author juntzhang
 */
case class AggrPhysicalPlan(filter: Expression = null, evaluation: Expression)

case class PhysicalPlan(
  metricKey: String,
  window: Long,
  evaluation: Expression,
  aggregations: Seq[AggrPhysicalPlan],
  dimensions: Seq[Expression],
  filter: Expression
) {
  def process(rawEvent: RawEvent): TdqMetric = {
    val cacheData = new JHashMap[String, Any]()
    val metric = new TdqMetric(metricKey, rawEvent.getEventTimestamp)
    metric.setWindow(window)
    metric.setPhysicalPlan(this)
    cacheData.put("__RAW_EVENT", rawEvent)
    val input = InternalRow(Array(metric), cacheData)
    if (where(input)) {
      dimensions.foreach(dim => {
        metric.putTag(dim.cacheKey.get, dim.call(input, fromCache = false))
      })
      aggregations.foreach(aggr => {
        if (aggr.filter != null) {
          if (where(input, aggr)) {
            groupBy(metric, input, aggr)
          }
        } else {
          groupBy(metric, input, aggr)
        }
      })
      metric.genUID()
      metric
    } else {
      null
    }
  }

  val c = DebugEvent("groupBy")

  private def groupBy(metric: TdqMetric, input: InternalRow, aggr: AggrPhysicalPlan): Unit = {
    val s = System.currentTimeMillis()
    try {
      metric.getExprMap.put(aggr.evaluation.cacheKey.get, aggr.evaluation.call(input, fromCache = false).asInstanceOf[Number])
    } finally {
      val t = System.currentTimeMillis() - s
      TimeCost.debug.put(c, TimeCost.debug.getOrDefault(c, 0L) + t)
    }
  }

  val a = DebugEvent("filter")

  private def where(input: InternalRow): Boolean = {
    val s = System.currentTimeMillis()
    try {
      filter != null && filter.call(input, fromCache = false).asInstanceOf[Boolean]
    } finally {
      val t = System.currentTimeMillis() - s
      TimeCost.debug.put(a, TimeCost.debug.getOrDefault(a, 0L) + t)
    }
  }

  val b = DebugEvent("aggr.filter")

  private def where(input: InternalRow, aggr: AggrPhysicalPlan): Boolean = {
    val s = System.currentTimeMillis()
    try {
      aggr.filter.call(input, fromCache = false).asInstanceOf[Boolean]
    } finally {
      val t = System.currentTimeMillis() - s
      TimeCost.debug.put(b, TimeCost.debug.getOrDefault(b, 0L) + t)
    }
  }

  def evaluate(metric: TdqMetric): Unit = {
    val cacheData = new JHashMap[String, Any]()
    metric.getExprMap.asScala.foreach { case (k: String, v) =>
      cacheData.put(k, v)
    }
    val input = InternalRow(Array(metric), cacheData)
    metric.setValue(evaluation.call(input, fromCache = true).asInstanceOf[Number].doubleValue())
  }
}

object PhysicalPlan {
  def evaluate(metric: TdqMetric): Unit = {
    metric.getPhysicalPlan.evaluate(metric)
  }
}
