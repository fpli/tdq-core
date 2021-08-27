package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{TdqEvent, RawEvent => TdqRawEvent}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.planner.LkpManagerTest
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.{BeforeClass, Test}

object PerformanceTest {
  @BeforeClass
  def setup(): Unit = {
    LkpManagerTest.init()
  }

  def main(args: Array[String]): Unit = {
    val raw = JsonUtils.parseObject(
      """
        |{
        |  "sojA": {
        |    "saucxgdpry": "false",
        |    "flgs": "AA**",
        |    "ac": "",
        |    "efam": "SAND",
        |    "saucxgdprct": "false",
        |    "sameid": "553fdf2676434ee79f1734396858f3e6",
        |    "saty": "1",
        |    "saebaypid": "100562",
        |    "g": "643d11771790a368a886f55bffe97ca9",
        |    "sapcxkw": "Apple+Care+Protection+Plan+For+Apple+Display+-+PC+%2B+Mac+MA517LL%2FA",
        |    "h": "77",
        |    "schemaversion": "3",
        |    "salv": "5",
        |    "ciid": "PRHBljc*",
        |    "p": "2367355",
        |    "sapcxcat": "58058%2C18793%2C182",
        |    "saiid": "818d4ee4-9fa4-4a67-a385-6fbe49ce69c4",
        |    "t": "15",
        |    "cflgs": "gA**",
        |    "samslid": "",
        |    "eactn": "AUCT",
        |    "pn": "2",
        |    "rq": "5d796a0415b9b360",
        |    "pagename": "SandPage"
        |  },
        |  "sojK": {
        |    "ciid": "PRHBljc*"
        |  },
        |  "sojC": {},
        |  "clientData": {
        |    "forwardFor": "10.198.144.70",
        |    "script": "sand",
        |    "server": "sand.stratus.ebay.com",
        |    "colo": "RNO",
        |    "agent": "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
        |    "remoteIP": "66.249.73.25",
        |    "corrId": "5d796a0415b9b360",
        |    "contentLength": "253",
        |    "nodeId": "e58de20d193a6862",
        |    "requestGuid": "179643d1-1bf0-ad39-6377-3e24fdc48564",
        |    "urlQueryString": "/itm/203008530552?chn=ps&mkevt=1&mkcid=28",
        |    "referrer": "",
        |    "rlogid": "t6pdhc9%3Ftilvgig%28q%3Ek%7Dt*w%60ut3542-179643d11bf-0x23a7",
        |    "encoding": "",
        |    "tname": "sand.v1",
        |    "tpool": "r1sand",
        |    "ttype": "",
        |    "tmachine": "10.211.150.55",
        |    "tstamp": "1620884394432",
        |    "tstatus": "",
        |    "tduration": "2",
        |    "tpayload": "corr_id_%3D5d796a0415b9b360%26node_id%3De58de20d193a6862%26REQUEST_GUID%3D179643d1-1bf0-ad39-6377-3e24fdc48564%26logid%3Dt6pdhc9%253Ftilvgig%2528q%253Ek%257Dt%2Aw%2560ut3542-179643d11bf-0x23a7"
        |  },
        |  "ingestTime": 1620884394460,
        |  "eventTimestamp": 3829847994432000
        |}
        |""".stripMargin, classOf[RawEvent])

    val tdqEvent = new TdqEvent(raw)

    """
      |forwardFor
      |script
      |server
      |TMachine
      |TStamp
      |TName
      |t
      |colo
      |pool
      |agent
      |remoteIP
      |TType
      |TPool
      |TStatus
      |corrId
      |contentLength
      |nodeId
      |requestGuid
      |urlQueryString
      |referrer
      |rlogid
      |acceptEncoding
      |TDuration
      |encoding
      |TPayload
      |""".stripMargin.split("\n").filter(_.nonEmpty).foreach(k => {

      //      println(PropertyUtils.getProperty(raw, s"clientData.$k"))
      println(s"tdqEvent clientData.$k=>${tdqEvent.get(s"clientData.$k")}")
    })

    val avro = TdqRawEvent.newBuilder()
      .setSojA(raw.getSojA)
      .setSojC(raw.getSojC)
      .setSojK(raw.getSojK)
      .setClientData(raw.getClientData.getMap)
      .setIngestTime(raw.getIngestTime)
      .setEventTimestamp(raw.getUnixEventTimestamp)
      .setSojTimestamp(raw.getEventTimestamp())
      .setProcessTimestamp(System.currentTimeMillis())
      .build()
    val tdqEvent2 = new TdqEvent(avro)

    """
      |forwardFor
      |script
      |server
      |TMachine
      |TStamp
      |TName
      |t
      |colo
      |pool
      |agent
      |remoteIP
      |TType
      |TPool
      |TStatus
      |corrId
      |contentLength
      |nodeId
      |requestGuid
      |urlQueryString
      |referrer
      |rlogid
      |acceptEncoding
      |TDuration
      |encoding
      |TPayload
      |""".stripMargin.split("\n").filter(_.nonEmpty).foreach(k => {

      //      println(PropertyUtils.getProperty(raw, s"clientData.$k"))
      println(s"tdqEvent2 clientData.$k=>${tdqEvent2.get(s"clientData.$k")}")
    })

    // this performance is good
    var s = System.currentTimeMillis()
    (0 to 1000000).foreach(_ => {
      tdqEvent.get("clientData.remoteIP")
    })
    println(s"hashmap=>${System.currentTimeMillis() - s}")

    s = System.currentTimeMillis()
    (0 to 1000000).foreach(_ => {
      tdqEvent2.get("clientData.remoteIP")
    })
    println(s"hashmap GenericRecord=>${System.currentTimeMillis() - s}")

    // refection exception will so bad
    //    s = System.currentTimeMillis()
    //    (0 to 1000000).foreach(_ => {
    //      try {
    //        PropertyUtils.getProperty(raw, "clientData.remoteIP1")
    //      } catch {
    //        case _: NoSuchMethodException =>
    //      }
    //    })
    //    println(System.currentTimeMillis() - s)

    //    s = System.currentTimeMillis()
    //    (0 to 1000000).foreach(_ => {
    //      PropertyUtils.getProperty(raw, s"clientData.remoteIP")
    //    })
    //    println(s"PropertyUtils=>${System.currentTimeMillis() - s}")
    //    s = System.currentTimeMillis()
    //
    //    // this is bad
    //    (0 to 1000000).foreach(_ => {
    //      SojUtils.getTagValueStr(raw, "remoteIP")
    //    })
    //    println(s"SojUtils.getTagValueStr=>${System.currentTimeMillis() - s}")
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
    val parser = new ProfilingSqlParser(config.getRules.get(0).getProfilers.get(0), window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString), new TdqEnv(), null)
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

    val metric = plan.process(new TdqEvent(rawEvent))
    assert(metric != null)
    println(metric.getValues)

  }
}


