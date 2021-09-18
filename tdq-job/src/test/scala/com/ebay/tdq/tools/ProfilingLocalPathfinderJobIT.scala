package com.ebay.tdq.tools

import com.ebay.sojourner.common.model.RawEvent
import com.ebay.tdq.ProfilingJobIT
import com.ebay.tdq.common.model.TdqEvent
import com.ebay.tdq.jobs.ProfilingJob
import com.ebay.tdq.sources.MemorySourceFactory
import com.ebay.tdq.utils._
import org.apache.commons.io.IOUtils
import org.apache.flink.streaming.api.functions.source.SourceFunction

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
case class ProfilingLocalPathfinderJobIT(profiler: String) extends ProfilingJob {

  import ProfilingJobIT.setupDB

  val config: String =
    s"""
       |{
       |  "id": "1",
       |  "name": "tdq.dev.pathfinder",
       |  "sources": [
       |    {
       |      "name": "test",
       |      "type": "realtime.memory",
       |      "config": {
       |        "schema-subject": "behavior.pathfinder.sojevent.schema",
       |        "rheos-services-urls": "https://rheos-services.qa.ebay.com"
       |      }
       |    }
       |  ],
       |  "rules": [
       |    {
       |      "name": "ido",
       |      "type": "realtime.rheos.profiler",
       |      "config": {
       |        "window": "5min"
       |      },
       |      "profilers": [
       |        $profiler
       |      ]
       |    }
       |  ],
       |  "sinks": [
       |    {
       |      "name": "console",
       |      "type": "realtime.console",
       |      "config": {
       |        "sub-type": "normal-metric",
       |        "std-name": "normal"
       |      }
       |    }
       |  ],
       |  "env": {
       |    "config": {
       |      "flink.app.window.metric-1st-aggr": "5s",
       |      "flink.app.local-aggr.queue-size": 0,
       |      "flink.app.local-aggr.flush-timeout": "5s",
       |      "flink.app.local-aggr.output-partitions": 2,
       |      "flink.app.parallelism.metric-1st-aggr": 2,
       |      "flink.app.parallelism.metric-2nd-aggr": 2
       |    }
       |  }
       |}
       |""".stripMargin

  override def setup(args: Array[String]): Unit = {
    setupDB(config)
    super.setup(args)
    MemorySourceFactory.setSourceFunction(new SourceFunction[TdqEvent]() {
      @throws[InterruptedException]
      override def run(ctx: SourceFunction.SourceContext[TdqEvent]): Unit = {
        // genTdqEvent(ctx)
        genRawEvent(ctx)
      }

      override def cancel(): Unit = {}
    })
  }

  def genTdqEvent(ctx: SourceFunction.SourceContext[TdqEvent]): Unit = {
    val json =
      """
        |{"type":"GENERIC_EVENT","data":{"appVersion":null,"rdt":0,"remoteIP":"23.242.253.54","rlogid":"t6pwehq%60%3C%3Dqkitqfiuf%2801%60ta*w%60ut3542-17b812cef53-0x161","browserFamily":"Chrome","botFlags":[],"webServer":"www.ebay.com","clickId":"725","pageName":"SellerOtherItemRaptor","sessionStartDt":3838917561314000,"cobrand":"0","clientIP":"23.242.253.54","appId":null,"browserVersion":"92.0.4515.159","requestCorrelationId":"177625d0271e345a","deviceType":"Windows 7 - NT 6.1 ","eventCaptureTime":0,"seqNum":"2508","pageFamily":"GR","deviceFamily":"Desktop","pageId":2046732,"version":3,"ciid":"LO9OVXk*","itemId":null,"forwardedFor":null,"guid":"9968192c1770a45e242080b3ffbaa651","eventFamily":null,"clientData":{"Script":"/sch/patti1578/m.html","Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36","Server":"www.ebay.com","RemoteIP":"23.242.253.54","corrId":"177625d0271e345a","Referer":"https://www.ebay.com/usr/patti1578?_trksid=p2047675.l2559","TType":"URL","Encoding":"gzip","TPayload":"corr_id_%3D177625d0271e345a%26node_id%3D00362d4efbbac9c0%26REQUEST_GUID%3D17b812ce-f4b0-a6e5-5792-96f1ff569d78%26logid%3Dt6pwehq%2560%253C%253Dqkitqfiuf%252801%2560ta%2Aw%2560ut3542-17b812cef53-0x161%26cal_mod%3Dfalse","TStamp":"23:36:50.89","TPool":"r1srcore","TStatus":"0","TDuration":"444","ContentLength":"0","TName":"SellerOtherItemRaptor","nodeId":"00362d4efbbac9c0","ForwardedFor":"23.242.253.54,23.55.36.84,66.135.196.45,10.192.140.119","TMachine":"10.110.85.121"},"enrichedOsVersion":"NT 6.1","sojDataDt":3838838400000000,"siid":null,"agentInfo":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36","rv":true,"bot":0,"reservedForFuture":0,"flags":"AAAAAAAEAAACCCAAAAAAAAAAAAAAAAAAAAAgAAAMABAAAAAQAAAIAAAAQAAAAAAAAAAAAAAQAAAgAAAAAGA*","event_timestamp":1629959810891000,"eventAction":null,"cookies":"null","cguid":"34f2527117a0acfbbc603e07fe6befa2","sid":"p2545226.m2533.l4587","icfBinary":0,"regu":0,"osVersion":null,"sqr":null,"rheosHeader":{"eventId":null,"eventSentTimestamp":1629959811551,"schemaId":6652,"producerId":"sojourner","eventCreateTimestamp":1629959811551},"iframe":false,"event_time_millis":1629959810891,"eventTimestamp":1629959810891,"osFamily":"Windows 7","sessionSkey":38389175609660,"sessionId":"9968192c1770a45e242080b3ffbaa6510000017b80d3ac10","staticPageType":0,"ingestTime":1629959811366,"userId":"1454500365","urlQueryString":"/sch/patti1578/m.html?_nkw=&_armrs=&_ipg=&_from=","sourceImprId":560022151468,"refererHash":"2074659235","applicationPayload":{"nqc":"AAIAAAAAAAAAAAAAAAADAgBAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAABAAAgAAAAAAAAAAAQAAAAAAIAAAAACAAAAAICAAAIGgCAAAAAAAAAgAQACA","flgs":"AAAAAAAEAAACCCAAAAAAAAAAAAAAAAAAAAAgAAAMABAAAAAQAAAIAAAAQAAAAAAAAAAAAAAQAAAgAAAAAGA*","ssb":"1","ccd":"601%5E1629959810901%5EamFlLWZpenp5%5E0%5E3%7C2%7C5%7C4%7C7%7C11%5E2%5E4%5E4%5E3%5E12%5E12%5E2%5E1%5E1%5E0%5E1%5E0%5E1%5E6442459075","nabi":"0","!cusp":"bisnf%3Abm","naa":"1","nqt":"AAIAAAAAAAAAAAAAAAADAgBAAAAAAAAAAAAAAAAAACAAAAAAAAAAAAIAAAAAAAAAAAAAAAAAABAAAgAAAAAAAAAAAQAAAAAAIAAACACAAAAAICAAAIGgCAAAAAAAAAgAQACA","fixedprices":"2","sHit":"110","directFromBrandbadgecnt":"0","nai":"46","clientIP":"23.242.253.54","rheosUpstreamSendTs":"1629959811613","nadsi":"0","Agent":"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36","!ttp_position":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","!hotcnt":"pq%3A0,pw%3A0,lq%3A0,sa%3A0,wp%3A0","ifrm":"false","!surfacetrk":"nl%3A0,sme%3A0,msku%3A0,sl%3A0,ebp%3A0,prp%3A0,prw%3A0,plc%3A0","dd_dc":"Windows 7 - NT 6.1 ","cpnip":"1","ciid":"LO9OVXk*","nbi":"2","trunc":"true","Payload":"/sch/patti1578/m.html?_nkw=&_armrs=&_ipg=&_from=","rq":"177625d0271e345a","soid":"227213625","rv":"true","RemoteIP":"23.242.253.54","pof":"0.0","cbrnd":"0","js":"1","ur_dedupe_stats":"olp%3D58%2Cold%3D0%2Cplp%3D0%2Cpld%3D0","authsellerbadgecnt":"0","dd_d":"Desktop","sigEPChange":"0000000000000000000000000000000000000000000000000000000000","bs":"0","srpaidic":"0","bw":"1200","EventTS":"23:36:50.89","TPool":"r1srcore","!var_itm":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","ForwardedFor":"23.242.253.54,23.55.36.84,66.135.196.45,10.192.140.119","timestamp":"1629959810891","uit":"1624385934943","c":"725","to_zip":"90017","g":"9968192c1770a45e242080b3ffbaa651","h":"2c","niss":"0","gvis":"DEFAULT%3A225","TType":"URL","prof":"22222","n":"34f2527117a0acfbbc603e07fe6befa2","!itm_sdattr_cnt":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","p":"2046732","r":"2074659235","rheosUpstreamCreateTs":"1629959810891","t":"0","u":"1454500365","ans3pb":"15.0","!itmshcost":"5.65,5.45,6.99,5.89,5.65,5.99,6.99,9.25,8.99,9.45,5.45,4.89,11.99,5.99,8.85,8.45,6.89,11.0,11.99,7.99,9.99,5.45,6.99,5.0,12.45,7.99,6.89,5.88,5.99,9.99,8.75,7.99,5.99,4.95,8.99,8.45,5.49,4.99,7.75,7.99,8.0,11.85,11.99,6.45,4.99,6.45,6.45,8.99","dc":"1","!hotredesigncnt":"pq%3A0,pw%3A0,lq%3A0,sa%3A0,wp%3A0,wt%3A0,fs%3A0,mp%3A0,po%3A0,dt%3A0,zb%3A45","dd_osv":"NT 6.1","rdt":"0","rlogid":"t6pwehq%60%3C%3Dqkitqfiuf%2801%60ta*w%60ut3542-17b812cef53-0x161","acond":"0","js_ev_mak":"9968192c1770a45e242080b3ffbaa651","pageName":"SellerOtherItemRaptor","uc":"1","dsbe_dmpr":"0","uaid":"812cef4c17b0a6e557959052ff5c0fdeS0","bot_provider":"%7B%22providers%22%3A%7B%22AK%22%3A%7B%22headers%22%3A%7B%22es%22%3A%22georegion%3D246%2Ccountry_code%3DUS%2Cregion_code%3DCA%2Ccity%3DLOSANGELES%2Cdma%3D803%2Cpmsa%3D4480%2Cmsa%3D4472%2Careacode%3D323%2B213%2B310%2Ccounty%3DLOSANGELES%2Cfips%3D06037%2Clat%3D33.9733%2Clong%3D-118.2487%2Ctimezone%3DPST%2Czip%3D90001-90068%2B90070-90084%2B90086-90089%2B90091%2B90093-90096%2B90099%2B90189%2Ccontinent%3DNA%2Cthroughput%3Dvhigh%2Cbw%3D5000%2Cnetwork%3Dcharter%2Casnum%3D20001%2Cnetwork_type%3Dcable%2Clocation_id%3D0%22%7D%7D%7D%7D","nmq":"0","tab":"10","scond":"1","ul":"en-US","ec":"1","auctions":"53","bott":"0","corrId":"177625d0271e345a","eprlogid":"t6pwehq%2560%253C%253Dqkitqfiuf%252801%2560ta*w%2560ut3542-17b812cef53-0x161","itt":"48%7C0","icpp":"48","es":"0","c_rid":"e6390c730db7ad7c49ed705c68a2b787","dd_os":"Windows 7","cqc":"3496422","freeReturnCnt":"0","!clktrack":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","gitCommitId":"b41eca7fa1fbf824fd447ca3dd64a078f62bea34","nodeId":"00362d4efbbac9c0","rdthttps":"2","abshit":"110","epcalenv":"","dd_bv":"92.0.4515.159","siid":"LOHrY4I*","!gdshid":"-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-","js_sojuuid":"9e8083cd-810e-41f1-8ebd-51e6c0fdf7c1","sid":"p2545226.m2533.l4587","pcc":"0","dd_bf":"Chrome","crc":"2","gf":"LH_SpecificSeller","pagename":"SellerOtherItemRaptor","kw_l2":"63862","kw_l1":"11450","!clm_odr":"3,2,5,4,7,11","Referer":"https://www.ebay.com/usr/patti1578?_trksid=p2047675.l2559","urlQueryString":"/sch/patti1578/m.html?_nkw=&_armrs=&_ipg=&_from=","!itm":"2V66F7ZQ,2V66FAJ6,2V671Y59,2V6564P7,2V66FDU5,2V671YQR,2V671VYI,2V67A6C4,2V667WEV,2V66S167,2V671KCB,2V66RZCX,2V68N0CZ,2V67YN30,2V66GR99,2V675BX4,2V653MYY,2V653NT7,2V67YLQ3,2V66NCF6,2V66FAJD,2V66IROE,2V671ZTQ,2V66FAJ9,2V671SJN,2V674GET,2V671LV3,2V671XWO,2V66FQSJ,2V678XBD,2V66DS5S,2V65YDBL,2V66S0K5,2V4STR5C,2V671NP7,2V67YMSZ,2V66PQ04,2V667R8N,2V66FAJ8,2V65ZNTF,2V66ITHC,2V671N77,2V66DNAI,2V52QMSX,2V66JN9O,2V66FDDN,2V67A5QA,2V678WWJ","!itemshtime":"CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A8|CasMaxECalDays%3A8|CasMinEBuDays%3A6|CasMaxEBuDays%3A6,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A14|CasMaxECalDays%3A14|CasMinEBuDays%3A9|CasMaxEBuDays%3A9,CasMinECalDays%3A13|CasMaxECalDays%3A13|CasMinEBuDays%3A8|CasMaxEBuDays%3A8,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A8|CasMaxECalDays%3A8|CasMinEBuDays%3A6|CasMaxEBuDays%3A6,CasMinECalDays%3A8|CasMaxECalDays%3A8|CasMinEBuDays%3A6|CasMaxEBuDays%3A6,CasMinECalDays%3A13|CasMaxECalDays%3A13|CasMinEBuDays%3A8|CasMaxEBuDays%3A8,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A14|CasMaxECalDays%3A14|CasMinEBuDays%3A9|CasMaxEBuDays%3A9,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A7|CasMaxECalDays%3A7|CasMinEBuDays%3A5|CasMaxEBuDays%3A5,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A13|CasMaxECalDays%3A13|CasMinEBuDays%3A8|CasMaxEBuDays%3A8,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A14|CasMaxECalDays%3A14|CasMinEBuDays%3A9|CasMaxEBuDays%3A9,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A6|CasMaxECalDays%3A6|CasMinEBuDays%3A4|CasMaxEBuDays%3A4,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7,CasMinECalDays%3A10|CasMaxECalDays%3A10|CasMinEBuDays%3A7|CasMaxEBuDays%3A7","ncbt":"0","ans1pb":"15.0","pg":"notAboveThreshold","currentImprId":"521122279212","srpaidfc":"0","!gp_position":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","pn":"2"},"partialValidPage":true,"referrer":"https://www.ebay.com/usr/patti1578?_trksid=p2047675.l2559","oldSessionSkey":null,"sojHeader":null,"currentImprId":521122279212,"siteId":"0","trafficSource":null,"eventCnt":0,"eventAttr":null}}
        |""".stripMargin
    ctx.collect(JsonUtils.parseObject(json, classOf[TdqEvent]))
  }

  def genRawEvent(ctx: SourceFunction.SourceContext[TdqEvent]): Unit = {
    val sample = getSampleData
    sample.foreach(e => {
      ctx.collect(new TdqEvent(e))
    })
    // Thread.sleep(10000000)
  }

  def getSampleData: Seq[RawEvent] = try {
    val is = classOf[ProfilingJob].getResourceAsStream("/pathfinder_raw_event.txt")
    try {
      IOUtils.readLines(is).asScala.map(json => {
        val event = JsonUtils.parseObject(json, classOf[RawEvent])
        event
      })
    } finally {
      if (is != null) is.close()
    }
  }

  def test(): Unit = {
    submit(Array[String]("--flink.app.name", "tdq.dev.pathfinder", "--flink.app.local", "true"))
  }
}