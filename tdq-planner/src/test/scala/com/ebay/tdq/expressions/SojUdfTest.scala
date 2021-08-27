package com.ebay.tdq.expressions

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.{TdqEvent, InternalMetric}
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.planner.LkpManagerTest
import com.ebay.tdq.planner.utils.udf
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.apache.avro.Schema
import org.junit.{Assert, BeforeClass, Test}

import scala.collection.JavaConverters._

/**
 * @author juntzhang
 */
object SojUdfTest {
  @BeforeClass
  def setup(): Unit = {
    LkpManagerTest.init()
  }
}

class SojUdfTest {
  val eventTime: Long = System.currentTimeMillis()
  val schema: Schema = new Schema.Parser().parse(
    """
      |{
      |  "type": "record",
      |  "name": "TdqEvent",
      |  "namespace": "com.ebay.tdq.common.model",
      |  "fields": [
      |    { "name": "event_timestamp",   "type": "long"                                                 },
      |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
      |  ]
      |}
      |""".stripMargin)

  def getTdqConfig(expr1: String, expr2: String): TdqConfig = {
    val json =
      s"""
         |{
         |  "id": "ignore",
         |  "rules": [
         |    {
         |      "name": "ignore",
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

  def test(expr1: String, expr2: String, schema: Schema, createEvent: () => TdqEvent, assertFunction: InternalMetric => Unit): Unit = {
    val config = getTdqConfig(expr1, expr2)
    val parser = new ProfilingSqlParser(
      config.getRules.get(0).getProfilers.get(0),
      window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString),
      new TdqEnv(),
      schema = schema
    )
    val plan = parser.parsePlan()
    println(plan)
    val metric = plan.process(createEvent())
    assert(metric != null)
    assertFunction.apply(metric)
  }

  def testExpr(expr: String, ret: String): Unit = {
    test(s"case when p2=$ret then 1 else 0 end", expr, schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }

  def testExprNUll(expr: String): Unit = {
    val schema = new Schema.Parser().parse(
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin)
    test(s"case when p2 is null then 1 else 0 end", expr, schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }

  @Test
  def soj_page_family(): Unit = {
    testExpr("soj_page_family(5780)", "'GR'")
    testExpr("soj_page_family(578000000)", "'Others'")
  }

  @Test
  def soj_decode_base36_vec(): Unit = {
    val hive = new udf.soj.Base36Decoder().evaluate("2TB749SB,2AWANTQH,,")
    Assert.assertEquals("220532100491,180448976969", hive)
    testExpr("soj_decode_base36_vec('2TB749SB,2AWANTQH,,')", "'220532100491,180448976969'")
  }

  @Test
  def soj_parse_clientinfo(): Unit = {
    val hive = new udf.soj.ClientInfoParser().evaluate("&Server&Server=123456&TName=123", "Server")
    Assert.assertEquals("123456", hive)
    testExpr("soj_parse_clientinfo('&Server&Server=123456&TName=123','Server')", "'123456'")
  }


  @Test
  def soj_decode_base64(): Unit = {
    val hive = new udf.soj.EbayBase64().evaluate("anVudHpoYW5n")
    Assert.assertEquals("juntzhang", hive)
    testExpr("soj_decode_base64('anVudHpoYW5n')", "'juntzhang'")
  }

  @Test
  def soj_extract_flag(): Unit = {
    val hive = new udf.soj.ExtractFlag().evaluate("1234", 3)
    Assert.assertEquals(1, hive)
    testExpr("SOJ_EXTRACT_FLAG('1234',4)", "0")
    testExpr("SOJ_EXTRACT_FLAG('AAAA',4)", "0")
    testExpr("SOJ_EXTRACT_FLAG('aaaa',4)", "1")
    testExpr("SOJ_EXTRACT_FLAG(null,4)", "0")
    testExpr("SOJ_EXTRACT_FLAG('Agg2YCAAIhQA',20)", "0")
  }

  @Test
  def soj_get_page_type(): Unit = {
    testExpr("SOJ_GET_PAGE_TYPE('290499010052','QhAZMKAAAABAAAIANwg*',0, 4340, 5, 0)", "5")
    testExpr("SOJ_GET_PAGE_TYPE('290499010052','QhAZMKAAAABAAAIANwg*',0, 4340, -1, 0)", "-1")

  }

  @Test
  def soj_get_url_domain(): Unit = {
    testExpr("SOJ_GET_URL_DOMAIN('http://test.com?param')", "'test.com?param'")
    testExpr("SOJ_GET_URL_DOMAIN('http://shop.ebay.com/xyz/abc?_nkw=ebay+uk&l1=2&sacat=456')", "'shop.ebay.com'")
    testExpr("SOJ_GET_URL_DOMAIN('http://user:password@www.ebay.com:80')", "'www.ebay.com'")
    testExprNUll("SOJ_GET_URL_DOMAIN('http:/test.com/a@fda/123')")
  }

  @Test
  def soj_get_url_params(): Unit = {
    testExpr("soj_get_url_params('http://shop.ebay.com/xyz/abc?_nkw=ebay+uk&l1=2&sacat=456')", "'_nkw=ebay+uk&l1=2&sacat=456'")
    testExprNUll("soj_get_url_params('http:/')")
    testExprNUll("soj_get_url_params('')")
    testExprNUll("soj_get_url_params(null)")
  }

  @Test
  def soj_get_url_path(): Unit = {
    testExpr("soj_get_url_path('http://shop.ebay.com/xyz/abc?_nkw=ebay+uk&l1=2&sacat=456')", "'/xyz/abc'")
    testExpr("soj_get_url_path('http://shop.ebay.com','/xyz/abc?_nkw=ebay+uk&l1=2&sacat=456')", "'/xyz/abc'")
    testExpr("soj_get_url_path('http://shop.ebay.com/')", "'/'")
    testExprNUll("soj_get_url_path('http:/')")
    testExprNUll("soj_get_url_path('')")
    testExprNUll("soj_get_url_path(null)")
  }

  @Test
  def soj_guid_ts(): Unit = {
    testExpr("soj_guid_ts('bb78f16f13a0a0aa17865356ff92ee92')", "'2012-11-01 10:16:36.463'")
  }

  @Test
  def soj_guid_ip(): Unit = {
    testExpr("soj_guid_ip('bb78f16f13a0a0aa17865356ff92ee92')", "'10.10.161.120'")
    testExprNUll("soj_guid_ip('123')")
  }

  @Test
  def soj_guid_sampling(): Unit = {
    testExpr("soj_guid_sampling('bb78f16f13a0a0aa17865356ff92ee92',1)", "false")
    testExpr("soj_guid_sampling('bb78f16f13a0a0aa17865356ff92ee92',100)", "true")
  }

  @Test
  def is_bigint(): Unit = {
    testExpr("soj_is_bigint('123')", "1")
    testExpr("soj_is_bigint('123a')", "0")
  }

  @Test
  def is_bitSet(): Unit = {
    testExpr("soj_is_bitSet('1232',2)", "true")
  }

  @Test
  def is_decimal(): Unit = {
    testExpr("soj_is_decimal('1232.22',18,2)", "1")
    testExpr("soj_is_decimal('1232.22',2,0)", "0")
    testExpr("soj_is_decimal('1232a',18,2)", "0")
    testExpr("is_decimal('1232a',18)","false")
  }

  @Test
  def is_integer(): Unit = {
    testExpr("soj_is_integer('1232')", "1")
    testExpr("soj_is_integer('1232a')", "0")
  }

  @Test
  def is_validIPv4(): Unit = {
    testExpr("soj_is_validIPv4('127.0.0.1')", "1")
    testExpr("soj_is_validIPv4('127.0.0')", "0")
  }

  @Test
  def is_validPrivateIPv4(): Unit = {
    testExpr("soj_is_validPrivateIPv4('127.0.0.1')", "false")
    testExpr("soj_is_validPrivateIPv4('192.168.0.1')", "true")
    testExpr("soj_is_validPrivateIPv4(' 202.76.247.150')", "false")
  }

  @Test
  def soj_sample_hash(): Unit = {
    testExpr("soj_sample_hash('5e525f491310a5a9631335c3ff79ba26', 100)", "1")
  }

  @Test
  def soj_base64tobigint(): Unit = {
    testExpr("soj_base64tobigint('iIDWAAE*')", "4309024904")
  }

  @Test
  def soj_collapse_whitespace(): Unit = {
    testExpr("soj_collapse_whitespace('ipad    white   16gb')", "'ipad white 16gb'")
  }

  @Test
  def soj_extract_nvp(): Unit = {
    testExpr("soj_extract_nvp('h8=d3&h4=d9&', 'h4', '&', '=')", "'d9'")
    testExpr("soj_extract_nvp('bn:7024841724|', 'bn', '|', ':')", "'7024841724'")
  }

  @Test
  def soj_get_base64encoded_bits_set(): Unit = {
    testExpr("soj_get_base64encoded_bits_set('AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAACEMAggAAAIoGEElQAzgBKACIAXBEAIIiBQAAAAABEIAgAACIABAAAAKACBAEQAIAAQAIABAAAAEEAAAAEIAAIBSJAAwARARAgiA')", "'607,634,639,644,645,654,660,686,690,692,699,700,705,711,714,717,719,721,732,733,736,737,738,749,752,754,766,770,781,783,784,785,791,795,806,812,816,820,827,829,869,873,878,888,910,914,929,956,958,970,977,987,991,1004,1021,1034,1049,1077,1083,1113,1118,1136,1145,1147,1150,1154,1157,1170,1171,1183,1187,1195,1199,1206,1212,1216'")
  }

  @Test
  def soj_get_browser_type(): Unit = {
    testExpr("soj_get_browser_type( 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 2.0.50727)' )", "'IE'")
  }

  @Test
  def soj_get_browser_version(): Unit = {
    testExpr("soj_get_browser_version( 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 2.0.50727)' )", "'6.x'")
  }

  @Test
  def soj_get_OS(): Unit = {
    testExpr("soj_get_OS( 'Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 2.0.50727)' )", "'Windows XP'")
    testExpr("soj_get_OS( 'Opera/9.52 (Windows NT 5.1; U; en)' )", "'Windows XP'")
  }

  @Test
  def soj_java_hash(): Unit = {
    testExpr("soj_java_hash('0000007e12a0a0e20196e7f6fe84b8e5',NULL,'4000',100)", "79")
  }

  @Test
  def soj_json_parse(): Unit = {
    testExpr("""soj_json_parse('{\"name\":\"juntzhang\"}','name')""", "'juntzhang'")
  }

  @Test
  def soj_list_element_count(): Unit = {
    testExpr("soj_list_element_count('abc,def',',')", "2")
    testExpr("soj_list_element_count('abc,defc,hef,,,','')", "1")
    testExprNUll("soj_list_element_count(null,',')")
  }

  @Test
  def soj_str_reverse(): Unit = {
    testExpr("soj_str_reverse('test')", "'tset'")
  }

  @Test
  def soj_url_decode_escapes(): Unit = {
    testExpr("soj_url_decode_escapes('BRAND%3ACalvin%2BKlein+Gucci+Polo%2BRalph%2BLauren+Prada+Ralph%2BLauren+Tommy%2BHilfiger%3BCONDITION%3ANew%3B', '%')", "'BRAND:Calvin+Klein+Gucci+Polo+Ralph+Lauren+Prada+Ralph+Lauren+Tommy+Hilfiger;CONDITION:New;'")
  }

  @Test
  def soj_list_get_val_by_idx(): Unit = {
    testExpr("soj_list_get_val_by_idx('abc,defc,hef',',',2)", "'defc'")
  }

  @Test
  def soj_parse_rlogid(): Unit = {
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','MacHine')", "'slc4b01c-97737d'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','datacenter')", "'SLC'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','pool')", "'r1srcore'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','machine')", "'slc4b01c-97737d'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','timestamp')", "'2012-08-29 10:59:39'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','THREADID')", "'0xbc'")
    testExpr("soj_parse_rlogid('t6pwehq%60%3C%3Dpie3a57d.%3C1002b-1397389e1f0-0xbc','poolmachine')", "'r1srcore::slc4b01c-97737d'")
  }

  @Test
  def soj_ts_date(): Unit = {
    testExpr("soj_ts_date(3586117768123000)", "'2013/08/21 23:49:28'")
  }

  @Test
  def soj_ts_mils(): Unit = {
    testExpr("soj_ts_mils(3586117768123000)", "'2013/08/21 23:49:28.123'")
  }

  @Test
  def string_ts_to_sojts(): Unit = {
    testExpr("string_ts_to_sojts('2013-08-21 23:49:28.123')", "3586117768123000")
  }

  @Test
  def string_date_to_sojts(): Unit = {
    testExpr("string_date_to_sojts('2013-08-21 23:49:28')", "3586032000000000")
  }

  @Test
  def decode_plmt_tag(): Unit = {
    testExpr("decode_plmt_tag('LAgAAB%252BLCAAAAAAAAADNlE1u3DAMhe%252BitQuQkiiRc5WiC4qiigGmmUGTFgWC3L10knbhE3j1aFuP1gf%252BvKbH93TJUHvrtKXHNV2%252BvqbrTBcEkMJbsnj1Rbb0MzQpdTTOo%252BWx6prMrr2AMU8Z7NTSlvwlUnwL1Y9UnxnDnG%252BtP5E94tCPdHn6dbtt6fn%252BL3qkS2HBvKUVP3%252BLDLqbRAQAw6K%252F42k%252FutvjC4T8eZe7vYs%252Bf8j9024ve7D9p%252Bm5H2imVmBcrZYJdVAXRs2qhnPR7LTOTEONjjTi6IVyNac6p%252BicWUCZXGgCjHPTHGvTOOqQtXdCqbBwsBQamh3HdPKT0%252BQDzWLLbe3dVkYFa6PVrpgHNJ0lA5yZRvC4BeLGXkabbia1ZFEDhmy50hrqfOraCMqBBq1FpxUQ41a5N41F0WqLXuu1qdm5adqBxrmgTSxdxqhVRbDXmBiMMcJp%252FdQ7Le56oBloIH3oJO2V2bgqGbhhb4sq%252BZlpqB3nxosV7sPnyBy1AWnuzm4r58KKcj6aCMY1ohQr6u0vZSiPKywIAAA%253D')",
      """'{\"pg\":2047675,\"pi\":[{\"id\":100938,\"ci\":-9,\"ri\":\"a571c82b62bf4fd88ea730c88d9b8e56\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100727,\"ci\":-9,\"ri\":\"da4081f643d04b57981a2aac1df5d75f\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100565,\"ci\":-9,\"ri\":\"d9e1e3524ce54dd9add290a85e95d00b\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100567,\"ci\":-9,\"ri\":\"68ac12a7751940f1b8935ba2e1bde5eb\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100562,\"ci\":-9,\"ri\":\"f8c26f40813b40c6b647a12b06ad3200\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100918,\"ci\":-9,\"ri\":\"320e3b6decc94329ac0802c245fbae8b\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100919,\"ci\":-9,\"ri\":\"1c6c12309c864876a727646ba2746acc\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100916,\"ci\":-9,\"ri\":\"e831cd1379bb44a99174bde15191dc7f\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100917,\"ci\":-9,\"ri\":\"b1c097bad5a7488c84a5c0ec176f545e\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1},{\"id\":100568,\"ci\":-9,\"ri\":\"e3c387bedb2844a096eee8ecf2238a19\",\"et\":[],\"ea\":[{\"ii\":[{\"i\":\"2l67n5cp\",\"m\":null,\"so\":null,\"p\":38912,\"f\":1}],\"ai\":\"999001\",\"av\":\"null\",\"mi\":0,\"mx\":0,\"oc\":0,\"as\":0,\"ao\":1}],\"ct\":1}],\"bit\":\"00\"}'""")
  }

  @Test
  def soj_get_top_domain(): Unit = {
    testExpr("soj_get_top_domain('www.ebay.com')", "'ebay'")
  }

  @Test
  def soj_url_extract_nvp(): Unit = {
    testExpr("soj_url_extract_nvp('/ebaymotors/Nissan-Rogue-Rear-View-Mirror-Built-Monitor-Oem-_W0QQcmdZViewItemQQhashZitem3a5ecdbcbcQQitemZ250698644668QQptZCarQ5fAudioQ5fVideo', 'hash', 1 )", "'item3a5ecdbcbc'")
    testExpr("soj_url_extract_nvp('QQaZ1QQbZ2QQcZ3', 'b', 1 )", "'2'")
  }

  @Test
  def soj_str_between_str(): Unit = {
    testExpr("soj_str_between_str('Opera/9.80 (Windows NT 5.1; U; en) Presto/2.2.15 Version/10.10', '(', ')' )", "'Windows NT 5.1; U; en'")
  }

  @Test
  def soj_list_get_range_by_idx(): Unit = {
    testExpr("soj_list_get_range_by_idx('abc,defc,hef',',',2,3)", "'defc,hef'")
  }


  @Test
  def soj_list_last_element(): Unit = {
    testExpr("soj_list_last_element('abc,defc,hef',',')", "'hef'")
  }

  @Test
  def soj_md5_hash_32(): Unit = {
    testExpr("soj_md5_hash_32('0000007e12a0a0e20196e7f6fe84b8e5',NULL,NULL,100)", "2")
  }

  @Test
  def soj_md5_hash_128(): Unit = {
    testExpr("soj_md5_hash_128('0000007e12a0a0e20196e7f6fe84b8e5',NULL,'4000',100)", "46")
  }

  @Test
  def soj_replace_char(): Unit = {
    testExpr("soj_replace_char('ab?cdef?(junk_)','?()_','')", "'abcdefjunk'")
  }

  @Test
  def soj_replace_rchar(): Unit = {
    testExpr("soj_replace_rchar('ab?cdef?(junk_)','?()_','')", "'??(_)'")
  }


  @Test
  def soj_str2date(): Unit = {
    testExpr("soj_str2date('2013-08-21')", "'3586032000000000'")
  }

  @Test
  def soj_ts_to_td(): Unit = {
    testExpr("soj_ts_to_td('2013-08-21 23:49:28.123')", "'3586117768123000'")
  }

  @Test
  def soj_nvl2(): Unit = {
    testExpr("soj_nvl2('abc=123','abc')", "'123'")
  }

  @Test
  def soj_timestamp2Date(): Unit = {
    testExpr("soj_timestamp2Date(3586117768123000)", "'2013/08/21 23:49:28.123'")
  }

  @Test
  def soj_value_in_list(): Unit = {
    testExpr("soj_value_in_list('abc,defc,hef',',','defc')", "'2'")
  }

  @Test
  def soj_str_between_endlist(): Unit = {
    testExpr("soj_str_between_endlist('Opera/9.80 (Windows NT 5.1; U; en) Presto/2.2.15 Version/10.10','(',')')", "'Windows NT 5.1; U; en'")
  }

  @Test
  def cstr_strchr(): Unit = {
    testExpr("cstr_strchr('www.ebay.com','.')", "'.ebay.com'")
  }

  @Test
  def IsTimestamp(): Unit = {
    testExpr("is_timestamp('2009-01-01 10:00:02',0)", "1")
  }


  @Test
  def soj_get_ua_version(): Unit = {
    testExpr("soj_get_ua_version('Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 4.0; KHTE B459418859A38001T821751 )', 1)", "'4.0'")
  }

  @Test
  def soj_str_between(): Unit = {
    testExpr("soj_str_between( 'Opera/9.80 (Windows NT 5.1; U; en) Presto/2.2.15 Version/10.10','(',')')", "'Windows NT 5.1; U; en'")
  }

  @Test
  def soj_str_between_list(): Unit = {
    testExpr("soj_str_between_list( 'Opera/9.80 (Windows NT 5.1; U; en) Presto/2.2.15 Version/10.10','(',')')", "'Windows NT 5.1; U; en'")
  }

  @Test
  def udf_soj_get_b64_bit_postns(): Unit = {
    testExpr("udf_soj_get_b64_bit_postns('BA**',0)", "'5,'")
  }

  @Test
  def decodeSRPItmcardSig(): Unit = {
    testExpr("decodeSRPItmcardSig('NGQoACIQAA%3D%3D%2CCCJkKAACCAA%3D%2CImAoAIIAAA%3D%3D%2CAmAoAAIgAA%3D%3D%2CAkAoAAIgAA%3D%3D%2CAkAoAAIgAA%3D%3D%2CImQoAAIAAA%3D%3D%2CAkAoAAIAAA%3D%3D%2CImQpAAIgAA%3D%3D%2CFEIICCIRAA%3D%3D%2CImAoAAI',14,65)", "0")
  }

  @Test
  def soj_parser(): Unit = {
    val schema = new Schema.Parser().parse(
      """
        |{
        |  "type": "record",
        |  "name": "TdqEvent",
        |  "namespace": "com.ebay.tdq.common.model",
        |  "fields": [
        |    { "name": "event_timestamp",   "type": "long"                                                 },
        |    { "name": "payload",           "type": [ "null", { "type": "map", "values": "string" }]       }
        |  ]
        |}
        |""".stripMargin)
    test(s"case when p2 is not null then 1 else 0 end", "soj_parser('dpi=332.509x331.755&mppid=26','abc_id,mtId=mtid|mt_id,mppid')", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }

  @Test
  def soj_parser_no_decode(): Unit = {
    test(s"case when p2 is not null then 1 else 0 end", "soj_parser_no_decode('dpi=332.509x331.755&mppid=26','abc_id,mtId=mtid|mt_id,mppid')", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }

  @Test
  def soj_str_to_map(): Unit = {
    test(s"case when p2 is not null then 1 else 0 end", "soj_str_to_map('1,2,3,4,5,',',')", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }

  @Test
  def soj_map_to_str(): Unit = {
    test(s"case when p2 is not null then 1 else 0 end", "soj_map_to_str(soj_parser_no_decode('dpi=332.509x331.755&mppid=26','abc_id,mtId=mtid|mt_id,mppid'))", schema, () => {
      val tdqEvent = new TdqEvent(
        Map(
          "event_timestamp" -> eventTime,
          "payload" -> Map(
          ).asJava
        ).mapValues(_.asInstanceOf[Object]).asJava
      )
      tdqEvent.buildEventTime(eventTime)
      tdqEvent
    }, metric => {
      println(metric)
      Assert.assertEquals(1d, metric.getValues.get("p1"))
    })
  }



}
