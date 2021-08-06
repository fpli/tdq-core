package com.ebay.tdq

import java.util.{HashMap => JHashMap}

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{TdqEvent, TdqMetric}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.planner.LkpManagerTest
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.{BeforeClass, Test}

object SOJlibUDFTest {
  @BeforeClass
  def setup(): Unit = {
    LkpManagerTest.init()
  }
}

class SOJlibUDFTest {
  @Test
  def test_soj_nvl(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("udid", "1")
    test("case when length(p2)>0 then 1 else 0 end", "soj_nvl('udid')", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })

    test("case when length(p2)>0 then 1 else 0 end", "soj_nvl('udid_test')", soj, metric => {
      assert(metric.getValues.get("p1") == 0)
    })
  }

  @Test
  def test_is_bbwoa_page(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("p", "2545343")
    test("case when IS_BBWOA_PAGE_WITH_ITM(p2) then 1 else 0 end", "cast(soj_nvl('p') AS INTEGER)", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
    })

    soj.put("p", "111111111")
    test("case when IS_BBWOA_PAGE_WITH_ITM(p2) then 1 else 0 end", "cast(soj_nvl('p') AS INTEGER)", soj, metric => {
      assert(metric.getValues.get("p1") == 0)
    })
  }

  def test(expr1: String, expr2: String, soj: JHashMap[String, String], assertFunction: TdqMetric => Unit, eventTime: Long = 3829847994095000L): Unit = {
    test0(expr1, expr2, soj, assertFunction, _ => {}, eventTime)
  }

  def test0(expr1: String, expr2: String, soj: JHashMap[String, String], assertFunction: TdqMetric => Unit, rawEventFunction: RawEvent => Unit, eventTime: Long = 3829847994095000L): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(config.getRules.get(0).getProfilers.get(0), window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString), new TdqEnv(), null)
    val plan = parser.parsePlan()
    println(plan)
    val rawEvent = new RawEvent
    rawEvent.setClientData(new ClientData)
    rawEvent.getClientData.setContentLength("55")
    rawEvent.getClientData.setRemoteIP("100.10.1.1")
    rawEvent.setEventTimestamp(eventTime)
    rawEvent.setSojA(new JHashMap[String, String])
    rawEvent.setSojK(new JHashMap[String, String])
    rawEvent.setSojC(new JHashMap[String, String])
    rawEvent.getSojA.putAll(soj)
    rawEventFunction(rawEvent)

    val metric = plan.process(new TdqEvent(rawEvent))
    assert(metric != null)
    assertFunction.apply(metric)
  }

  def getTdqConfig(expr1: String, expr2: String): TdqConfig = {
    val json =
      s"""
         |{
         |  "id": "1",
         |  "rules": [
         |    {
         |      "name": "rule_1",
         |      "type": "realtime.rheos.profiler",
         |      "config": {
         |        "window": "1min"
         |      },
         |      "profilers": [
         |        {
         |          "metric-name": "test1",
         |          "expr": "p1",
         |          "dimensions": ["p2"],
         |          "transformations": [
                      {"alias": "p1", "expr": "sum($expr1)"},
         |            {"alias": "p2", "expr": "$expr2"}
         |          ]
         |        }
         |      ]
         |    }
         |  ]
         |}
         |""".stripMargin
    JsonUtils.parseObject(json, classOf[TdqConfig])
  }

  @Test
  def test_soj_page_family(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("p", "5780")
    test("case when length(p2)>0 then 1 else 0 end", "soj_page_family(cast(soj_nvl('p') AS INTEGER))", soj, metric => {
      assert(metric.getValues.get("p1") == 1)
      assert(metric.getTags.get("p2").toString.toUpperCase().equals("GR"))
    })

    soj.put("p", "578000000")
    test("case when length(p2)>0 then 1 else 0 end", "soj_page_family(cast(soj_nvl('p') AS INTEGER))", soj, metric => {
      assert(metric.getTags.get("p2") == "Others")
    })
  }

  @Test
  def test_ebay_ip(): Unit = {
    val soj = new JHashMap[String, String]
    test0("case when clientData.remoteIP like '10.%' then 1 else 0 end", "clientData.remoteIP", soj, metric => {
      println(metric)
    }, rawEvent => {
      rawEvent.getClientData.setRemoteIP("10.10.1.1")
    })
  }

  @Test
  def test_timestamp(): Unit = {
    val soj = new JHashMap[String, String]
    test(s"case when p2 is not null then 1 else 0 end", "unix_timestamp(event_timestamp)", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
    })

    test(s"case when p2 is not null then 1 else 0 end", "unix_timestamp(event_timestamp)", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
    })
  }

  /*
    {
      "sojA": {
        "nqc": "AA**",
        "ssc": "8582420015",
        "flgs": "QIBxIKAAAAAAAAIAYQAEABAAgIAAgIAAAABwAAABAAAEAAAAAAAAEA**",
        "!wtballqs": "1001--9.508110044003457E-4|1007--0.0019347829885912248|1035--0.007850870506054994",
        "obfs_sid_uid_same": "false",
        "nlpp": "10",
        "itpnactp": "131090",
        "nqt": "AA**",
        "swcenabled": "true",
        "noep": "5",
        "iwsv": "0",
        "vibisdm": "1600X1600",
        "!vimc": "1%5E147%5E101224%5E146%5ERNRY,1%5E171%5E101196%5E169%5ERNRY",
        "!_OBFS_SELLERID": "1358478065",
        "mtpvi": "0",
        "!_wtbss": "1001|1007",
        "bdrs": "0",
        "OBFS_EP_STATE": "NOT_IN_EXPERIMENT",
        "fbscore": "46781",
        "swccount": "1",
        "!_callingpageid": "2047675",
        "meta": "131090",
        "slr": "1358478065",
        "OBFS_STATUS": "NOT_REQUIRED",
        "curprice": "72.43",
        "attrct": "21",
        "virvwcnt": "0",
        "rq": "802667aec4a20fec",
        "bc": "0",
        "visbetyp": "2",
        "!cat1": "179680",
        "l1": "14770",
        "l2": "179680",
        "!_itmhs": "1035|940|1007|945|947|1001",
        "qtys": "0",
        "itmcond": "1000",
        "!_OBFS_BUYER_VIEWING_SITEID": "77",
        "bs": "77",
        "OBFS_ITEMID": "373461953623",
        "obfs_desc_has_contactInfo": "false",
        "vibisb": "1600",
        "vibisd": "1600X1600",
        "!_epec": "7,6,7,6",
        "qtya": "8",
        "st": "9",
        "c": "1",
        "vibisbm": "1600",
        "g": "643d10741790ad908d607eafffe283dd",
        "h": "74",
        "leaf": "179680",
        "n": "643d10741790ad908d607eafffe283dd",
        "!_OBFS_PHONE_COUNT": "0",
        "p": "2047675",
        "fdp": "99",
        "t": "77",
        "nofp": "5",
        "rpdur": "1",
        "tr": "254405",
        "dc": "1",
        "visplt": "100562%2C100567%2C100568%2C100938%2C100727%2C100565%2C100916%2C100917%2C100918%2C100919%2C",
        "nozp": "5",
        "!ampid": "3P_IMP",
        "bsnippets": "true",
        "sdes": "1",
        "!_OBFS_LINK_COUNT": "0",
        "uc": "77",
        "mbsa": "500",
        "uaid": "643d10741790ad908d607eafffe283dcS0",
        "bot_provider": "%7B%22providers%22%3A%7B%22AK%22%3A%7B%22headers%22%3A%7B%22es%22%3A%22georegion%3D246%2Ccountry_code%3DUS%2Cregion_code%3DCA%2Ccity%3DSANJOSE%2Cdma%3D807%2Cpmsa%3D7400%2Cmsa%3D7362%2Careacode%3D408%2Ccounty%3DSANTACLARA%2Cfips%3D06085%2Clat%3D37.3435%2Clong%3D-121.8887%2Ctimezone%3DPST%2Czip%3D95101%2B95103%2B95106%2B95108-95113%2B95115-95136%2B95138-95139%2B95141%2B95148%2B95150-95161%2B95164%2B95170%2B95172-95173%2B95190-95194%2B95196%2Ccontinent%3DNA%2Cthroughput%3Dvhigh%2Cbw%3D5000%2Casnum%3D15169%2Clocation_id%3D0%22%7D%7D%7D%7D",
        "ul": "de-DE",
        "!oed": "0",
        "pymntVersion": "2",
        "ec": "1",
        "itm": "373461953623",
        "promol": "0",
        "eprlogid": "t6ulcpjqcj9%253Fjqpsobtlrbn%2528q%257Fr3k*w%2560ut351%253E-179643d1077-0x177",
        "iver": "2601218830024",
        "!_OBFS_BUYERID": "0",
        "viqtyremain": "8",
        "fistime": "17",
        "es": "77",
        "vimr": "101196%2C101224",
        "itmtitle": "Radar+255%2F40R19+100Y+Profil%3A+DIMAX+R8+PLUS+%2F+Sommerreifen",
        "cflgs": "wA**",
        "pymntMethods": "PPL%7CGGP%7CVSA%7CMSC%7CAMX%7CDDB%7CSOFO",
        "gitCommitId": "98fd39e0ad38fdc1e067a6acc67410b4dd12b2de",
        "vwc": "0",
        "epcalenv": "",
        "!_wtbaqs": "1001|1007|1035",
        "shipsiteid": "77",
        "virvwavg": "0.0",
        "obfs_listing_is_eligible": "true",
        "vibis": "300",
        "nw": "0",
        "OBFS_NON_OBFUSCATION_REASON": "SELLER_WHITE_LIST",
        "!notes": "0",
        "ppfoid": "0",
        "pagename": "ViewItemPageRaptor",
        "IS_TIRE_INSTALLATION_STATUS": "ineligible",
        "!_wtbqs": "1001|1007",
        "!cmplsvs": "77|77",
        "rpg": "2047675",
        "custVLSEligibility": "false",
        "fimbsa": "500",
        "iimp": "10",
        "swcembg": "true",
        "cp_usd": "88.08575",
        "srv": "0",
        "!vifit": "4",
        "!_itmhss": "1001|1007",
        "pn": "2",
        "qtrmn": "8",
        "!_OBFS_EMAIL_COUNT": "0"
      },
      "sojK": {
        "ciid": "PRB0CNY*"
      },
      "sojC": {},
      "clientData": {
        "forwardFor": "66.249.79.229,184.25.254.69,66.135.196.44,10.192.142.87",
        "script": "/itm",
        "server": "www.ebay.de",
        "colo": "LVS",
        "agent": "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
        "remoteIP": "66.249.79.229",
        "corrId": "802667aec4a20fec",
        "contentLength": "0",
        "nodeId": "094ed03e0a0582bd",
        "requestGuid": "179643d1-06f0-ad90-8d64-e54dffdbb52a",
        "urlQueryString": "/itm/373461953623?chn=ps&mkevt=1&mkcid=28",
        "referrer": "",
        "rlogid": "t6ulcpjqcj9%3Fjqpsobtlrbn%28q%7Fr3k*w%60ut351%3E-179643d1077-0x177",
        "encoding": "gzip",
        "tname": "ViewItemPageRaptor",
        "tpool": "r1viewitem",
        "ttype": "URL",
        "tmachine": "10.217.8.214",
        "tstamp": "1620884394095",
        "tstatus": "0",
        "tduration": "339",
        "tpayload": "corr_id_%3D802667aec4a20fec%26node_id%3D094ed03e0a0582bd%26REQUEST_GUID%3D179643d1-06f0-ad90-8d64-e54dffdbb52a%26logid%3Dt6ulcpjqcj9%253Fjqpsobtlrbn%2528q%257Fr3k%2Aw%2560ut351%253E-179643d1077-0x177%26cal_mod%3Dfalse"
      },
      "ingestTime": 1620884394452,
      "eventTimestamp": 3829847994095000
    }
   */
  @Test
  def test_soj_parse_rlogid(): Unit = {
    val soj = new JHashMap[String, String]
    soj.put("eprlogid", "t6ulcpjqcj9%253Fjqpsobtlrbn%2528q%257Fr3k*w%2560ut351%253E-179643d1077-0x177")
    test(s"case when p2=0 then 0 else 1 end", "unix_timestamp(event_timestamp) - unix_timestamp(to_timestamp(soj_parse_rlogid(soj_nvl('eprlogid'), 'timestamp')))", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 0)
    })

    test(s"case when p2=0 then 0 else 1 end", "unix_timestamp(event_timestamp) - unix_timestamp(to_timestamp(soj_parse_rlogid(soj_nvl('eprlogid'), 'timestamp')))", soj, metric => {
      println(metric)
      assert(metric.getValues.get("p1") == 1)
      assert(metric.getTags.get("p2") == 5)
    }, eventTime = 3829847994095000L + 5000000)
  }
}