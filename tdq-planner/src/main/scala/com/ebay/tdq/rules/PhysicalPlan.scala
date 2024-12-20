package com.ebay.tdq.rules

import java.util.{Random, HashMap => JHashMap, Map => JMap}

import com.ebay.tdq.common.model.{InternalMetric, TdqEvent}
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
  metricName: String,
  window: Long,
  evaluation: Option[Expression],
  aggregations: Array[Transformation],
  dimensions: Array[Transformation],
  filter: Expression,
  cxt: Option[PhysicalPlanContext] = None
) extends Serializable {
  lazy val random = new Random()

  def uuid(): String = {
    s"${metricName}_$window"
  }

  def sampling(): Boolean = {
    cxt.isDefined && cxt.get.sampling && cxt.get.samplingFraction > 0 && cxt.get.samplingFraction < 1 &&
      Math.abs(random.nextDouble) < cxt.get.samplingFraction
  }

  // TODO validate
  def validatePlan(): Unit = {}

  def process(event: TdqEvent): InternalMetric = {
    val cacheData = new JHashMap[String, Any]()
    val eventTimeMillis = event.getEventTimeMs
    val metric = new InternalMetric(metricName, (eventTimeMillis / 60000) * 60000)
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
      metric.genMetricId()
      metric
    } else {
      null
    }
  }

  private def groupBy(metric: InternalMetric, input: InternalRow, aggr: Transformation): Unit = {
    val t = aggr.expr.call(input)
    if (t != null) {
      metric.putExpr(aggr.expr.cacheKey.get, t.asInstanceOf[Number].doubleValue())
    }
  }

  private def where(input: InternalRow, filter: Expression): Boolean = {
    filter == null || (filter != null && filter.call(input).asInstanceOf[Boolean])
  }

  def merge(m1: InternalMetric, m2: InternalMetric): InternalMetric = {
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

  def evaluate(metric: InternalMetric): Unit = {
    if (evaluation.isDefined) {
      val cacheData = new JHashMap[String, Any]()
      metric.getValues.asScala.foreach { case (k: String, v) =>
        cacheData.put(k, v)
      }
      val input = InternalRow(Array(metric), cacheData)
      val v = evaluation.get.call(input)
      metric.setValue(if (v != null) v.asInstanceOf[Number].doubleValue() else 0d)
    }
  }

  override def toString: String = {
    s"metricKey=$metricName,window=$window,evaluation=$evaluation,aggregations=${aggregations.mkString(";")},dimensions=${dimensions.mkString(";")}"
  }
}

object PhysicalPlan {
  def eval(evaluation: Expression, event: TdqEvent): Any = {
    val cache = new JHashMap[String, Any]()
    cache.put("__TDQ_EVENT", event)
    PhysicalPlan.eval(evaluation, cache);
  }

  def eval(evaluation: Expression, cacheData: JMap[String, Any]): Any = {
    evaluation.call(InternalRow(null, cacheData, true))
  }
}