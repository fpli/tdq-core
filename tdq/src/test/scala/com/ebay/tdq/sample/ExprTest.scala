package com.ebay.tdq.sample

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
trait Expr {
  val cacheKey: Option[String]

  def call(operands: Array[Any], cache: JHashMap[String, Any]): Any = {
    val v = eval(operands, cache)
    if (cacheKey.isDefined && cacheKey.get.nonEmpty) {
      cache.put(cacheKey.get, v)
    }
    v
  }

  def eval(operands: Array[Any], params: JHashMap[String, Any]): Any
}

case class GetStructField(name: String, cacheKey: Option[String] = None) extends Expr {
  override def eval(operands: Array[Any], cache: JHashMap[String, Any]): Any = {
    val o = cache.get(name)
    if (o != null) {
      return o
    }
    PropertyUtils.getProperty(cache.get("__RAW_EVENT"), name)
  }
}

case class GetRawEventExpr(cacheKey: Option[String] = Some("__RAW_EVENT")) extends Expr {
  override def eval(operands: Array[Any], params: JHashMap[String, Any]): Any =
    operands(0).asInstanceOf[RawEvent]
}

case class And(left: Expr, right: Expr) extends Expr {
  override val cacheKey: Option[String] = None

  override def eval(operands: Array[Any], params: JHashMap[String, Any]): Any =
    left.call(operands, params).asInstanceOf[Boolean] && right.call(operands, params).asInstanceOf[Boolean]
}

case class ExtractTag(subject: Expr, tag: String, cacheKey: Option[String] = None) extends Expr {
  override def eval(operands: Array[Any], params: JHashMap[String, Any]): Any = {
    TdqUDFs.extractTag(subject.call(operands, params).asInstanceOf[RawEvent], tag)
  }
}

case class Length(child: Expr, cacheKey: Option[String] = None) extends Expr {
  override def eval(operands: Array[Any], params: JHashMap[String, Any]): Any = {
    StringUtils.length(child.call(operands, params).asInstanceOf[String])
  }
}

object SqlParserImpl {

  def main(args: Array[String]): Unit = {
    val siteId: String = "1"
    val item: String = "123"
    val tDuration: String = "40"
    val pageId: String = "1702898"
    val contentLength: String = "55"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength(contentLength)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", tDuration)
    rawEvent.getSojA.put("itm", item)

    // context
    val params = new JHashMap[String, Any]()
    params.put("__RAW_EVENT", rawEvent)

    // sql expr              :   length(TAG_EXTRACT(__RAW_EVENT, 'p'))
    val logicalPlan = Length(ExtractTag(GetRawEventExpr(), "p", cacheKey = Some("page_id")), cacheKey = Some("len"))

    // physical function     :   StringUtils.length(TdqUDFs.extractTag(e, "p"))
    val v = logicalPlan.call(Array(rawEvent), params)
    println(v)
    println(TdqRawEventProcessFunction.mapToString(params))

    // expressions => deps
    //      SqlNumericLiteral,SqlCharStringLiteral => constant
    //      SqlIdentifier => get from map
    // aggression  => pre agg map
    //      TdqMetric add middle aggr
    // filter      => expressions
    //      use expressions

    /*
      LogicalPlan lp
      TdqMetric m = new TdqMetric(lp.metricKey, rawEvent.getEventTimestamp());
      m.setWindows
      Map<String, Object> params    = new HashMap<>();
      params.put("__RAW_EVENT", rawEvent);
        if(lp.filter(params)) {
        lp.run(params, m);
        collector.collect(m);
      }
     */
  }
}
