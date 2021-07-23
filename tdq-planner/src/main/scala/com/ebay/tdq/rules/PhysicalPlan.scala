package com.ebay.tdq.rules

import java.util.{Random, HashMap => JHashMap, HashSet => JHashSet}

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.expressions._

import scala.collection.JavaConverters.mapAsScalaMapConverter

/**
 * @author juntzhang
 */

case class Transformation(name: String, filter: Expression = null, expr: Expression)

case class PhysicalPlans(plans: Array[PhysicalPlan])

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

  def process(rawEvent: RawEvent): TdqMetric = {
    val cacheData = new JHashMap[String, Any]()
    val eventTimeMillis = rawEvent.getUnixEventTimestamp
    val metric = new TdqMetric(metricKey, (eventTimeMillis / 60000) * 60000)
    metric.setWindow(window)
    aggregations.foreach(aggr => {
      metric.putAggrExpress(aggr.name, aggr.expr.simpleName)
    })
    cacheData.put("__RAW_EVENT", rawEvent)
    cacheData.put("soj_timestamp", rawEvent.getEventTimestamp)
    cacheData.put("event_timestamp", eventTimeMillis * 1000)
    cacheData.put("event_time_millis", eventTimeMillis)

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

  def findDimensionValues(): JHashMap[String, JHashSet[String]] = {
    val names: Set[String] = dimensions.map(_.name).toSet
    val ans = new JHashMap[String, JHashSet[String]]()
    // get from filter
    if (filter != null && filter.children != null) {
      filter.children.foreach(expr => {
        findInExpressionValues(expr, names, ans)
      })
    }

    // get from Transformation
    dimensions.filter(_.filter != null).map(_.filter).foreach(expr => {
      findInExpressionValues(expr, names, ans)
    })
    ans
  }

  def findInExpressionValues(expr: Expression, names: Set[String], ans: JHashMap[String, JHashSet[String]]): Unit = {
    expr match {
      case not: Not if not.child.isInstanceOf[In] =>
      // ignore not in
      case in: In if in.value.cacheKey.isDefined && names.contains(in.value.cacheKey.get) =>
        var v = ans.get(in.value.cacheKey.get)
        if (v == null) {
          v = new JHashSet[String]()
          ans.put(in.value.cacheKey.get, v)
        }
        in.list.foreach {
          case literal: Literal =>
            v.add(literal.value.toString)
          case _ =>
        }
      case _ =>
        expr.children.foreach(child => {
          findInExpressionValues(child, names, ans)
        })
    }
  }
}
