package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.Test

import scala.collection.mutable

object PerformanceDebug {
  val m = new mutable.HashMap[String, Int]()

  def add(k: String): Unit = {
    m.put (k, m.getOrElse(k, 0) + 1)
  }

  override def toString: String = {
    m.filter(_._2 > 0).toString
  }
}
class PerformanceTest {
  @Test
  def testExprCallCache(): Unit = {
    val json =
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
        |        {"alias": "soj_tag_p", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('p')"}}},
        |        {"alias": "soj_tag_u", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('u')"}}},
        |        {"alias": "soj_tag_itm", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('itm|itmid|itm_id|itmlist|litm')"}}},
        |        {"alias": "soj_tag_dn", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('dn')"}}},
        |        {"alias": "soj_tag_mav", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('mav')"}}},
        |        {"alias": "soj_tag_mos", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('mos')"}}},
        |        {"alias": "soj_tag_osv", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('osv')"}}},
        |        {"alias": "soj_tag_es", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('es')"}}},
        |        {"alias": "soj_tag_t", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('t')"}}},
        |        {"alias": "soj_tag_app", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('app')"}}},
        |        {"alias": "soj_tag_duration", "expression": {"operator": "UDF", "config": {"text": " SOJ_NVL('TDuration')"}}},
        |        {"alias": "total_cnt", "expression": {"operator": "Count", "config": {"arg0": "1.0"}}},
        |        {"alias": "domain", "expression": {"operator": "UDF", "config": {"text": " SOJ_PAGE_FAMILY(CAST(soj_tag_p AS INTEGER))"}}},
        |        {"alias": "site_id", "expression": {"operator": "UDF", "config": {"text": "CAST(soj_tag_t AS INTEGER)"}}},
        |        {"alias": "app", "expression": {"operator": "UDF", "config": {"text": "soj_tag_app"}}},
        |        {"alias": "page_id", "expression": {"operator": "UDF", "config": {"text": "CAST(soj_tag_p AS INTEGER)"}}},
        |        {"alias": "duration_sum", "expression": {"operator": "SUM", "config": {"arg0": "CAST(soj_tag_duration AS DOUBLE)"}}},
        |        {"alias": "gm_t_usr_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |
        |        {"alias": "gm_t_itm_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH( REGEXP_EXTRACT(soj_tag_itm, '^(\\d+(%2C)?)+$', 1) ) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_dn_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_mav_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_mos_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_osv_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "ep_site_icr_cnt", "expression": {"operator": "Sum", "config": {"arg0": "case when length(soj_tag_t)>0 and length(soj_tag_es)>0 and soj_tag_t = soj_tag_es then 0.0 else 1.0 end"}}},
        |
        |        {"alias": "gm_t_usr_cnt0", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt1", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt2", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt3", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt4", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt5", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt6", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt7", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt8", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt9", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |
        |        {"alias": "gm_t_usr_cnt10", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt11", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt12", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt13", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt14", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt15", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt16", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt17", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt18", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt19", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |
        |        {"alias": "gm_t_usr_cnt20", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt21", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt22", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt23", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt24", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt25", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt26", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt27", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt28", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt29", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |
        |        {"alias": "gm_t_usr_cnt30", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt31", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt32", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt33", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt34", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt35", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt36", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt37", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt38", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt39", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |
        |        {"alias": "gm_t_usr_cnt40", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt41", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt42", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt43", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt44", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt45", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt46", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt47", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt48", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}},
        |        {"alias": "gm_t_usr_cnt49", "expression": {"operator": "Sum", "config": {"arg0": "case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end"}}}
        |      ]
        |    }
        |  ]
        |  }]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString)
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "1"
    val item: String = "123"
    val pageId: String = "1702898"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength("55")
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("TDuration", "155")
    rawEvent.getSojA.put("itm", item)

    val metric = plan.process(rawEvent)
    assert(metric != null)
    println(metric.getExprMap)

  }
}
