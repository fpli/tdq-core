package com.ebay.tdq

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.config.TdqConfig
import com.ebay.tdq.rules.ProfilingSqlParser
import com.ebay.tdq.utils.{DateUtils, JsonUtils}
import org.junit.Test

/**
 * @author juntzhang
 */
class PhysicalPlanTest {

  @Test
  def testFindDimensionValues(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_1",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "2min"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "global_mandatory_tag_item_rate1",
        |          "dimensions": [
        |            "domain",
        |            "site"
        |          ],
        |          "config": {
        |             "pronto-dropdown": "expr.itm_cnt > 0"
        |           },
        |          "filter": "domain in ('ASQ', 'BID','BIDFLOW','BIN','BINFLOW','CART','OFFER','UNWTCH','VI','WTCH','XO') and IS_BBWOA_PAGE_WITH_ITM(page_id) and clientData.remoteIP not like '10.%' and site not in('3','4')",
        |          "expr": "itm_valid_cnt / itm_cnt",
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expr": "CAST( SOJ_NVL('p') AS INTEGER)"
        |            },
        |            {
        |              "alias": "domain",
        |              "expr": "soj_page_family(page_id)"
        |            },
        |            {
        |              "alias": "site",
        |              "expr": "SOJ_NVL('t')"
        |            },
        |            {
        |              "alias": "itm_cnt",
        |              "expr": "count(1)"
        |            },
        |            {
        |              "alias": "itm_valid_cnt",
        |              "expr": "Sum(case when CAST( SOJ_NVL('itm|itmid|itm_id|itmlist|litm') AS LONG) is not null then 1 else 0 end)"
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(config.getRules.get(0).getProfilers.get(0), window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString), new TdqEnv(), null)
    val plan = parser.parsePlan()
    println(plan)

  }

  @Test
  def testFindDimensionValues2(): Unit = {
    val json =
      """
        |{
        |  "id": "1",
        |  "rules": [
        |    {
        |      "name": "rule_1",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "2min"
        |      },
        |      "profilers": [
        |        {
        |          "metric-name": "global_mandatory_tag_item_rate1",
        |          "dimensions": [
        |            "domain",
        |            "site"
        |          ],
        |          "expr": "itm_valid_cnt / itm_cnt",
        |          "transformations": [
        |            {
        |              "alias": "page_id",
        |              "expr": "CAST( SOJ_NVL('p') AS INTEGER)"
        |            },
        |            {
        |              "alias": "domain",
        |              "expr": "soj_page_family(page_id)",
        |              "filter": "domain in ('ASQ', 'BID','BIDFLOW','BIN','BINFLOW','CART','OFFER','UNWTCH','VI','WTCH','XO')"
        |            },
        |            {
        |              "alias": "site",
        |              "expr": "SOJ_NVL('t')",
        |              "filter": "site not in('3','4')"
        |            },
        |            {
        |              "alias": "itm_cnt",
        |              "expr": "count(1)"
        |            },
        |            {
        |              "alias": "itm_valid_cnt",
        |              "expr": "Sum(case when CAST( SOJ_NVL('itm|itmid|itm_id|itmlist|litm') AS LONG) is not null then 1 else 0 end)"
        |            }
        |          ]
        |        }
        |      ]
        |    }
        |  ]
        |}
        |""".stripMargin
    val config = JsonUtils.parseObject(json, classOf[TdqConfig])
    val parser = new ProfilingSqlParser(config.getRules.get(0).getProfilers.get(0), window = DateUtils.toSeconds(config.getRules.get(0).getConfig.get("window").toString), new TdqEnv(), null)
    val plan = parser.parsePlan()
    println(plan)

  }
}
