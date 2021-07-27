package com.ebay.tdq.rules

import java.util.{Random, HashMap => JHashMap}

import com.ebay.tdq.common.model.{TdqEvent, TdqMetric}
import com.ebay.tdq.expressions._

import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * @author juntzhang
 */

case class Transformation(name: String, filter: Expression = null, expr: Expression)

case class PhysicalPlanContext(
  sampling: Boolean = false,
  samplingFraction: Double = 0.0001,
  prontoDropdownExpr: String,
  prontoFilterExpr: String
)

case class PhysicalPlan(
  metricKey: String,
  window: Long,
  evaluation: Expression,
  aggregations: Array[Transformation],
  dimensions: Array[Transformation],
  filter: Expression,
  cxt: Option[PhysicalPlanContext] = None
) extends Serializable {
  lazy val groupByEvent: DebugEvent = DebugEvent("groupBy")
  lazy val filterEvent: DebugEvent = DebugEvent("filter")
  lazy val aggrFilterEvent: DebugEvent = DebugEvent("aggr.filter")
  lazy val random = new Random()

  def uuid(): String = {
    s"${metricKey}_$window"
  }

  def sampling(): Boolean = {
    cxt.isDefined && cxt.get.sampling && cxt.get.samplingFraction > 0 && cxt.get.samplingFraction < 1 &&
      Math.abs(random.nextDouble) < cxt.get.samplingFraction
  }

  // TODO validate
  def validatePlan(): Unit = {}

  def process(event: TdqEvent): TdqMetric = {
    val cacheData = new JHashMap[String, Any]()
    val eventTimeMillis = event.getEventTimeMs
    val metric = new TdqMetric(metricKey, (eventTimeMillis / 60000) * 60000)
    metric.setWindow(window)
    aggregations.foreach(aggr => {
      metric.putAggrExpress(aggr.name, aggr.expr.simpleName)
    })
    cacheData.put("__TDQ_EVENT", event)

    val input = InternalRow(Array(metric), cacheData)
    if (where(input, filter)) {
      dimensions.foreach(d => {
        metric.putTag(d.name, d.expr.call(input))
        if (!where(input, d.filter)) {
          metric.removeTag(d.name)
        }
      })
      aggregations.foreach(a => {
        if (where(input, a.filter)) {
          groupBy(metric, input, a)
        }
      })
      metric.genUID()
      metric
    } else {
      null
    }
  }

  private def groupBy(metric: TdqMetric, input: InternalRow, aggr: Transformation): Unit = {
    val t = aggr.expr.call(input)
    if (t != null) {
      metric.putExpr(aggr.expr.cacheKey.get, t.asInstanceOf[Number].doubleValue())
    }
  }

  private def where(input: InternalRow, filter: Expression): Boolean = {
    filter == null || (filter != null && filter.call(input).asInstanceOf[Boolean])
  }

  def merge(m1: TdqMetric, m2: TdqMetric): TdqMetric = {
    val m = m1
    aggregations.foreach {
      case Transformation(name, _, expression) =>
        val newV = ExpressionRegistry.aggregateOperator(
          expression.simpleName,
          m1.getValues.getOrDefault(name, 0d),
          m2.getValues.getOrDefault(name, 0d)
        )
        m1.putExpr(name, newV)
    }
    m
  }

  def evaluate(metric: TdqMetric): Unit = {
    val cacheData = new JHashMap[String, Any]()
    metric.getValues.asScala.foreach { case (k: String, v) =>
      cacheData.put(k, v)
    }
    val input = InternalRow(Array(metric), cacheData)
    val v = evaluation.call(input)
    metric.setValue(if (v != null) v.asInstanceOf[Number].doubleValue() else 0d)
  }
}
