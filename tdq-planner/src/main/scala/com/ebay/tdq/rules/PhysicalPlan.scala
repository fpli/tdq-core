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

case class PhysicalPlans(plans: Array[PhysicalPlan])

case class PhysicalPlan(
  metricKey: String,
  window: Long,
  evaluation: Expression,
  aggregations: Array[AggrPhysicalPlan],
  dimensions: Array[Expression],
  filter: Expression
) extends Serializable {
  lazy val groupByEvent: DebugEvent = DebugEvent("groupBy")
  lazy val filterEvent: DebugEvent = DebugEvent("filter")
  lazy val aggrFilterEvent: DebugEvent = DebugEvent("aggr.filter")

  def uuid(): String = {
    s"${metricKey}_$window"
  }

  // TODO validate
  def validatePlan(): Unit = {
  }

  def process(rawEvent: RawEvent): TdqMetric = {
    process(rawEvent, rawEvent.getEventTimestamp)
  }

  def process(rawEvent: RawEvent, eventTime: Long): TdqMetric = {
    val cacheData = new JHashMap[String, Any]()
    val metric = new TdqMetric(metricKey, eventTime)
    metric.setWindow(window)

    aggregations.foreach(aggr => {
      metric.putAggrExpress(aggr.name, aggr.evaluation.simpleName)
    })

    cacheData.put("__RAW_EVENT", rawEvent)

    val input = InternalRow(Array(metric), cacheData)

    if (filter == null || where(input)) {
      dimensions.foreach(dim => {
        metric.putTag(dim.cacheKey.get, dim.call(input))
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
    val t = aggr.evaluation.call(input)
    if (t != null) {
      metric.putExpr(aggr.evaluation.cacheKey.get, t.asInstanceOf[Number].doubleValue())
    }
  }

  private def where(input: InternalRow): Boolean = {
    filter != null && filter.call(input).asInstanceOf[Boolean]
  }

  private def where(input: InternalRow, aggr: AggrPhysicalPlan): Boolean = {
    aggr.filter.call(input).asInstanceOf[Boolean]
  }

  def merge(m1: TdqMetric, m2: TdqMetric): TdqMetric = {
    val m = m1
    aggregations.foreach {
      case AggrPhysicalPlan(name, _, expression) =>
        m.putExpr(name,
          expression.asInstanceOf[AggregateExpression].merge(
            m1.getExprMap.get(name),
            m2.getExprMap.get(name)
          ).asInstanceOf[Number].doubleValue()
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
    val v = evaluation.call(input)
    metric.setValue(if (v != null) v.asInstanceOf[Number].doubleValue() else 0d)
  }
}
