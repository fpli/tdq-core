package com.ebay.tdq.rules

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.expressions._
import com.ebay.tdq.expressions.aggregate.AggregateExpression

import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * @author juntzhang
 */

case class AggrPhysicalPlan(name: String, filter: Expression = null, evaluation: Expression)

case class PhysicalPlan(
  metricKey: String,
  window: Long,
  evaluation: Expression,
  aggregations: Seq[AggrPhysicalPlan],
  dimensions: Seq[Expression],
  filter: Expression
) extends Serializable {
  lazy val groupByEvent: DebugEvent = DebugEvent("groupBy")
  lazy val filterEvent: DebugEvent = DebugEvent("filter")
  lazy val aggrFilterEvent: DebugEvent = DebugEvent("aggr.filter")

  def uuid(): String = {
    s"${metricKey}_$window"
  }

  // todo validate
  def validatePlan(): Unit = {

  }

  def process(rawEvent: RawEvent): TdqMetric = {
    val cacheData = new JHashMap[String, Any]()
    val metric = new TdqMetric(metricKey, rawEvent.getEventTimestamp)
    metric.setWindow(window)
    metric.setPhysicalPlan(this)
    cacheData.put("__RAW_EVENT", rawEvent)
    val input = InternalRow(Array(metric), cacheData)
    if (filter == null || where(input)) {
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

  private def groupBy(metric: TdqMetric, input: InternalRow, aggr: AggrPhysicalPlan): Unit = {
    val s = System.currentTimeMillis()
    try {
      metric.getExprMap.put(aggr.evaluation.cacheKey.get, aggr.evaluation.call(input, fromCache = false).asInstanceOf[Number])
    } finally {
      TimeCost.put(groupByEvent, s)
    }
  }

  private def where(input: InternalRow): Boolean = {
    val s = System.currentTimeMillis()
    try {
      filter != null && filter.call(input, fromCache = false).asInstanceOf[Boolean]
    } finally {
      TimeCost.put(filterEvent, s)
    }
  }

  private def where(input: InternalRow, aggr: AggrPhysicalPlan): Boolean = {
    val s = System.currentTimeMillis()
    try {
      aggr.filter.call(input, fromCache = false).asInstanceOf[Boolean]
    } finally {
      TimeCost.put(aggrFilterEvent, s)
    }
  }

  def merge(m1: TdqMetric, m2: TdqMetric): TdqMetric = {
    val m = m1.copy
    if (m.getEventTime < m2.getEventTime) {
      m.setEventTime(m2.getEventTime)
    }
    aggregations.foreach {
      case AggrPhysicalPlan(name, _, expression) =>
        m.putExpr2(name,
          expression.asInstanceOf[AggregateExpression].merge(
            m1.getExprMap.get(name),
            m2.getExprMap.get(name)
          ).asInstanceOf[Number]
        )
    }
    m
  }

  def evaluate(metric: TdqMetric): Unit = {
    val cacheData = new JHashMap[String, Any]()
    metric.getExprMap.asScala.foreach { case (k: String, v) =>
      cacheData.put(k, v)
    }
    val input = InternalRow(Array(metric), cacheData)
    val v = evaluation.call(input, fromCache = true)
    metric.setValue(if (v != null) v.asInstanceOf[Number].doubleValue() else 0d)
  }
}
