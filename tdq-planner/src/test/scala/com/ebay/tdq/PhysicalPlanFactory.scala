package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.sojourner.common.util.SojTimestamp
import com.ebay.tdq.common.env.JdbcEnv
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.planner.LkpManagerTest
import com.ebay.tdq.rules.{PhysicalPlan, ProfilingSqlParser}
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.{BeforeClass, Test}

/**
 * @author juntzhang
 */
object PhysicalPlanFactory {
  @BeforeClass
  def setup(): Unit = {
    LkpManagerTest.init()
  }

  def getPhysicalPlan(json: String): PhysicalPlan = {
    val objectMapper = new ObjectMapper
    val config: TdqConfig = objectMapper.reader.forType(classOf[TdqConfig]).readValue(json)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    parser.parsePlan()
  }

}

class ProfilingSqlParserTest {

  @Test
  def testEventCapturePublishLatency(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_1",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "10s"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "event_capture_publish_latency",
        |          "comment": "Event Capture Publish Latency",
        |          "dimensions": ["page_id"],
        |          "expression": {"operator": "Expr", "config": {"text": "t_duration_sum"}},
        |          "filter": "page_id in (1702898, 1677718) and CAST(clientData.contentLength AS double) > 30.0",
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expression": {"operator": "UDF", "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}}
        |            },
        |            {
        |              "alias": "t_duration",
        |              "expression": {
        |                "operator": "Expr",
        |                "config": {"text": "CAST( SOJ_NVL('TDuration') AS DOUBLE)"}
        |              }
        |            },
        |            {
        |              "alias": "t_duration_sum",
        |              "expression": {"operator": "SUM", "config": {"arg0": "t_duration"}}
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
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

    var metric = plan.process(rawEvent)
    assert(metric != null)
    assert(metric.getValues.get("t_duration_sum") == 155d)

    rawEvent.getClientData.setContentLength("25")
    metric = plan.process(rawEvent)
    assert(metric == null)

    rawEvent.getClientData.setContentLength("35")
    rawEvent.getSojA.put("TDuration", "10")
    metric = plan.process(rawEvent)
    assert(metric.getValues.get("t_duration_sum") == 10d)

  }

  @Test
  def testMissingItemRate(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_3",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "2min"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "global_mandatory_tag_item_rate2",
        |          "comment": "Global Mandatory Tag - Item Rate",
        |          "dimensions": ["page_id"],
        |          "expression": {"operator": "Expr", "config": {"text": "itm_valid_cnt / itm_cnt"}},
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expression": {
        |                "operator": "UDF",
        |                "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}
        |              }
        |            },
        |            {
        |              "alias": "item",
        |              "expression": {
        |                "operator": "UDF",
        |                "config": {"text": "CAST( SOJ_NVL('itm|itmid|itm_id|itmlist|litm') AS LONG)"}
        |              }
        |            },
        |            {
        |              "alias": "itm_valid_ind",
        |              "expression": {
        |                "operator": "Expr",
        |                "config": {
        |                  "text": "case when item is not null then 1.0 else 0.0 end"
        |                }
        |              }
        |            },
        |            {
        |              "alias": "itm_cnt",
        |              "expression": {"operator": "Count", "config": {"arg0": "1.0"}}
        |            },
        |            {
        |              "alias": "itm_valid_cnt",
        |              "expression": {
        |                "operator": "Sum", "config": {"arg0": "itm_valid_ind"}
        |              }
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "0"
    val item: String = "123"
    val pageId: String = "1702898"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("itm", item)

    var metric = plan.process(rawEvent)
    assert(metric.getValues.get("itm_cnt") == 1)
    assert(metric.getValues.get("itm_valid_cnt") == 1)

    rawEvent.getSojA.put("itm", "123a")
    metric = plan.process(rawEvent)
    assert(metric.getValues.get("itm_cnt") == 1)
    assert(metric.getValues.get("itm_valid_cnt") == 0)

    rawEvent.getSojA.remove("itm")
    val s = System.currentTimeMillis()
    val s1 = System.nanoTime()

    (0 to 100000).foreach { _ =>
      rawEvent.getSojA.put("p", Math.abs(RawEventTest.getInt).toString)
      rawEvent.getSojA.put("itm", Math.abs(RawEventTest.getLong).toString)
      metric = plan.process(rawEvent)
    }

    println(s"100k process cast time ${System.currentTimeMillis() - s} ms")
    println(s"100k process cast time ${System.nanoTime() - s1} ns")
    //    assert(metric.getExprMap.get("itm_cnt") == 1)
    //    assert(metric.getExprMap.get("itm_valid_cnt") == 0)

  }

  @Test
  def testTransformationFilter(): Unit = {
    val json =
      """
        |{
        |  "id": "1", "name": "cfg_1", "rules": [
        |  {
        |    "name": "rule_1", "type": "realtime.rheos.profiler", "config": {"window": "5min"}, "profilers": [
        |    {
        |      "metric-name": "test",
        |      "expr": "total_cnt",
        |      "dimensions": ["page_id"],
        |      "transformations": [
        |        {"alias": "page_id",          "expr": "CAST(soj_tag_p AS INTEGER)",           "filter": "page_id in (2547208, 2483445)"},
        |        {"alias": "soj_tag_p",        "expr": "soj_nvl('p')"},
        |        {"alias": "total_cnt",        "expr": "count(1)"}
        |      ]
        |    }
        |  ]}
        |]}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "0"
    val item: String = "123"
    val pageId: String = "2547208"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("itm", item)

    var metric = plan.process(rawEvent)
    assert(metric.getTags.get("page_id") == 2547208)
    assert(metric.getValues.get("total_cnt") == 1)

    rawEvent.getSojA.put("p", "123")
    metric = plan.process(rawEvent)
    assert(metric.getTags.get("page_id") == null)
    assert(metric.getValues.get("total_cnt") == 1)
  }

  @Test
  def testQualificationAgeNQT(): Unit = {
    val json =
      """
        |{
        |  "id":    "20",
        |  "name":  "cfg_20",
        |  "rules": [
        |    {
        |      "name":      "rule_20",
        |      "type":      "realtime.rheos.profiler",
        |      "config":    {
        |        "window": "5min"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name":     "qualification_age_nqt_5min",
        |          "expr":            "qualified_events_cnt / total_cnt",
        |          "dimensions":      ["expt_flow_type", "channel", "qual_age_target"],
        |          "filter": "qual_timestamp is not null",
        |          "transformations": [
        |            {"alias": "soj_ec", "expr": "soj_nvl('ec')"},
        |            {"alias": "soj_eprlogid", "expr": "soj_nvl('eprlogid')"},
        |            {"alias": "tpool", "expr": "soj_nvl('TPool')"},
        |            {
        |              "alias": "channel",
        |              "expr":  "case when soj_ec = '1' then 'Web' when soj_ec = '2' then 'Mobile Web' when soj_ec = '4' then 'Android' when soj_ec = '5' then 'iOS' when soj_ec = '6' then 'Email' else 'Unknown' end"
        |            },
        |            {
        |              "alias": "qual_timestamp",
        |              "expr":  "to_timestamp(soj_parse_rlogid(soj_eprlogid, 'timestamp'))"
        |            },
        |            {
        |              "alias": "qual_age",
        |              "expr":  "unix_timestamp(event_timestamp) - unix_timestamp(qual_timestamp)"
        |            },
        |            {
        |              "alias": "pool_type",
        |              "expr":  "case when tpool in ('r1rover', 'r1pulsgwy', 'r1edgetrksvc') THEN 'T' else 'D' end"
        |            },
        |            {
        |              "alias": "expt_flow_type",
        |              "expr":  "case when pool_type = 'T' and soj_ec in ('4', '5') then 'Native Client Side' when soj_ec in ('4', '5') then 'Native Server Side' else 'Native web, MWeb, DWeb' end"
        |            },
        |            {
        |              "alias": "qual_age_target",
        |              "expr":  "case when expt_flow_type in ('Native Client Side') then '< 12 hours' else '< 10 second' end"
        |            },
        |            {"alias": "total_cnt", "expr":  "count(1)"},
        |            {
        |              "alias": "qualified_events_cnt",
        |              "expr":  "sum( case  when expt_flow_type in ('Native Client Side') and qual_age <= 3600 * 12 then 1  when expt_flow_type in ('Native web, MWeb, DWeb', 'Native Server Side') and qual_age <= 10 then 1  else 0 end)"
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "0"
    val item: String = "123"
    val pageId: String = "2547208"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.setEventTimestamp(SojTimestamp.getUnixTimestampToSojTimestamp(SojTimestamp.getSojTimestampToUnixTimestamp(3829847994095000L) + 1000 * 11))
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("eprlogid", "t6ulcpjqcj9%253Fjqpsobtlrbn%2528q%257Fr3k*w%2560ut351%253E-179643d1077-0x177")
    rawEvent.getSojA.put("ec", "1")
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("itm", item)

    var metric = plan.process(rawEvent)
    println(metric)
    assert(metric.getValues.get("total_cnt") == 1)
    assert(metric.getValues.get("qualified_events_cnt") == 0)

    rawEvent.setEventTimestamp(SojTimestamp.getUnixTimestampToSojTimestamp(SojTimestamp.getSojTimestampToUnixTimestamp(3829847994095000L) + 1000 * 5))
    metric = plan.process(rawEvent)
    println(metric)
    assert(metric.getValues.get("total_cnt") == 1)
    assert(metric.getValues.get("qualified_events_cnt") == 1)
  }

  @Test
  def testSearchTagRate(): Unit = {
    val json =
      """
        |{
        |  "id": "30", "name": "cfg_30", "rules": [
        |  {
        |    "name": "30", "type": "realtime.rheos.profiler", "config": {"window": "5min"}, "profilers": [
        |    {
        |      "metric-name": "common_metric",
        |      "expr": "total_cnt",
        |      "dimensions": ["domain", "site_id", "app", "page_id"],
        |      "transformations": [
        |
        |        {"alias": "soj_tag_p",           "expr": "soj_nvl('p')"},
        |        {"alias": "soj_tag_u",           "expr": "soj_nvl('u')"},
        |        {"alias": "soj_tag_t",           "expr": "soj_nvl('t')"},
        |        {"alias": "soj_tag_itm",         "expr": "soj_nvl('itm|itmid|itm_id|itmlist|litm')"},
        |        {"alias": "soj_tag_mav",         "expr": "soj_nvl('mav')"},
        |        {"alias": "soj_tag_dn",          "expr": "soj_nvl('dn')"},
        |        {"alias": "soj_tag_mos",         "expr": "soj_nvl('mos')"},
        |        {"alias": "soj_tag_osv",         "expr": "soj_nvl('osv')"},
        |        {"alias": "soj_tag_es",          "expr": "soj_nvl('es')"},
        |        {"alias": "soj_tag_app",         "expr": "soj_nvl('app')"},
        |        {"alias": "soj_tag_duration",    "expr": "soj_nvl('TDuration')"},
        |        {"alias": "soj_tag_cpnip",       "expr": "soj_nvl('cpnip')"},
        |        {"alias": "soj_tag_icpp",        "expr": "soj_nvl('icpp')"},
        |        {"alias": "soj_tag_prof",        "expr": "soj_nvl('prof')"},
        |        {"alias": "soj_tag_clktrack",    "expr": "soj_nvl('!clktrack')"},
        |        {"alias": "soj_tag_eactn",       "expr": "soj_nvl('eactn')"},
        |
        |        {"alias": "app",                 "expr": "soj_tag_app",                                                      "filter": "app in ('2571','1462','2878')"},
        |        {"alias": "page_id",             "expr": "CAST(soj_tag_p AS INTEGER)",                                       "filter": "page_id in (2547208, 2483445)"},
        |        {"alias": "domain",              "expr": "soj_page_family(CAST(soj_tag_p AS INTEGER))"},
        |        {"alias": "site_id",             "expr": "CAST(soj_tag_t AS INTEGER)"},
        |        {"alias": "usr_id",              "expr": "CAST(soj_tag_u AS LONG)"},
        |
        |        {"alias": "total_cnt",           "expr": "count(1)"},
        |
        |        {"alias": "duration_sum",        "expr": "sum(CAST(soj_tag_duration AS DOUBLE))"},
        |
        |        {"alias": "gmt_total_cnt",       "expr": "count(1)"    ,                                                  "filter": "domain in ('ASQ','BID','BIDFLOW','BIN','BINFLOW','CART','OFFER','UNWTCH','VI','WTCH','XO')"  },
        |        {"alias": "gmt_usr_cnt",         "expr": "sum(case when LENGTH(soj_tag_u) > 0 then 1.0 else 0.0 end)"  ,  "filter": "domain in ('ASQ','BID','BIDFLOW','BIN','BINFLOW','CART','OFFER','UNWTCH','VI','WTCH','XO')"  },
        |        {"alias": "gmt_itm_cnt",         "expr": "sum(case when LENGTH(soj_tag_itm) > 0 then 1.0 else 0.0 end)",  "filter": "domain in ('ASQ','BID','BIDFLOW','BIN','BINFLOW','CART','OFFER','UNWTCH','VI','WTCH','XO')"  },
        |
        |        {"alias": "nt_dn_cnt",           "expr": "sum(case when LENGTH(soj_tag_dn) > 0 then 1.0 else 0.0 end)"  },
        |        {"alias": "nt_mav_cnt",          "expr": "sum(case when LENGTH(soj_tag_mav) > 0 then 1.0 else 0.0 end)" },
        |        {"alias": "nt_mos_cnt",          "expr": "sum(case when LENGTH(soj_tag_mos) > 0 then 1.0 else 0.0 end)" },
        |        {"alias": "nt_osv_cnt",          "expr": "sum(case when LENGTH(soj_tag_osv) > 0 then 1.0 else 0.0 end)" },
        |
        |        {"alias": "ep_site_incon_cnt",   "expr": "sum(case when length(soj_tag_t)>0 and length(soj_tag_es)>0 and soj_tag_t = soj_tag_es then 0 else 1 end)"},
        |
        |        {"alias": "ut_err_cnt",          "expr": "sum(case when ((soj_tag_u similar to '\\d+' and (usr_id <= 0 or usr_id > 9999999999999999)) or soj_tag_u not similar to '\\d+') then 1 else 0 end)"},
        |
        |        {"alias": "st_total_cnt",        "expr": "count(1)"                                                      , "filter":"page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and soj_tag_eactn is not null)"   },
        |        {"alias": "st_cpnip_cnt",        "expr": "sum(case when LENGTH(soj_tag_cpnip) > 0 then 1 else 0 end)"    , "filter":"page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and soj_tag_eactn is not null)"   },
        |        {"alias": "st_icpp_cnt",         "expr": "sum(case when LENGTH(soj_tag_icpp) > 0 then 1 else 0 end)"     , "filter":"page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and soj_tag_eactn is not null)"   },
        |        {"alias": "st_prof_cnt",         "expr": "sum(case when LENGTH(soj_tag_prof) > 0 then 1 else 0 end)"     , "filter":"page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and soj_tag_eactn is not null)"   },
        |        {"alias": "st_clktrack_cnt",     "expr": "sum(case when LENGTH(soj_tag_clktrack) > 0 then 1 else 0 end)" , "filter":"page_id in (2047936,2054032,2053742,2045573) or (page_id in (2351460,2381081) and soj_tag_eactn is not null)"   }
        |      ]
        |    }
        |  ]
        |  }]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new JdbcEnv()
    )
    val plan = parser.parsePlan()
    println(plan)

    val siteId: String = "0"
    val item: String = "123"
    val pageId: String = "1702898"
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.setEventTimestamp(System.currentTimeMillis)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.put("p", pageId)
    rawEvent.getSojA.put("t", siteId)
    rawEvent.getSojA.put("itm", item)

    val metric = plan.process(rawEvent)
    println(metric)

  }


}
